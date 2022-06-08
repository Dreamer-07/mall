package pers.prover.mall.product.service.impl;

import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import pers.prover.mall.common.utils.PageUtils;
import pers.prover.mall.common.utils.Query;

import pers.prover.mall.product.client.WareFeignClient;
import pers.prover.mall.product.dao.SkuInfoDao;
import pers.prover.mall.product.entity.SkuImagesEntity;
import pers.prover.mall.product.entity.SkuInfoEntity;
import pers.prover.mall.product.entity.SpuInfoDescEntity;
import pers.prover.mall.product.entity.SpuInfoEntity;
import pers.prover.mall.product.service.*;
import pers.prover.mall.product.vo.SpuSaveVo;
import pers.prover.mall.product.vo.api.SkuItemVo;


@Service("skuInfoService")
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoDao, SkuInfoEntity> implements SkuInfoService {

    @Autowired
    private ExecutorService baseExecutor;

    @Autowired
    private SpuInfoDescService spuInfoDescService;

    @Autowired
    private SkuImagesService skuImagesService;

    @Autowired
    private ProductAttrValueService productAttrValueService;

    @Autowired
    private SkuSaleAttrValueService skuSaleAttrValueService;

    @Autowired
    private WareFeignClient wareFeignClient;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String key = (String) params.get("key");
        Long catelogId = strCovertLong((String) params.get("catelogId"));
        Long brandId = strCovertLong((String) params.get("brandId"));
        Integer max = strCoverInt((String) params.get("max"));
        Integer min = strCoverInt((String) params.get("brandId"));

        LambdaQueryWrapper<SkuInfoEntity> selectSkuLqw = new LambdaQueryWrapper<SkuInfoEntity>()
                .and(!StringUtils.isBlank(key), (qw) -> {
                    qw.eq(SkuInfoEntity::getSkuId, key).or().like(SkuInfoEntity::getSkuId, key);
                })
                .eq(catelogId != 0L, SkuInfoEntity::getCatalogId, catelogId)
                .eq(brandId != 0L, SkuInfoEntity::getBrandId, brandId)
                .le(max != 0, SkuInfoEntity::getPrice, max)
                .ge(min != 0, SkuInfoEntity::getPrice, min);

        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                selectSkuLqw
        );

        return new PageUtils(page);
    }

    @Override
    public Long save(SpuSaveVo.Skus skuReqVp, SpuInfoEntity spuInfoEntity, String defaultImg) {
        SkuInfoEntity skuInfoEntity = new SkuInfoEntity();
        BeanUtils.copyProperties(skuReqVp, skuInfoEntity);
        skuInfoEntity.setSpuId(spuInfoEntity.getId());
        skuInfoEntity.setCatalogId(spuInfoEntity.getCatalogId());
        skuInfoEntity.setBrandId(spuInfoEntity.getBrandId());
        skuInfoEntity.setSkuDefaultImg(defaultImg);
        skuInfoEntity.setSaleCount(0L);
        this.save(skuInfoEntity);
        return skuInfoEntity.getSkuId();
    }

    @Override
    public String getSkuName(Long skuId) {
        return this.getById(skuId).getSkuName();
    }

    @Override
    public List<SkuInfoEntity> listBySpuId(Long spuId) {
        LambdaQueryWrapper<SkuInfoEntity> selectLqw = new LambdaQueryWrapper<>();
        selectLqw.eq(SkuInfoEntity::getSpuId, spuId);
        return this.list(selectLqw);
    }

    @Override
    public SkuItemVo getSkuItem(Long skuId) {
        SkuItemVo skuItemVo = new SkuItemVo();

        // 查找 sku 基本信息
        CompletableFuture<SkuInfoEntity> skuInfoEntityCompletableFuture = CompletableFuture.supplyAsync(() -> {
            SkuInfoEntity skuInfoEntity = this.getById(skuId);
            skuItemVo.setSkuInfoEntity(skuInfoEntity);
            return skuInfoEntity;
        }, baseExecutor);

        // 查找库存信息
        CompletableFuture<Void> stockInfoCF = CompletableFuture.runAsync(() -> {
            Map<Long, Boolean> stockInfoMap = wareFeignClient.stockInfo(Collections.singletonList(skuId)).getData(new TypeReference<Map<Long, Boolean>>() {
            });
            Boolean stockInfo = stockInfoMap.get(skuId);
            skuItemVo.setHasStock(stockInfo);
        }, baseExecutor);

        // 查找 spu 的销售属性
        CompletableFuture<Void> spuSaleAttrCF = skuInfoEntityCompletableFuture.thenAcceptAsync((skuInfoEntity -> {
            Long spuId = skuInfoEntity.getSpuId();
            List<SkuItemVo.SpuSaleAttrVo> spuSaleAttrVoList = skuSaleAttrValueService.saleAttrList(spuId);
            skuItemVo.setSpuSaleAttrVoList(spuSaleAttrVoList);
        }), baseExecutor);

        // 查找 spu 的商品介绍
        CompletableFuture<Void> spuInfoDescCF = skuInfoEntityCompletableFuture.thenAcceptAsync((skuInfoEntity -> {
            Long spuId = skuInfoEntity.getSpuId();
            SpuInfoDescEntity spuInfoDescEntity = spuInfoDescService.getById(spuId);
            skuItemVo.setSpuInfoDescEntity(spuInfoDescEntity);
        }), baseExecutor);

        // 查找 spu 的规格参数
        CompletableFuture<Void> spuBaseAttrCF = skuInfoEntityCompletableFuture.thenAcceptAsync((skuInfoEntity -> {
            Long catalogId = skuInfoEntity.getCatalogId();
            Long spuId = skuInfoEntity.getSpuId();
            List<SkuItemVo.SpuBaseAttrVo> spuBaseAttrVoList = productAttrValueService.baseAttrList(catalogId, spuId);
            skuItemVo.setSpuBaseAttrVoList(spuBaseAttrVoList);
        }), baseExecutor);

        // 查找 sku 的图片信息
        CompletableFuture<Void> skuImagesCF = CompletableFuture.runAsync(() -> {
            List<SkuImagesEntity> skuImagesEntityList = skuImagesService.listBySkuId(skuId);
            skuItemVo.setSkuImagesEntityList(skuImagesEntityList);
        }, baseExecutor);

        // 等待所有异步任务完成后返回
        try {
            CompletableFuture.allOf(stockInfoCF, spuInfoDescCF, spuSaleAttrCF, spuBaseAttrCF, skuImagesCF).get();
            return skuItemVo;
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("获取 sku 详情信息失败");
        }
    }

    private Long strCovertLong(String str) {
        if (StringUtils.isBlank(str)) {
            return 0L;
        }
        return Long.parseLong(str);
    }

    private Integer strCoverInt(String str) {
        if (StringUtils.isBlank(str)) {
            return 0;
        }
        return Integer.parseInt(str);
    }

}