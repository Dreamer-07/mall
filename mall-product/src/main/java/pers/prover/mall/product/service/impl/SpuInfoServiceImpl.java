package pers.prover.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;
import pers.prover.mall.common.to.SkuReductionTo;
import pers.prover.mall.common.to.SpuBoundTo;
import pers.prover.mall.common.utils.PageUtils;
import pers.prover.mall.common.utils.Query;

import pers.prover.mall.common.utils.R;
import pers.prover.mall.product.client.CouponFeignClient;
import pers.prover.mall.product.dao.SpuInfoDao;
import pers.prover.mall.product.entity.AttrEntity;
import pers.prover.mall.product.entity.SkuInfoEntity;
import pers.prover.mall.product.entity.SpuInfoEntity;
import pers.prover.mall.product.service.*;
import pers.prover.mall.product.vo.SpuSaveVo;


@Service("spuInfoService")
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {

    @Autowired
    private SpuInfoDescService spuInfoDescService;

    @Autowired
    private SpuImagesService spuImagesService;

    @Autowired
    private ProductAttrValueService productAttrValueService;

    @Autowired
    private SkuInfoService skuInfoService;

    @Autowired
    private SkuImagesService skuImagesService;

    @Autowired
    private SkuSaleAttrValueService skuSaleAttrValueService;

    @Autowired
    private CouponFeignClient couponFeignClient;

    @Override

    public PageUtils queryPage(Map<String, Object> params) {
        String key = (String) params.get("key");
        Long catelogId = strCovertLong((String) params.get("catelogId"));
        Long brandId = strCovertLong((String) params.get("brandId"));
        Integer status = strCoverInt((String) params.get("status"));

        LambdaQueryWrapper<SpuInfoEntity> selectSpuLqw = new LambdaQueryWrapper<SpuInfoEntity>()
                .and(!StringUtils.isBlank(key), (qw) -> {
                    qw.eq(SpuInfoEntity::getId, key).or().like(SpuInfoEntity::getSpuName, key);
                })
                .eq(catelogId != 0L, SpuInfoEntity::getCatalogId, catelogId)
                .eq(brandId != 0L, SpuInfoEntity::getBrandId, brandId)
                .eq(status != 0, SpuInfoEntity::getPublishStatus, status);

        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                selectSpuLqw
        );

        return new PageUtils(page);
    }

    @Override
    @Transactional
    public void save(SpuSaveVo spuSaveVo) {
        // 保存 spu 基本信息
        SpuInfoEntity spuInfoEntity = new SpuInfoEntity();
        BeanUtils.copyProperties(spuSaveVo, spuInfoEntity);
        spuInfoEntity.setCreateTime(new Date());
        spuInfoEntity.setUpdateTime(new Date());
        this.save(spuInfoEntity);

        Long spuInfoId = spuInfoEntity.getId();

        // 保存 spu 描述信息
        spuInfoDescService.save(spuInfoId, spuSaveVo.getDecript());

        // 保存 spu 图集信息
        spuImagesService.save(spuInfoId, spuSaveVo.getImages());

        // 保存 spu 规格参数
        productAttrValueService.save(spuInfoId, spuSaveVo.getBaseAttrs());

        // 保存 spu 积分信息
        SpuBoundTo spuBoundTo = new SpuBoundTo();
        spuBoundTo.setSpuId(spuInfoId);
        spuBoundTo.setBuyBounds(spuSaveVo.getBounds().getBuyBounds());
        spuBoundTo.setGrowBounds(spuBoundTo.getGrowBounds());
        R saveSpuBoundsResult = couponFeignClient.saveSpuBounds(spuBoundTo);
        if (saveSpuBoundsResult.getCode() != 0) {
            throw new RuntimeException("保存积分信息失败");
        }

        spuSaveVo.getSkus().forEach(sku -> {
            // 获取默认图片
            String defaultImg = "";
            for (SpuSaveVo.Skus.Images image : sku.getImages()) {
                if (image.getDefaultImg() == 1) {
                    defaultImg = image.getImgUrl();
                    break;
                }
            }

            // 保存 sku 基本信息
            Long skuId = skuInfoService.save(sku, spuInfoEntity, defaultImg);

            // 保存 sku 销售属性
            skuSaleAttrValueService.save(skuId, sku.getAttr());

            // 保存 sku 图集信息
            skuImagesService.save(skuId, sku.getImages());

            // 保存会员等级优惠相关信息
            SkuReductionTo skuReductionTo = new SkuReductionTo();
            BeanUtils.copyProperties(sku, skuReductionTo);
            skuReductionTo.setSkuId(skuId);
            R saveSkuReductionResult = couponFeignClient.saveSkuReduction(skuReductionTo);
            if (saveSkuReductionResult.getCode() != 0) {
                throw new RuntimeException("保存sku积分信息失败");
            }
        });
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