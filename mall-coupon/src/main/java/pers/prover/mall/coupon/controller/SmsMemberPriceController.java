package pers.prover.mall.coupon.controller;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pers.prover.mall.coupon.entity.SmsMemberPriceEntity;
import pers.prover.mall.coupon.service.SmsMemberPriceService;
import pers.prover.mall.common.utils.PageUtils;
import pers.prover.mall.common.utils.R;



/**
 * 商品会员价格
 *
 * @author 小·木曾仪仲·哈牛柚子露·蛋卷
 * @email 2391105059@qq.com
 * @date 2022-05-29 15:26:15
 */
@RestController
@RequestMapping("coupon/smsmemberprice")
public class SmsMemberPriceController {
    @Autowired
    private SmsMemberPriceService smsMemberPriceService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    // @RequiresPermissions("coupon:smsmemberprice:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = smsMemberPriceService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    // @RequiresPermissions("coupon:smsmemberprice:info")
    public R info(@PathVariable("id") Long id){
		SmsMemberPriceEntity smsMemberPrice = smsMemberPriceService.getById(id);

        return R.ok().put("smsMemberPrice", smsMemberPrice);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    // @RequiresPermissions("coupon:smsmemberprice:save")
    public R save(@RequestBody SmsMemberPriceEntity smsMemberPrice){
		smsMemberPriceService.save(smsMemberPrice);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    // @RequiresPermissions("coupon:smsmemberprice:update")
    public R update(@RequestBody SmsMemberPriceEntity smsMemberPrice){
		smsMemberPriceService.updateById(smsMemberPrice);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    // @RequiresPermissions("coupon:smsmemberprice:delete")
    public R delete(@RequestBody Long[] ids){
		smsMemberPriceService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
