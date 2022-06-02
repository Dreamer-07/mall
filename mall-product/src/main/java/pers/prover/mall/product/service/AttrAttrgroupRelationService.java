package pers.prover.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import pers.prover.mall.common.utils.PageUtils;
import pers.prover.mall.product.entity.AttrAttrgroupRelationEntity;
import pers.prover.mall.product.vo.AttrAttrgroupRelationReqVo;

import java.util.List;
import java.util.Map;

/**
 * 属性&属性分组关联
 *
 * @author 小·木曾仪仲·哈牛柚子露·蛋卷
 * @email 2391105059@qq.com
 * @date 2022-05-29 15:29:02
 */
public interface AttrAttrgroupRelationService extends IService<AttrAttrgroupRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 为 属性 和 属性分组 进行关联
     * @param attrId
     * @param attrGroupId
     */
    void save(Long attrId, Long attrGroupId);

    /**
     * 根据 attrId 获取 attrGroupId
     * @param attrId
     * @return
     */
    Long getAttrGroupId(Long attrId);

    /**
     * 修改 attr 和 attrGroup 的关联关系
     * @param attrId
     * @param attrGroupId
     */
    void update(Long attrId, Long attrGroupId);

    /**
     * 获取被关联的属性
     * @param attrGroupIds
     * @return
     */
    List<Long> getAttrIds(List<Long> attrGroupIds);

    /**
     * 批量保存
     * @param attrAttrgroupRelationReqVos
     */
    void saveBatch(List<AttrAttrgroupRelationReqVo> attrAttrgroupRelationReqVos);

    /**
     * 获取被关联的属性
     * @param attrGroupId
     * @return
     */
    List<Long> getAttrIds(Long attrGroupId);
}

