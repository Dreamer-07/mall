package pers.prover.mall.cart.service;

import pers.prover.mall.cart.vo.CartItemVo;

import java.util.List;

public interface CartService {
    /**
     * 获取购物车列表信息
     * @return
     */
    List<CartItemVo> getCartInfo();

    /**
     * 添加购物车信息
     * @param skuId
     * @param count
     * @return
     */
    CartItemVo addToCart(Long skuId, Integer count);

    /**
     * 修改购物车中的商品个数
     * @param skuId
     * @param count
     */
    void updateCartItemCount(Long skuId, Integer count);

    /**
     * 修改购物车中商品的选中状态
     * @param skuId
     * @param checked
     */
    void updateCartItemChecked(Long skuId, Boolean checked);

    /**
     * 删除购物车中的商品
     * @param skuId
     */
    void deleteCartItem(Long skuId);
}
