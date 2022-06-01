package pers.prover.mall.product.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pers.prover.mall.common.utils.R;
import pers.prover.mall.product.entity.CategoryEntity;
import pers.prover.mall.product.service.CategoryService;

import java.util.Arrays;
import java.util.List;

/**
 * @author 小丶木曾义仲丶哈牛柚子露丶蛋卷
 * @version 1.0
 * @date 2022/5/30 9:37
 */
@RestController
@RequestMapping("/admin/product/category")
public class CategoryAdminController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/tree-list")
    public R treeList() {
        return R.ok().put("data", categoryService.treeList());
    }

    @DeleteMapping("/delete")
    public R removeByIds(@RequestBody Long[] ids) {
        categoryService.removeByIds(ids);
        return R.ok();
    }

    @PostMapping("/save")
    // @RequiresPermissions("product:category:save")
    public R save(@RequestBody CategoryEntity category){
        //
        categoryService.save(category);
        return R.ok();
    }

    /**
     * 信息
     */
    @GetMapping("/info/{catId}")
    public R info(@PathVariable("catId") Long catId){
        CategoryEntity category = categoryService.getById(catId);
        return R.ok().put("data", category);
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    public R update(@RequestBody CategoryEntity category){
        categoryService.updateCascade(category);
        return R.ok();
    }

    @PostMapping("/update/batch")
    public R updateBatch(@RequestBody CategoryEntity[] categoryEntityList) {
        categoryService.updateBatchById(Arrays.asList(categoryEntityList));
        return R.ok();
    }

}
