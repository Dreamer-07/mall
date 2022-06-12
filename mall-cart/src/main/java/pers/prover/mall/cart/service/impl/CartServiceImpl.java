package pers.prover.mall.cart.service.impl;

import com.alibaba.fastjson.TypeReference;
import org.apache.catalina.startup.UserConfig;
import org.apache.commons.lang.StringUtils;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import pers.prover.mall.cart.bo.UserKeyBo;
import pers.prover.mall.cart.client.ProductFeignClient;
import pers.prover.mall.cart.constant.RedisKeyConstant;
import pers.prover.mall.cart.interceptor.UserKeyInterceptor;
import pers.prover.mall.cart.service.CartService;
import pers.prover.mall.cart.to.ReqCartItemSkuTo;
import pers.prover.mall.cart.vo.CartItemVo;
import pers.prover.mall.common.utils.R;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 小丶木曾义仲丶哈牛柚子露丶蛋卷
 * @version 1.0
 * @date 2022/6/11 18:17
 */
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private ProductFeignClient productFeignClient;

    @Override
    public List<CartItemVo> getCartInfo() {
        // 获取用户在 redis 中保存的购物车数据
        BoundHashOperations<String, String, CartItemVo> userCartItemHashOps = getUserBoundHashOps();
        return Optional.ofNullable(userCartItemHashOps.values()).orElse(new ArrayList<>())
                .stream()
                .map(obj -> (CartItemVo) obj)
                .collect(Collectors.toList());
    }

    @Override
    public CartItemVo addToCart(Long skuId, Integer count) {
        // 转移 user-key 的数据
        this.transferUserKeyCart();
        // 保存购物车商品的基本数据
        CartItemVo cartItemVo = new CartItemVo();
        cartItemVo.setSkuId(skuId);
        cartItemVo.setCount(count);
        BoundHashOperations<String, String, CartItemVo> userBoundHashOps = getUserBoundHashOps();
        // 添加购物车商品
        cartItemVo = addCartItem(userBoundHashOps, Collections.singletonList(cartItemVo), true).get(0);
        return cartItemVo;
    }

    @Override
    public void updateCartItemCount(Long skuId, Integer count) {
        BoundHashOperations<String, String, CartItemVo> userBoundHashOps = getUserBoundHashOps();
        CartItemVo cartItemVo = userBoundHashOps.get(skuId.toString());
        if (cartItemVo == null) {
            throw new RuntimeException("购物车商品不存在!!");
        }
        cartItemVo.setCount(count);
        userBoundHashOps.put(skuId.toString(), cartItemVo);
    }

    @Override
    public void updateCartItemChecked(Long skuId, Boolean checked) {
        BoundHashOperations<String, String, CartItemVo> userBoundHashOps = getUserBoundHashOps();
        CartItemVo cartItemVo = userBoundHashOps.get(skuId.toString());
        if (cartItemVo == null) {
            throw new RuntimeException("购物车商品不存在!!");
        }
        cartItemVo.setChecked(checked);
        userBoundHashOps.put(skuId.toString(), cartItemVo);
    }

    @Override
    public void deleteCartItem(Long skuId) {
        BoundHashOperations<String, String, CartItemVo> userBoundHashOps = getUserBoundHashOps();
        CartItemVo cartItemVo = userBoundHashOps.get(skuId.toString());
        if (cartItemVo == null) {
            throw new RuntimeException("购物车商品不存在!!");
        }
        userBoundHashOps.delete(skuId.toString());
    }

    /**
     * 从 TheadLocal 获取用户信息，并获取操作其对应的 购物车(Hash) 的 Operation
     * @return
     */
    private BoundHashOperations<String, String, CartItemVo> getUserBoundHashOps() {
        // 获取用户信息
        UserKeyBo userKeyBo = UserKeyInterceptor.userKeyBoThreadLocal.get();
        // 获取用户的身份标识(有 id 有 id，无则用 key)
        String userKey = Optional.ofNullable(userKeyBo.getUserId()).orElse(userKeyBo.getUserKey());
        // 获取操作用户购物车的 Operations
        return redisTemplate.boundHashOps(RedisKeyConstant.USER_CART + userKey);
    }

    /**
     * 转移 userKey(临时用户) 的购物车数据
     */
    private void transferUserKeyCart() {
        UserKeyBo userKeyBo = UserKeyInterceptor.userKeyBoThreadLocal.get();
        String userId = userKeyBo.getUserId();
        String userKey = userKeyBo.getUserKey();
        if (!StringUtils.isBlank(userId)) {
            // 获取操作 user-key 用户的购物车 Operations
            BoundHashOperations<String, Long, CartItemVo> userKeyBoundHashOperations = redisTemplate.boundHashOps(RedisKeyConstant.USER_CART + userKey);
            List<CartItemVo> cartItemVos = userKeyBoundHashOperations.values();
            // 如果 user-key 的购物车数据大于 0 才需要转移数据
            if (cartItemVos != null && cartItemVos.size() > 0) {
                // 获取操作 user-id(登录用户) 购物车的  Operations
                BoundHashOperations<String, String, CartItemVo> userIdHashOperations = redisTemplate.boundHashOps(RedisKeyConstant.USER_CART + userId);
                this.addCartItem(userIdHashOperations, cartItemVos, false);
                // 清除临时购物车的数据
                redisTemplate.delete(RedisKeyConstant.USER_CART + userKey);
            }
        }
    }

    /**
     * 添加购物车数据项
     *
     * @param bho 需要添加数据的购物车 Operations
     * @param cartItemVos 要添加到购物车的商品集合
     * @param shouldFeign 是否需要通过远程调用获取 cartItem 数据
     */
    private List<CartItemVo> addCartItem(BoundHashOperations<String, String, CartItemVo> bho, List<CartItemVo> cartItemVos, boolean shouldFeign) {
        List<CartItemVo> result = new ArrayList<>();
        for (CartItemVo cartItemVo : cartItemVos) {
            // 判断当前 Operations 中是否存在对应的数据
            CartItemVo cachedCartItemVo = bho.get(cartItemVo.getSkuId().toString());
            if (cachedCartItemVo != null) {
                // 如果数据存在，那么修改数量即可
                cachedCartItemVo.setCount(cachedCartItemVo.getCount() + cartItemVo.getCount());
            } else {
                if (shouldFeign) {
                    // 远程调用获取 sku 信息
                    R getSkuCartItemRes = productFeignClient.getSkuCartItem(cartItemVo.getSkuId());
                    if (getSkuCartItemRes.getCode() != 0) {
                        throw new RuntimeException(String.format("获取商品信息失败(商品标识: %s)", cartItemVo.getSkuId().toString()));
                    }
                    ReqCartItemSkuTo cartItemSkuTo = getSkuCartItemRes.getData(new TypeReference<ReqCartItemSkuTo>() {
                    });
                    // 将 sku 信息封装到 cart 信息中
                    BeanUtils.copyProperties(cartItemSkuTo, cartItemVo);
                    cartItemVo.setChecked(true);
                }
                // 如果数据不存在，就需要新增
                cachedCartItemVo = cartItemVo;
            }
            bho.put(cartItemVo.getSkuId().toString(), cachedCartItemVo);
            result.add(cachedCartItemVo);
        }
        return result;
    }

}
