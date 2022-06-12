package pers.prover.mall.cart.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;

/**
 * 购物车数据实体类
 *
 * @author 小丶木曾义仲丶哈牛柚子露丶蛋卷
 * @version 1.0
 * @date 2022/6/11 18:46
 */
@Data
public class CartVo {

    /**
     * 商品信息
     */
    private List<CartItemVo> cartItemVoList;

    /**
     * 优惠价格
     */
    private BigDecimal reduce = BigDecimal.ZERO;

    /**
     * 商品类型个数
     */
    public Integer getItemTypeCount() {
        return this.cartItemVoList.size();
    }

    /**
     * 已选择要购买的商品个数
     *
     * @return
     */
    public Integer getTotalCount() {
        return this.cartItemVoList.stream().reduce(0, (totalCount, cartItemVo) -> {
            totalCount += cartItemVo.getChecked() ? cartItemVo.getCount() : 0;
            return totalCount;
        }, (integer, integer2) -> null);
    }

    /**
     * 已选择的要购买的商品总结
     *
     * @return
     */
    public BigDecimal getTotalPrice() {
        return this.cartItemVoList.stream().reduce(
                BigDecimal.ZERO,
                (totalPrice, cartItemVo) -> totalPrice.add(cartItemVo.getChecked() ? cartItemVo.getTotalPrice() : BigDecimal.ZERO),
                (bigDecimal, bigDecimal2) -> null
        ).subtract(this.reduce);
    }

}
