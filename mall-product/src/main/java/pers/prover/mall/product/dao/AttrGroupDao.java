package pers.prover.mall.product.dao;

import org.apache.ibatis.annotations.Param;
import pers.prover.mall.product.entity.AttrGroupEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import pers.prover.mall.product.service.AttrAttrgroupRelationService;
import pers.prover.mall.product.vo.AttrAttrgroupRelationReqVo;

import java.util.List;

/**
 * 属性分组
 * 
 * @author 小·木曾仪仲·哈牛柚子露·蛋卷
 * @email 2391105059@qq.com
 * @date 2022-05-29 15:29:02
 */
@Mapper
public interface AttrGroupDao extends BaseMapper<AttrGroupEntity> {

    void deleteBatch(@Param("list") List<AttrAttrgroupRelationReqVo> attrAttrgroupRelationReqVos);
}
