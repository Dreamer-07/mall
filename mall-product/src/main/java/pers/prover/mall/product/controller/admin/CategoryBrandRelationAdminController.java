package pers.prover.mall.product.controller.admin;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import pers.prover.mall.product.entity.CategoryBrandRelationEntity;
import pers.prover.mall.product.entity.CategoryEntity;
import pers.prover.mall.product.service.CategoryBrandRelationService;
import pers.prover.mall.common.utils.PageUtils;
import pers.prover.mall.common.utils.R;



/**
 * 品牌分类关联
 *
 * @author 小·木曾仪仲·哈牛柚子露·蛋卷
 * @email 2391105059@qq.com
 * @date 2022-05-31 19:48:37
 */
@RestController
@RequestMapping("/admin/product/categorybrandrelation")
public class CategoryBrandRelationAdminController {
    @Autowired
    private CategoryBrandRelationService categoryBrandRelationService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    // @RequiresPermissions("product:categorybrandrelation:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = categoryBrandRelationService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    // @RequiresPermissions("product:categorybrandrelation:info")
    public R info(@PathVariable("id") Long id){
		CategoryBrandRelationEntity categoryBrandRelation = categoryBrandRelationService.getById(id);

        return R.ok().put("categoryBrandRelation", categoryBrandRelation);
    }

    @GetMapping("/catelog/{brandId}")
    public R getCatelogByBrandId(@PathVariable Long brandId) {
        List<CategoryBrandRelationEntity> categoryBrandRelationEntities = categoryBrandRelationService.getCatelogByBrandId(brandId);
        return R.ok().put("data", categoryBrandRelationEntities);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody CategoryBrandRelationEntity categoryBrandRelation){
		// categoryBrandRelationService.save(categoryBrandRelation);
        categoryBrandRelationService.saveCascade(categoryBrandRelation);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    // @RequiresPermissions("product:categorybrandrelation:update")
    public R update(@RequestBody CategoryBrandRelationEntity categoryBrandRelation){
		categoryBrandRelationService.updateById(categoryBrandRelation);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    // @RequiresPermissions("product:categorybrandrelation:delete")
    public R delete(@RequestBody Long[] ids){
		categoryBrandRelationService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
