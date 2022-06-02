package pers.prover.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import pers.prover.mall.common.utils.PageUtils;
import pers.prover.mall.product.entity.BrandEntity;
import pers.prover.mall.product.entity.CategoryBrandRelationEntity;

import java.util.List;
import java.util.Map;

/**
 * 品牌分类关联
 *
 * @author 小·木曾仪仲·哈牛柚子露·蛋卷
 * @email 2391105059@qq.com
 * @date 2022-05-31 19:48:37
 */
public interface CategoryBrandRelationService extends IService<CategoryBrandRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 根据 brandId 获取关联的分类列表
     * @param brandId
     * @return
     */
    List<CategoryBrandRelationEntity> getCatelogByBrandId(Long brandId);

    /**
     * 级联保存 分类与品牌 之间的关系
     * @param categoryBrandRelation
     */
    void saveCascade(CategoryBrandRelationEntity categoryBrandRelation);

    /**
     * 根据 brandId 修改 brandName
     * @param brandId
     * @param brandName
     */
    void updateBrandName(Long brandId, String brandName);

    /**
     * 根据 catId 修改 categoryName
     * @param catId
     * @param categoryName
     */
    void updateCategoryName(Long catId, String categoryName);

    /**
     * 获取分类关联的品牌
     * @param catId
     * @return
     */
    List<BrandEntity> getBrandListByCatelogId(Long catId);
}

