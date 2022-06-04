package pers.prover.mall.search.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pers.prover.mall.common.to.es.ProductMapping;
import pers.prover.mall.common.utils.R;
import pers.prover.mall.search.service.SearchProductService;

import java.util.List;

/**
 * @author 小丶木曾义仲丶哈牛柚子露丶蛋卷
 * @version 1.0
 * @date 2022/6/3 22:14
 */
@RestController
@RequestMapping("/search/product")
public class SearchProductController {


    @Autowired
    private SearchProductService searchProductService;

    /**
     * 商品上架
     * @param productMappingList
     * @return
     */
    @PostMapping("/inner/up")
    public R productUp(@RequestBody List<ProductMapping> productMappingList) {
        boolean flag = searchProductService.saveProductUpInfo(productMappingList);
        return R.ok().put("data", flag);
    }

}
