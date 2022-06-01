package pers.prover.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import pers.prover.mall.common.utils.PageUtils;
import pers.prover.mall.product.entity.AttrEntity;
import pers.prover.mall.product.entity.AttrGroupEntity;
import pers.prover.mall.product.vo.AttrAttrgroupRelationReqVo;

import java.util.List;
import java.util.Map;

/**
 * 属性分组
 *
 * @author 小·木曾仪仲·哈牛柚子露·蛋卷
 * @email 2391105059@qq.com
 * @date 2022-05-29 15:29:02
 */
public interface AttrGroupService extends IService<AttrGroupEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 根据 catelogId 查询对应的属性
     * @param params
     * @param catelogId
     * @return
     */
    PageUtils queryPage(Map<String, Object> params, Long catelogId);

    /**
     * 根据 attrGroupId 获取对应的 attrGroupName
     * @param attrGroupId
     * @return
     */
    String getGroupName(Long attrGroupId);

    /**
     * 根据 attrGroupId  获取对应的 catelogId
     * @param attrGroupId
     * @return
     */
    Long getCatelogId(Long attrGroupId);

    /**
     * 获取 attrGroup 关联的 attr
     * @param attrGroupId
     * @return
     */
    List<AttrEntity> getAttrGroupRelationAttr(String attrGroupId);

    /**
     * 获取 attrGroup 在当前分类中没有关联的 attr
     * @param attrGroupId
     * @return
     */
    PageUtils getAttrGroupNoRelationAttr(Map<String, Object> params, String attrGroupId);

    /**
     * 保存顺序分组与分组的关系
     * @param attrAttrgroupRelationReqVos
     */
    void saveAttrRelation(List<AttrAttrgroupRelationReqVo> attrAttrgroupRelationReqVos);

    /**
     * 删除关联关系
     * @param attrAttrgroupRelationReqVos
     */
    void removeBatch(List<AttrAttrgroupRelationReqVo> attrAttrgroupRelationReqVos);
}

