package pers.prover.mall.coupon.controller;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pers.prover.mall.coupon.entity.SmsHomeSubjectSpuEntity;
import pers.prover.mall.coupon.service.SmsHomeSubjectSpuService;
import pers.prover.mall.common.utils.PageUtils;
import pers.prover.mall.common.utils.R;



/**
 * 专题商品
 *
 * @author 小·木曾仪仲·哈牛柚子露·蛋卷
 * @email 2391105059@qq.com
 * @date 2022-05-29 15:26:15
 */
@RestController
@RequestMapping("coupon/smshomesubjectspu")
public class SmsHomeSubjectSpuController {
    @Autowired
    private SmsHomeSubjectSpuService smsHomeSubjectSpuService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    // @RequiresPermissions("coupon:smshomesubjectspu:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = smsHomeSubjectSpuService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    // @RequiresPermissions("coupon:smshomesubjectspu:info")
    public R info(@PathVariable("id") Long id){
		SmsHomeSubjectSpuEntity smsHomeSubjectSpu = smsHomeSubjectSpuService.getById(id);

        return R.ok().put("smsHomeSubjectSpu", smsHomeSubjectSpu);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    // @RequiresPermissions("coupon:smshomesubjectspu:save")
    public R save(@RequestBody SmsHomeSubjectSpuEntity smsHomeSubjectSpu){
		smsHomeSubjectSpuService.save(smsHomeSubjectSpu);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    // @RequiresPermissions("coupon:smshomesubjectspu:update")
    public R update(@RequestBody SmsHomeSubjectSpuEntity smsHomeSubjectSpu){
		smsHomeSubjectSpuService.updateById(smsHomeSubjectSpu);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    // @RequiresPermissions("coupon:smshomesubjectspu:delete")
    public R delete(@RequestBody Long[] ids){
		smsHomeSubjectSpuService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
