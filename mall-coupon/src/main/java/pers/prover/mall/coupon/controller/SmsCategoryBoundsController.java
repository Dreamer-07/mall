package pers.prover.mall.coupon.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pers.prover.mall.common.utils.PageUtils;
import pers.prover.mall.common.utils.R;
import pers.prover.mall.coupon.entity.SmsCategoryBoundsEntity;
import pers.prover.mall.coupon.service.SmsCategoryBoundsService;

import java.util.Arrays;
import java.util.Map;



/**
 * 商品分类积分设置
 *
 * @author 小·木曾仪仲·哈牛柚子露·蛋卷
 * @email 2391105059@qq.com
 * @date 2022-05-29 15:26:15
 */
@RestController
@RequestMapping("coupon/smscategorybounds")
public class SmsCategoryBoundsController {
    @Autowired
    private SmsCategoryBoundsService smsCategoryBoundsService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    // @RequiresPermissions("coupon:smscategorybounds:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = smsCategoryBoundsService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    // @RequiresPermissions("coupon:smscategorybounds:info")
    public R info(@PathVariable("id") Long id){
		SmsCategoryBoundsEntity smsCategoryBounds = smsCategoryBoundsService.getById(id);

        return R.ok().put("smsCategoryBounds", smsCategoryBounds);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    // @RequiresPermissions("coupon:smscategorybounds:save")
    public R save(@RequestBody SmsCategoryBoundsEntity smsCategoryBounds){
		smsCategoryBoundsService.save(smsCategoryBounds);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    // @RequiresPermissions("coupon:smscategorybounds:update")
    public R update(@RequestBody SmsCategoryBoundsEntity smsCategoryBounds){
		smsCategoryBoundsService.updateById(smsCategoryBounds);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    // @RequiresPermissions("coupon:smscategorybounds:delete")
    public R delete(@RequestBody Long[] ids){
		smsCategoryBoundsService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
