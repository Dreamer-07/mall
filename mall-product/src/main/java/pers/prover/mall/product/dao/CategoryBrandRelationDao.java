package pers.prover.mall.product.dao;

import org.apache.ibatis.annotations.Param;
import pers.prover.mall.product.entity.CategoryBrandRelationEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 品牌分类关联
 * 
 * @author 小·木曾仪仲·哈牛柚子露·蛋卷
 * @email 2391105059@qq.com
 * @date 2022-05-31 19:48:37
 */
@Mapper
public interface CategoryBrandRelationDao extends BaseMapper<CategoryBrandRelationEntity> {

    void updateBrandName(@Param("brandId") Long brandId,@Param("brandName") String brandName);

    /**
     *
     * @param catId
     * @param categoryName
     */
    void updateCategoryName(@Param("catId")Long catId,@Param("categoryName") String categoryName);
}
