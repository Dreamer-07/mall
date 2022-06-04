package pers.prover.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import pers.prover.mall.common.utils.PageUtils;
import pers.prover.mall.product.entity.ProductAttrValueEntity;
import pers.prover.mall.product.vo.SpuSaveVo;

import java.util.List;
import java.util.Map;

/**
 * spu属性值
 *
 * @author 小·木曾仪仲·哈牛柚子露·蛋卷
 * @email 2391105059@qq.com
 * @date 2022-05-29 15:29:02
 */
public interface ProductAttrValueService extends IService<ProductAttrValueEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void save(Long spuInfoId, List<SpuSaveVo.BaseAttrs> baseAttrs);

    List<ProductAttrValueEntity> getBySpuId(Long spuId);

    void updateAttr(Long spuId, List<ProductAttrValueEntity> productAttrValueEntityList);

}

