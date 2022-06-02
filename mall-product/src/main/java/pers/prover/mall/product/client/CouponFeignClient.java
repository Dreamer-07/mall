package pers.prover.mall.product.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import pers.prover.mall.common.to.SkuReductionTo;
import pers.prover.mall.common.to.SpuBoundTo;
import pers.prover.mall.common.utils.R;

@FeignClient("mall-coupon")
public interface CouponFeignClient {

    @PostMapping("/admin/coupon/spubounds/inner/save")
    R saveSpuBounds(@RequestBody SpuBoundTo spuBoundTo);

    @PostMapping("/admin/coupon/skufullreduction/inner/save")
    R saveSkuReduction(@RequestBody SkuReductionTo skuReductionTo);

}
