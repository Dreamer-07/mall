package pers.prover.mall.product.to;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author 小丶木曾义仲丶哈牛柚子露丶蛋卷
 * @version 1.0
 * @date 2022/6/12 10:34
 */
@Data
public class RespSkuInfoCartTo {

    /**
     * 商品的标题
     */
    private String skuTitle;

    /**
     * sku 商品的图片
     */
    private String skuImgUrl;

    /**
     * sku 商品的销售属性
     *  属性名:属性值[;属性名:属性值]
     */
    private String skuSaleAttr;

    /**
     * 单个商品的价格
     */
    private BigDecimal price;

}
