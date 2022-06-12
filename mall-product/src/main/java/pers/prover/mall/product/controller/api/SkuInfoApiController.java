package pers.prover.mall.product.controller.api;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pers.prover.mall.common.utils.R;
import pers.prover.mall.product.entity.SkuInfoEntity;
import pers.prover.mall.product.entity.SkuSaleAttrValueEntity;
import pers.prover.mall.product.service.SkuInfoService;
import pers.prover.mall.product.service.SkuSaleAttrValueService;
import pers.prover.mall.product.to.RespSkuInfoCartTo;

import java.util.List;
import java.util.function.BiFunction;

/**
 * @author 小丶木曾义仲丶哈牛柚子露丶蛋卷
 * @version 1.0
 * @date 2022/6/12 10:32
 */
@RestController
@RequestMapping("/api/product/skuInfo")
public class SkuInfoApiController {

    @Autowired
    private SkuInfoService skuInfoService;

    @Autowired
    private SkuSaleAttrValueService skuSaleAttrValueService;

    /**
     * 返回商品的购物车数据
     * @param skuId
     * @return
     */
    @GetMapping("/inner/cart/{skuId}")
    public R getSkuCartItem(@PathVariable("skuId") Long skuId) {
        // 获取 sku 商品的基本属性
        SkuInfoEntity skuInfoEntity = skuInfoService.getById(skuId);
        RespSkuInfoCartTo respSkuInfoCartTo = new RespSkuInfoCartTo();
        BeanUtils.copyProperties(skuInfoEntity, respSkuInfoCartTo);
        respSkuInfoCartTo.setSkuImgUrl(skuInfoEntity.getSkuDefaultImg());
        // 获取 sku 商品的销售属性
        List<SkuSaleAttrValueEntity> skuInfoEntityList = skuSaleAttrValueService.listBySkuId(skuId);
        // 将 sku 销售属性转换为指定的字符串(attrName:attrValue[;attrName:attrValue])
        StringBuffer skuSaleAttrSb = skuInfoEntityList.stream().reduce(new StringBuffer(), (skuSaleAttrStr, skuSaleAttr) -> {
            skuSaleAttrStr
                    .append(skuSaleAttr.getAttrName())
                    .append(":")
                    .append(skuSaleAttr.getAttrValue())
                    .append(";");
            return skuSaleAttrStr;
        }, (s1, s2) -> null);
        respSkuInfoCartTo.setSkuSaleAttr(skuSaleAttrSb.substring(0, skuSaleAttrSb.length() - 1));
        return R.ok().put("data", respSkuInfoCartTo);
    }

}
