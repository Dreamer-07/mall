package pers.prover.mall.product.service.impl;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import pers.prover.mall.common.utils.PageUtils;
import pers.prover.mall.common.utils.Query;

import pers.prover.mall.product.dao.CategoryDao;
import pers.prover.mall.product.entity.CategoryEntity;
import pers.prover.mall.product.service.CategoryService;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

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