package pers.prover.mall.cart.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import pers.prover.mall.cart.service.CartService;
import pers.prover.mall.cart.vo.CartItemVo;
import pers.prover.mall.cart.vo.CartVo;
import pers.prover.mall.common.utils.R;

import java.util.List;

/**
 * @author 小丶木曾义仲丶哈牛柚子露丶蛋卷
 * @version 1.0
 * @date 2022/6/11 16:44
 */
@RequestMapping("/api/cart")
@RestController
public class CartApiController {

    @Autowired
    private CartService cartService;


    /**
     * 获取购物车信息
     * @return
     */
    @GetMapping
    public R getCartInfo() {
        List<CartItemVo> cartItemVoList = cartService.getCartInfo();
        CartVo cartVo = new CartVo();
        cartVo.setCartItemVoList(cartItemVoList);
        return R.ok().put("data", cartVo);
    }

    /**
     * 添加商品到购物车中
     * @param skuId
     * @param count
     * @return
     */
    @PostMapping("/item/{skuId}/{count}")
    public R addCartItem(@PathVariable("skuId") Long skuId, @PathVariable("count") Integer count) {
        CartItemVo cartItemVo = cartService.addToCart(skuId, count);
        return R.ok().put("data", cartItemVo);
    }


    @PutMapping("/item/{skuId}/count/{count}")
    public R updateCartItemCount(@PathVariable Long skuId, @PathVariable Integer count) {
        cartService.updateCartItemCount(skuId, count);
        return R.ok();
    }

    @PutMapping("/item/{skuId}/checked/{checked}")
    public R updateCartItemChecked(@PathVariable Long skuId, @PathVariable Integer checked) {
        // checked = 0 = false; checked = 1 = true
        cartService.updateCartItemChecked(skuId, checked != 0);
        return R.ok();
    }

    @DeleteMapping("/item/{skuId}")
    public R deleteCartItem(@PathVariable Long skuId) {
        cartService.deleteCartItem(skuId);
        return R.ok();
    }

}
