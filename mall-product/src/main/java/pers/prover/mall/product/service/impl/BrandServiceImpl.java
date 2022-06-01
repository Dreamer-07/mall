package pers.prover.mall.product.service.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;
import pers.prover.mall.common.utils.PageUtils;
import pers.prover.mall.common.utils.Query;

import pers.prover.mall.product.dao.BrandDao;
import pers.prover.mall.product.entity.BrandEntity;
import pers.prover.mall.product.entity.CategoryBrandRelationEntity;
import pers.prover.mall.product.service.BrandService;
import pers.prover.mall.product.service.CategoryBrandRelationService;


@Service("brandService")
public class BrandServiceImpl extends ServiceImpl<BrandDao, BrandEntity> implements BrandService {

    @Autowired
    private CategoryBrandRelationService categoryBrandRelationService;

    @Override

    public PageUtils queryPage(Map<String, Object> params) {
        IPage<BrandEntity> page = this.page(
                new Query<BrandEntity>().getPage(params),
                new QueryWrapper<BrandEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    @Transactional
    public void updateCascade(BrandEntity brand) {
        if (!StringUtils.isBlank(brand.getName())) {
            categoryBrandRelationService.updateBrandName(brand.getBrandId(), brand.getName());
        }
        this.updateById(brand);
    }

}