package pers.prover.mall.product.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pers.prover.mall.common.utils.PageUtils;
import pers.prover.mall.common.utils.R;
import pers.prover.mall.product.entity.AttrEntity;
import pers.prover.mall.product.entity.ProductAttrValueEntity;
import pers.prover.mall.product.service.AttrService;
import pers.prover.mall.product.vo.Attr;
import pers.prover.mall.product.vo.AttrReqVo;
import pers.prover.mall.product.vo.AttrRespVo;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * 商品属性
 *
 * @author 小·木曾仪仲·哈牛柚子露·蛋卷
 * @email 2391105059@qq.com
 * @date 2022-05-29 15:29:02
 */
@RestController
@RequestMapping("/admin/product/attr")
public class AttrController {
    @Autowired
    private AttrService attrService;

    /**
     * 查询规格(base)属性
     */
    @RequestMapping("/base/list/{catelogId}")
    // @RequiresPermissions("product:attr:list")
    public R queryBaseList(@RequestParam Map<String, Object> params, @PathVariable Long catelogId){
        PageUtils page = attrService.queryBaseListPage(params, catelogId);

        return R.ok().put("page", page);
    }

    @GetMapping("/base/listforspu/{spuId}")
    public R getBaseListBySpuId(@PathVariable("spuId") Long spuId) {
        List<ProductAttrValueEntity> productAttrValueEntityList = attrService.getBaseListBySpuId(spuId);
        return R.ok().put("data", productAttrValueEntityList);
    }

    @RequestMapping("/sale/list/{catelogId}")
    // @RequiresPermissions("product:attr:list")
    public R querySaleList(@RequestParam Map<String, Object> params, @PathVariable Long catelogId){
        PageUtils page = attrService.querySaleListPage(params, catelogId);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrId}")
    // @RequiresPermissions("product:attr:info")
    public R info(@PathVariable("attrId") Long attrId){
        AttrRespVo attr = attrService.getDetailInAdmin(attrId);

        return R.ok().put("attr", attr);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    // @RequiresPermissions("product:attr:save")
    public R save(@RequestBody AttrReqVo attrReqVo) {
        attrService.save(attrReqVo);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    // @RequiresPermissions("product:attr:update")
    public R update(@RequestBody AttrReqVo attrReqVo){
		attrService.updateById(attrReqVo);

        return R.ok();
    }

    @PostMapping("/update/{spuId}")
    public R updateBaseAttr(@PathVariable Long spuId, @RequestBody List<ProductAttrValueEntity> productAttrValueEntityList) {
        attrService.updateBaseAttr(spuId, productAttrValueEntityList);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    // @RequiresPermissions("product:attr:delete")
    public R delete(@RequestBody Long[] attrIds){
		attrService.removeByIds(Arrays.asList(attrIds));

        return R.ok();
    }

}
