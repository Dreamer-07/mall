package pers.prover.mall.product.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pers.prover.mall.product.service.CategoryService;
import pers.prover.mall.product.vo.api.CategoryLevel2RespVo;

import java.util.List;
import java.util.Map;

/**
 * @author 小丶木曾义仲丶哈牛柚子露丶蛋卷
 * @version 1.0
 * @date 2022/6/4 15:53
 */
@RestController
@RequestMapping("/api/product/category")
public class CategoryApiController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/list")
    public Map<Long, List<CategoryLevel2RespVo>> list() {
        return categoryService.listMapByParentId();
    }

}
