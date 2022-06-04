package pers.prover.mall.product.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import pers.prover.mall.common.to.es.ProductMapping;
import pers.prover.mall.common.utils.R;

import java.util.List;

/**
 * @author 小丶木曾义仲丶哈牛柚子露丶蛋卷
 * @version 1.0
 * @date 2022/6/4 11:19
 */
@FeignClient("mall-search")
public interface SearchFeignClient {

    @PostMapping("/search/product/inner/up")
    public R productUp(@RequestBody List<ProductMapping> productMappingList);

}
