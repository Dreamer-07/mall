package pers.prover.mall.product.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pers.prover.mall.product.entity.CategoryEntity;
import pers.prover.mall.product.service.CategoryService;

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

    @GetMapping({"/", "/index.html"})
    public String routeIndex(Model model) {
        List<CategoryEntity> categoryEntityList = categoryService.listByParentId(0L);
        model.addAttribute("categoryList", categoryEntityList);
        return "index";
    }
}
