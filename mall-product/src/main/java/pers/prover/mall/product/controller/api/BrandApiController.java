package pers.prover.mall.product.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pers.prover.mall.common.utils.R;
import pers.prover.mall.product.service.BrandService;

import java.util.List;

/**
 * @author 小丶木曾义仲丶哈牛柚子露丶蛋卷
 * @version 1.0
 * @date 2022/6/7 19:46
 */
@RestController
@RequestMapping("/api/product/brand")
public class BrandApiController {

    @Autowired
    private BrandService brandService;

    @PostMapping("/inner/brand-name")
    public R brandNameList(@RequestBody List<Long> brandIds) {
        List<String> brandNames = brandService.brandNameList(brandIds);
        return R.ok().put("data", brandNames);
    }

}
