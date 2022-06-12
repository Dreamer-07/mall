package pers.prover.mall.cart.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 购物车中具体商品的数据实体类
 * @author 小丶木曾义仲丶哈牛柚子露丶蛋卷
 * @version 1.0
 * @date 2022/6/11 16:48
 */
@Data
public class CartItemVo {

    /**
     * sku 商品
     */
    private Long skuId;

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
     * 是否为选中
     */
    private Boolean checked;

    /**
     * 单个商品的价格
     */
    private BigDecimal price;

    /**
     * 购买的数量
     */
    private Integer count;

    /**
     * 该商品的总价格
     * @return
     */
    public BigDecimal getTotalPrice() {
        return price.subtract(BigDecimal.valueOf(count));
    }

}
