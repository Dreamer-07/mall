package pers.prover.mall.coupon.controller;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pers.prover.mall.coupon.entity.SmsSkuBoundsEntity;
import pers.prover.mall.coupon.service.SmsSkuBoundsService;
import pers.prover.mall.common.utils.PageUtils;
import pers.prover.mall.common.utils.R;



/**
 * 商品sku积分设置
 *
 * @author 小·木曾仪仲·哈牛柚子露·蛋卷
 * @email 2391105059@qq.com
 * @date 2022-05-29 15:26:15
 */
@RestController
@RequestMapping("coupon/smsskubounds")
public class SmsSkuBoundsController {
    @Autowired
    private SmsSkuBoundsService smsSkuBoundsService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    // @RequiresPermissions("coupon:smsskubounds:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = smsSkuBoundsService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    // @RequiresPermissions("coupon:smsskubounds:info")
    public R info(@PathVariable("id") Long id){
		SmsSkuBoundsEntity smsSkuBounds = smsSkuBoundsService.getById(id);

        return R.ok().put("smsSkuBounds", smsSkuBounds);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    // @RequiresPermissions("coupon:smsskubounds:save")
    public R save(@RequestBody SmsSkuBoundsEntity smsSkuBounds){
		smsSkuBoundsService.save(smsSkuBounds);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    // @RequiresPermissions("coupon:smsskubounds:update")
    public R update(@RequestBody SmsSkuBoundsEntity smsSkuBounds){
		smsSkuBoundsService.updateById(smsSkuBounds);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    // @RequiresPermissions("coupon:smsskubounds:delete")
    public R delete(@RequestBody Long[] ids){
		smsSkuBoundsService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
