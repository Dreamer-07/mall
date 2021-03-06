package pers.prover.mall.product.service.impl;

import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;
import pers.prover.mall.common.constant.ProductConstant;
import pers.prover.mall.common.to.SkuReductionTo;
import pers.prover.mall.common.to.SpuBoundTo;
import pers.prover.mall.common.to.es.ProductMapping;
import pers.prover.mall.common.utils.PageUtils;
import pers.prover.mall.common.utils.Query;

import pers.prover.mall.common.utils.R;
import pers.prover.mall.product.client.CouponFeignClient;
import pers.prover.mall.product.client.SearchFeignClient;
import pers.prover.mall.product.client.WareFeignClient;
import pers.prover.mall.product.dao.SpuInfoDao;
import pers.prover.mall.product.entity.AttrEntity;
import pers.prover.mall.product.entity.ProductAttrValueEntity;
import pers.prover.mall.product.entity.SkuInfoEntity;
import pers.prover.mall.product.entity.SpuInfoEntity;
import pers.prover.mall.product.service.*;
import pers.prover.mall.product.vo.SpuSaveVo;


@Service("spuInfoService")
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {

    @Autowired
    private BrandService brandService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private AttrService attrService;

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

    @Autowired
    private SearchFeignClient searchFeignClient;

    @Autowired
    private WareFeignClient wareFeignClient;

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
        // ?????? spu ????????????
        SpuInfoEntity spuInfoEntity = new SpuInfoEntity();
        BeanUtils.copyProperties(spuSaveVo, spuInfoEntity);
        spuInfoEntity.setCreateTime(new Date());
        spuInfoEntity.setUpdateTime(new Date());
        this.save(spuInfoEntity);

        Long spuInfoId = spuInfoEntity.getId();

        // ?????? spu ????????????
        spuInfoDescService.save(spuInfoId, spuSaveVo.getDecript());

        // ?????? spu ????????????
        spuImagesService.save(spuInfoId, spuSaveVo.getImages());

        // ?????? spu ????????????
        productAttrValueService.save(spuInfoId, spuSaveVo.getBaseAttrs());

        // ?????? spu ????????????
        SpuBoundTo spuBoundTo = new SpuBoundTo();
        spuBoundTo.setSpuId(spuInfoId);
        spuBoundTo.setBuyBounds(spuSaveVo.getBounds().getBuyBounds());
        spuBoundTo.setGrowBounds(spuBoundTo.getGrowBounds());
        R saveSpuBoundsResult = couponFeignClient.saveSpuBounds(spuBoundTo);
        if (saveSpuBoundsResult.getCode() != 0) {
            throw new RuntimeException("????????????????????????");
        }

        spuSaveVo.getSkus().forEach(sku -> {
            // ??????????????????
            String defaultImg = "";
            for (SpuSaveVo.Skus.Images image : sku.getImages()) {
                if (image.getDefaultImg() == 1) {
                    defaultImg = image.getImgUrl();
                    break;
                }
            }

            // ?????? sku ????????????
            Long skuId = skuInfoService.save(sku, spuInfoEntity, defaultImg);

            // ?????? sku ????????????
            skuSaleAttrValueService.save(skuId, sku.getAttr());

            // ?????? sku ????????????
            skuImagesService.save(skuId, sku.getImages());

            // ????????????????????????????????????
            SkuReductionTo skuReductionTo = new SkuReductionTo();
            BeanUtils.copyProperties(sku, skuReductionTo);
            skuReductionTo.setSkuId(skuId);
            R saveSkuReductionResult = couponFeignClient.saveSkuReduction(skuReductionTo);
            if (saveSkuReductionResult.getCode() != 0) {
                throw new RuntimeException("??????sku??????????????????");
            }
        });
    }

    @Override
    public void productUp(Long spuId) {
        // 1. ??????????????? sku
        List<SkuInfoEntity> skuInfoEntityList = skuInfoService.listBySpuId(spuId);

        // 2. ????????? es ????????????
        // ?????? spu ??????????????? ==> key: attrId & value: attrEntity
        Map<Long, ProductAttrValueEntity> productAttrValueMap = productAttrValueService.getBySpuId(spuId).stream()
                .collect(Collectors.toMap(ProductAttrValueEntity::getAttrId, productAttrValueEntity -> productAttrValueEntity));
        List<AttrEntity> attrEntityList = attrService.listByIdsAndSearchType(productAttrValueMap.keySet());
        List<ProductMapping.Attr> attrs = attrEntityList.stream()
                .map(attrEntity -> {
                    ProductMapping.Attr attr = new ProductMapping.Attr();
                    BeanUtils.copyProperties(attrEntity, attr);
                    // ?????? attrId ??????????????? spu?????????????????????
                    attr.setAttrValue(productAttrValueMap.get(attr.getAttrId()).getAttrValue());
                    return attr;
                })
                .collect(Collectors.toList());

        // 3. ?????? spu ?????? brandName & catalogName
        String brandName = brandService.getBrandName(skuInfoEntityList.get(0).getBrandId());
        String catelogName = categoryService.getCatelogName(skuInfoEntityList.get(0).getCatalogId());

        // 4. ?????? sku ???????????????
        List<Long> skuIds = skuInfoEntityList.stream().map(SkuInfoEntity::getSkuId).collect(Collectors.toList());
        R r = wareFeignClient.stockInfo(skuIds);
        HashMap<Long, Boolean> stockInfo = r.getData(new TypeReference<HashMap<Long, Boolean>>() {
        });

        // ?????? skuInfo
        List<ProductMapping> productMappings = skuInfoEntityList.stream().map(skuInfoEntity -> {
            ProductMapping productMapping = new ProductMapping();
            productMapping.setAttrs(attrs);
            productMapping.setSpuId(spuId);
            // TODO: ?????????????????? 0
            productMapping.setHotScore(0L);
            BeanUtils.copyProperties(skuInfoEntity, productMapping);
            productMapping.setBrandImg(skuInfoEntity.getSkuDefaultImg());
            productMapping.setSkuPrice(skuInfoEntity.getPrice());
            productMapping.setBrandName(brandName);
            productMapping.setCatalogName(catelogName);
            productMapping.setHasStock(Optional.ofNullable(stockInfo.get(skuInfoEntity.getSkuId())).orElse(false));
            return productMapping;
        }).collect(Collectors.toList());

        // 3. ?????? spu ??????
        R productUpState = searchFeignClient.productUp(productMappings);
        if (productUpState.getCode() != 0) {
            throw new RuntimeException("???????????????????????????????????????????????????");
        }
        this.updateStatus(spuId, ProductConstant.StatusEnum.SPU_UP.getCode());
    }

    private void updateStatus(Long spuId, int code) {
        this.baseMapper.updateStatus(spuId, code);
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