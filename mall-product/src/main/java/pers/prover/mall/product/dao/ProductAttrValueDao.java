package pers.prover.mall.product.dao;

import org.apache.ibatis.annotations.Param;
import pers.prover.mall.product.entity.ProductAttrValueEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import pers.prover.mall.product.vo.api.SkuItemVo;

import java.util.List;

/**
 * spu属性值
 * 
 * @author 小·木曾仪仲·哈牛柚子露·蛋卷
 * @email 2391105059@qq.com
 * @date 2022-05-29 15:29:02
 */
@Mapper
public interface ProductAttrValueDao extends BaseMapper<ProductAttrValueEntity> {

    List<SkuItemVo.SpuBaseAttrVo> baseAttrList(@Param("catalogId") Long catalogId, @Param("spuId") Long spuId);
}
