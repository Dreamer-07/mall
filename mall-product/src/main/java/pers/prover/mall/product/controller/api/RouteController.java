package pers.prover.mall.product.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import pers.prover.mall.product.entity.CategoryEntity;
import pers.prover.mall.product.service.CategoryService;
import pers.prover.mall.product.service.SkuInfoService;
import pers.prover.mall.product.vo.api.SkuItemVo;

import java.util.List;

/**
 * @author 小丶木曾义仲丶哈牛柚子露丶蛋卷
 * @version 1.0
 * @date 2022/6/4 15:46
 */
@Controller
public class RouteController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SkuInfoService skuInfoService;

    @GetMapping({"/", "/index.html"})
    public String routeIndex(Model model) {
        List<CategoryEntity> categoryEntityList = categoryService.listByParentId(0L);
        model.addAttribute("categoryList", categoryEntityList);
        return "index";
    }

    @GetMapping("/{skuId}.html")
    @ResponseBody
    public SkuItemVo routeItem(@PathVariable Long skuId) {
        return skuInfoService.getSkuItem(skuId);
        // model.addAttribute("skuItemVo", skuItemVo);
        // return "";
    }
}
