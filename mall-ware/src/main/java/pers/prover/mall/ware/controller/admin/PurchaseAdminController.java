package pers.prover.mall.ware.controller.admin;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import pers.prover.mall.ware.entity.PurchaseEntity;
import pers.prover.mall.ware.service.PurchaseService;
import pers.prover.mall.common.utils.PageUtils;
import pers.prover.mall.common.utils.R;
import pers.prover.mall.ware.vo.MergeVo;
import pers.prover.mall.ware.vo.PurchaseReqVo;


/**
 * 采购信息
 *
 * @author 小·木曾仪仲·哈牛柚子露·蛋卷
 * @email 2391105059@qq.com
 * @date 2022-05-29 15:30:45
 */
@RestController
@RequestMapping("admin/ware/purchase")
public class PurchaseAdminController {
    @Autowired
    private PurchaseService purchaseService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    // @RequiresPermissions("ware:purchase:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = purchaseService.queryPage(params);

        return R.ok().put("page", page);
    }

    @GetMapping("unreceive/list")
    public R unreceiveList(@RequestParam Map<String, Object> params) {
        PageUtils page =  purchaseService.queryUnreceiveList(params);
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    // @RequiresPermissions("ware:purchase:info")
    public R info(@PathVariable("id") Long id){
		PurchaseEntity purchase = purchaseService.getById(id);

        return R.ok().put("purchase", purchase);
    }

    @PostMapping("/save")
    public R save(@RequestBody PurchaseEntity purchaseEntity) {
        purchaseService.save(purchaseEntity);
        return R.ok();
    }

    /**
     * 保存
     */
    @PostMapping("/merge")
    public R megre(@RequestBody MergeVo mergeVo) {
        purchaseService.saveMegre(mergeVo);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody PurchaseEntity purchase){
		purchaseService.updateDetail(purchase);
        return R.ok();
    }

    @PostMapping("/received")
    public R received(@RequestBody List<Long> purchaseIds) {
        purchaseService.received(purchaseIds);
        return R.ok();
    }

    @PostMapping("/done")
    public R done(@RequestBody PurchaseReqVo purchaseReqVo) {
        purchaseService.done(purchaseReqVo);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    // @RequiresPermissions("ware:purchase:delete")
    public R delete(@RequestBody Long[] ids){
		purchaseService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
