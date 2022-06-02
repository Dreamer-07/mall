package pers.prover.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import pers.prover.mall.common.utils.PageUtils;
import pers.prover.mall.common.utils.Query;

import pers.prover.mall.product.dao.CategoryBrandRelationDao;
import pers.prover.mall.product.entity.BrandEntity;
import pers.prover.mall.product.entity.CategoryBrandRelationEntity;
import pers.prover.mall.product.service.BrandService;
import pers.prover.mall.product.service.CategoryBrandRelationService;
import pers.prover.mall.product.service.CategoryService;


@Service("categoryBrandRelationService")
public class CategoryBrandRelationServiceImpl extends ServiceImpl<CategoryBrandRelationDao, CategoryBrandRelationEntity> implements CategoryBrandRelationService {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BrandService brandService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryBrandRelationEntity> page = this.page(
                new Query<CategoryBrandRelationEntity>().getPage(params),
                new QueryWrapper<CategoryBrandRelationEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryBrandRelationEntity> getCatelogByBrandId(Long brandId) {
        LambdaQueryWrapper<CategoryBrandRelationEntity> lqw = new LambdaQueryWrapper<>();
        lqw.eq(CategoryBrandRelationEntity::getBrandId, brandId);
        return this.list(lqw);
    }

    @Override
    public void saveCascade(CategoryBrandRelationEntity categoryBrandRelation) {
        String categoryName = categoryService.getById(categoryBrandRelation.getCatelogId()).getName();
        String brandName = brandService.getById(categoryBrandRelation.getBrandId()).getName();
        categoryBrandRelation.setCatelogName(categoryName);
        categoryBrandRelation.setBrandName(brandName);
        this.save(categoryBrandRelation);
    }

    @Override
    public void updateBrandName(Long brandId, String brandName) {
        this.baseMapper.updateBrandName(brandId, brandName);
    }

    @Override
    public void updateCategoryName(Long catId, String categoryName) {
        this.baseMapper.updateCategoryName(catId, categoryName);
    }

    @Override
    public List<BrandEntity> getBrandListByCatelogId(Long catId) {
        List<BrandEntity> brandEntities = null;

        // 获取关联信息
        LambdaQueryWrapper<CategoryBrandRelationEntity> selectBrandIdsLqw = new LambdaQueryWrapper<CategoryBrandRelationEntity>()
                .select(CategoryBrandRelationEntity::getBrandId)
                .eq(CategoryBrandRelationEntity::getCatelogId, catId);
        List<CategoryBrandRelationEntity> categoryBrandRelationEntities = this.list(selectBrandIdsLqw);

        if (categoryBrandRelationEntities != null && categoryBrandRelationEntities.size() > 0) {
            // 获取 brandIds
            List<Long> brandIds = categoryBrandRelationEntities.stream().map(CategoryBrandRelationEntity::getBrandId).collect(Collectors.toList());
            // 根据 brandid 获取 brand info
            brandEntities = brandService.listByIds(brandIds);
        }

        return brandEntities;
    }

}