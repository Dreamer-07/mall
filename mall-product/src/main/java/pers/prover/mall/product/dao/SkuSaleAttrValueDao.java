package pers.prover.mall.product.dao;

import pers.prover.mall.product.entity.SkuSaleAttrValueEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import pers.prover.mall.product.vo.api.SkuItemVo;

import java.util.List;

/**
 * sku销售属性&值
 * 
 * @author 小·木曾仪仲·哈牛柚子露·蛋卷
 * @email 2391105059@qq.com
 * @date 2022-05-29 15:29:02
 */
@Mapper
public interface SkuSaleAttrValueDao extends BaseMapper<SkuSaleAttrValueEntity> {

    List<SkuItemVo.SpuSaleAttrVo> saleAttrList(Long spuId);
}
