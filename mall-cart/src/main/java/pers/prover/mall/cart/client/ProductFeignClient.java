package pers.prover.mall.cart.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import pers.prover.mall.common.utils.R;

/**
 * @author 小丶木曾义仲丶哈牛柚子露丶蛋卷
 * @version 1.0
 * @date 2022/6/11 18:39
 */
@FeignClient("mall-product")
@RequestMapping("/api/product")
public interface ProductFeignClient {

    @GetMapping("/skuInfo/inner/cart/{skuId}")
    public R getSkuCartItem(@PathVariable("skuId") Long skuId);

}
