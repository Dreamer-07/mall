package pers.prover.mall.product.service.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import pers.prover.mall.common.utils.PageUtils;
import pers.prover.mall.common.utils.Query;

import pers.prover.mall.product.dao.SkuImagesDao;
import pers.prover.mall.product.entity.SkuImagesEntity;
import pers.prover.mall.product.service.SkuImagesService;
import pers.prover.mall.product.vo.SpuSaveVo;


@Service("skuImagesService")
public class SkuImagesServiceImpl extends ServiceImpl<SkuImagesDao, SkuImagesEntity> implements SkuImagesService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuImagesEntity> page = this.page(
                new Query<SkuImagesEntity>().getPage(params),
                new QueryWrapper<SkuImagesEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void save(Long skuId, List<SpuSaveVo.Skus.Images> images) {
        List<SkuImagesEntity> skuImagesEntities = images.stream().filter(image -> !StringUtils.isBlank(image.getImgUrl())).map(image -> {
            SkuImagesEntity skuImagesEntity = new SkuImagesEntity();
            BeanUtils.copyProperties(image, skuImagesEntity);
            skuImagesEntity.setSkuId(skuId);
            return skuImagesEntity;
        }).collect(Collectors.toList());
        this.saveBatch(skuImagesEntities);
    }

}