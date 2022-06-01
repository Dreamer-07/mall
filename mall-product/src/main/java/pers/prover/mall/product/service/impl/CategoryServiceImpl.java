package pers.prover.mall.product.service.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;
import pers.prover.mall.common.utils.PageUtils;
import pers.prover.mall.common.utils.Query;

import pers.prover.mall.product.dao.CategoryDao;
import pers.prover.mall.product.entity.CategoryEntity;
import pers.prover.mall.product.service.CategoryBrandRelationService;
import pers.prover.mall.product.service.CategoryService;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Autowired
    private CategoryBrandRelationService categoryBrandRelationService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> treeList() {
        List<CategoryEntity> categoryEntityList = this.list();
        return covertTreeList(categoryEntityList, 0);
    }

    @Override
    public void removeByIds(Long[] ids) {
        // TODO 判断数据是否被引用
        baseMapper.deleteBatchIds(Arrays.asList(ids));
    }

    @Override
    public List<Long> getCatelogPath(Long catelogId) {
        CategoryEntity categoryEntity = this.getById(catelogId);
        List<Long> categoryPath = new ArrayList<>();
        getCategoryParentPath(categoryEntity, categoryPath);
        return categoryPath;
    }

    @Override
    @Transactional
    public void updateCascade(CategoryEntity category) {
        if (!StringUtils.isBlank(category.getName())) {
            categoryBrandRelationService.updateCategoryName(category.getCatId(), category.getName());
        }
        this.updateById(category);
    }

    @Override
    public String getCatelogPathStr(Long catelogId) {
        CategoryEntity categoryEntity = this.getById(catelogId);
        List<String> categoryPath = new ArrayList<>();
        getCategoryParentPathStr(categoryEntity, categoryPath);
        return String.join( "/", categoryPath);
    }

    /**
     * 获取分类的父id路径
     * @param categoryEntity
     * @param categoryPath
     */
    private void getCategoryParentPath(CategoryEntity categoryEntity, List<Long> categoryPath) {
        if (categoryEntity.getParentCid() != 0) {
            getCategoryParentPath(this.getById(categoryEntity.getParentCid()), categoryPath);
        }
        categoryPath.add(categoryEntity.getCatId());
    }

    /**
     * 获取分类的父分类的名
     * @param categoryEntity
     */
    private void getCategoryParentPathStr(CategoryEntity categoryEntity, List<String> categoryPath) {
        if (categoryEntity.getParentCid() != 0) {
            getCategoryParentPathStr(this.getById(categoryEntity.getParentCid()), categoryPath);
        }
        categoryPath.add(categoryEntity.getName());
    }

    private List<CategoryEntity> covertTreeList(List<CategoryEntity> categoryEntityList, long parentId) {
        return categoryEntityList.stream()
                .filter(categoryEntity -> categoryEntity.getParentCid() == parentId)
                .peek(categoryEntity -> {
                    categoryEntity.setChildren(covertTreeList(categoryEntityList, categoryEntity.getCatId()));
                })
                .sorted(Comparator.comparingInt(c -> (c.getSort() == null ? 0 : c.getSort())))
                .collect(Collectors.toList());
    }

}