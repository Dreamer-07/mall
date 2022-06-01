package pers.prover.mall.product.dao;

import org.apache.ibatis.annotations.Param;
import pers.prover.mall.product.entity.AttrAttrgroupRelationEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import pers.prover.mall.product.vo.AttrAttrgroupRelationReqVo;

import java.util.List;

/**
 * 属性&属性分组关联
 * 
 * @author 小·木曾仪仲·哈牛柚子露·蛋卷
 * @email 2391105059@qq.com
 * @date 2022-05-29 15:29:02
 */
@Mapper
public interface AttrAttrgroupRelationDao extends BaseMapper<AttrAttrgroupRelationEntity> {

    void updateAttrGroupId(@Param("attrId") Long attrId,@Param("attrGroupId") Long attrGroupId);
}
