package pers.prover.mall.search.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import pers.prover.mall.common.utils.R;

import java.util.List;

/**
 * @author 小丶木曾义仲丶哈牛柚子露丶蛋卷
 * @version 1.0
 * @date 2022/6/7 19:42
 */
@FeignClient("mall-product")
public interface ProductFeignClient {
    @PostMapping("/api/product/brand/inner/brand-name")
    public R brandNameList(@RequestBody List<Long> brandIds);
}
