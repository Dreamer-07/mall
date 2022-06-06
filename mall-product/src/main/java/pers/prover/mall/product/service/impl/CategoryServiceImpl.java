package pers.prover.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jdk.nashorn.internal.ir.IfNode;
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
import pers.prover.mall.product.vo.api.CategoryLevel2RespVo;


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
        return String.join("/", categoryPath);
    }

    @Override
    public String getCatelogName(Long catalogId) {
        LambdaQueryWrapper<CategoryEntity> selectLqw = new LambdaQueryWrapper<CategoryEntity>()
                .select(CategoryEntity::getName)
                .eq(CategoryEntity::getCatId, catalogId);
        return this.getOne(selectLqw).getName();
    }

    @Override
    public List<CategoryEntity> listByParentId(Long parentId) {
        LambdaQueryWrapper<CategoryEntity> selectLqw = new LambdaQueryWrapper<>();
        selectLqw.eq(CategoryEntity::getParentCid, parentId);
        return this.list(selectLqw);
    }

    @Override
    public Map<Long, List<CategoryLevel2RespVo>> listMapByParentId() {
        // 获取所有分类信息
        List<CategoryEntity> categoryEntities = this.list();

        // 获取所有的一级分类 id 信息
        return categoryEntities.stream()
                .filter(categoryEntity -> categoryEntity.getParentCid() == 0)
                .map(CategoryEntity::getCatId)
                .collect(Collectors.toMap(catId -> catId, catId -> {
                    // 获取对应的二级分类
                    List<CategoryEntity> categoryLevel2List = covertTreeList(categoryEntities, catId);

                    // 将二级分类的结构进行转换
                    return categoryLevel2List.stream().map(categoryLevel2Entity -> {

                        CategoryLevel2RespVo categoryLevel2RespVo = new CategoryLevel2RespVo();
                        categoryLevel2RespVo.setId(categoryLevel2Entity.getCatId().toString());
                        categoryLevel2RespVo.setName(categoryLevel2Entity.getName());
                        categoryLevel2RespVo.setCatalog1Id(catId);

                        // 获取二级分类标识
                        Long catLevel2Id = categoryLevel2Entity.getCatId();

                        // 将三级分类进行转换
                        List<CategoryLevel2RespVo.CategoryLevel3RespVo> categoryLevel3RespVos = categoryLevel2Entity.getChildren().stream().map(categoryLevel3Entity -> {
                            CategoryLevel2RespVo.CategoryLevel3RespVo categoryLevel3RespVo = new CategoryLevel2RespVo.CategoryLevel3RespVo();
                            categoryLevel3RespVo.setCatalog2Id(catLevel2Id);
                            categoryLevel3RespVo.setId(categoryLevel3Entity.getCatId().toString());
                            categoryLevel3RespVo.setName(categoryLevel3Entity.getName());
                            return categoryLevel3RespVo;
                        }).collect(Collectors.toList());
                        categoryLevel2RespVo.setCatalog3List(categoryLevel3RespVos);

                        return categoryLevel2RespVo;
                    }).collect(Collectors.toList());
                }));
    }

    /**
     * 获取分类的父id路径
     *
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
     *
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