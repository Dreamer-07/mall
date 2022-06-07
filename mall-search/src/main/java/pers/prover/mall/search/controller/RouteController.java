package pers.prover.mall.search.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import pers.prover.mall.search.service.SearchProductService;
import pers.prover.mall.search.vo.ProductSearchParamVo;
import pers.prover.mall.search.vo.ProductSearchResultVo;

import java.util.List;

/**
 * @author 小丶木曾义仲丶哈牛柚子露丶蛋卷
 * @version 1.0
 * @date 2022/6/7 14:07
 */
@Controller
public class RouteController {

    @Autowired
    private SearchProductService searchProductService;

    @GetMapping("/product/list")
    @ResponseBody
    public ProductSearchResultVo searchProductList(ProductSearchParamVo productSearchParamVo) {
        ProductSearchResultVo productInfo = searchProductService.search(productSearchParamVo);
        // model.addAttribute("productInfo", productInfo);
        // return "list";
        return productInfo;
    }

}
