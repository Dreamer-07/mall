package pers.prover.mall.cart.to;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 用来获取购物车中的 sku 信息
 * @author 小丶木曾义仲丶哈牛柚子露丶蛋卷
 * @version 1.0
 * @date 2022/6/11 18:31
 */
@Data
public class ReqCartItemSkuTo {

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
