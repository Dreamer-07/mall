package pers.prover.mall.product.service.impl;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import pers.prover.mall.common.utils.PageUtils;
import pers.prover.mall.common.utils.Query;

import pers.prover.mall.product.dao.SpuImagesDao;
import pers.prover.mall.product.entity.SpuImagesEntity;
import pers.prover.mall.product.service.SpuImagesService;


@Service("spuImagesService")
public class SpuImagesServiceImpl extends ServiceImpl<SpuImagesDao, SpuImagesEntity> implements SpuImagesService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuImagesEntity> page = this.page(
                new Query<SpuImagesEntity>().getPage(params),
                new QueryWrapper<SpuImagesEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void save(Long spuInfoId, List<String> images) {
        List<SpuImagesEntity> spuImagesEntities = new ArrayList<>();
        for (int i = 0; i < images.size(); i++) {
            SpuImagesEntity spuImagesEntity = new SpuImagesEntity();
            spuImagesEntity.setSpuId(spuInfoId);
            spuImagesEntity.setImgSort(i);
            spuImagesEntity.setImgName(images.get(i));
            spuImagesEntity.setImgUrl(images.get(i));
            spuImagesEntities.add(spuImagesEntity);
        }
        this.saveBatch(spuImagesEntities);
    }

}