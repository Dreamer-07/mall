package pers.prover.mall.ware.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pers.prover.mall.common.utils.R;

import java.util.List;

@FeignClient("mall-product")
public interface ProductFeignClient {
    @GetMapping("/admin/product/skuinfo/inner/skuName/{skuId}")
    R getSkuName(@PathVariable("skuId") Long skuId);
}
