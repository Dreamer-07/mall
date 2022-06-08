package pers.prover.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import pers.prover.mall.common.utils.PageUtils;
import pers.prover.mall.product.entity.SkuSaleAttrValueEntity;
import pers.prover.mall.product.vo.SpuSaveVo;
import pers.prover.mall.product.vo.api.SkuItemVo;

import java.util.List;
import java.util.Map;

/**
 * sku销售属性&值
 *
 * @author 小·木曾仪仲·哈牛柚子露·蛋卷
 * @email 2391105059@qq.com
 * @date 2022-05-29 15:29:02
 */
public interface SkuSaleAttrValueService extends IService<SkuSaleAttrValueEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void save(Long skuId, List<SpuSaveVo.Skus.Attr> attr);

    /**
     * 获取 spu 销售属性的组合
     * @param spuId
     * @return
     */
    List<SkuItemVo.SpuSaleAttrVo> saleAttrList(Long spuId);
}

