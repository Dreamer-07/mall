package pers.prover.mall.product.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import pers.prover.mall.common.utils.PageUtils;
import pers.prover.mall.product.entity.AttrEntity;
import pers.prover.mall.product.entity.AttrGroupEntity;
import pers.prover.mall.product.entity.ProductAttrValueEntity;
import pers.prover.mall.product.vo.Attr;
import pers.prover.mall.product.vo.AttrReqVo;
import pers.prover.mall.product.vo.AttrRespVo;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 商品属性
 *
 * @author 小·木曾仪仲·哈牛柚子露·蛋卷
 * @email 2391105059@qq.com
 * @date 2022-05-29 15:29:02
 */
public interface AttrService extends IService<AttrEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 保存 Attr
     * @param attrReqVo
     */
    void save(AttrReqVo attrReqVo);


    /**
     * 查找规格参数列表分页
     * @param params
     * @return
     */
    PageUtils queryBaseListPage(Map<String, Object> params, Long catelogId);

    /**
     * 获取 attr detail info 在 admin(管理系统) 中
     * @param attrId
     * @return
     */
    AttrRespVo getDetailInAdmin(Long attrId);

    /**
     * 修改 Attr
     * @param attrReqVo
     */
    void updateById(AttrReqVo attrReqVo);

    /**
     * 获取分类中不在 attrIds 中的属性
     * @param attrIds
     * @param catelogId
     * @return
     */
    PageUtils pageByNotInIds(Map<String, Object> params, List<Long> attrIds, Long catelogId);

    /**
     * 查找销售属性列表分页
     * @param params
     * @return
     */
    PageUtils querySaleListPage(Map<String, Object> params, Long catelogId);

    /**
     * 查找 spu 的规格参数
     * @param spuId
     * @return
     */
    List<ProductAttrValueEntity> getBaseListBySpuId(Long spuId);

    /**
     * 根据 spuId 修改对应的销售属性
     * @param spuId
     * @param productAttrValueEntityList
     */
    void updateBaseAttr(Long spuId, List<ProductAttrValueEntity> productAttrValueEntityList);

    /**
     * 查找属性详情(且可以被检索)
     * @param attrIds
     * @return
     */
    List<AttrEntity> listByIdsAndSearchType(Collection<Long> attrIds);
}

