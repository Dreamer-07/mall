package pers.prover.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import pers.prover.mall.common.utils.PageUtils;
import pers.prover.mall.common.utils.Query;

import pers.prover.mall.product.dao.SkuInfoDao;
import pers.prover.mall.product.entity.SkuInfoEntity;
import pers.prover.mall.product.entity.SpuInfoEntity;
import pers.prover.mall.product.service.SkuInfoService;
import pers.prover.mall.product.vo.SpuSaveVo;


@Service("skuInfoService")
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoDao, SkuInfoEntity> implements SkuInfoService {

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