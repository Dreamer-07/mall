package pers.prover.mall.product.controller.admin;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import pers.prover.mall.product.entity.AttrEntity;
import pers.prover.mall.product.entity.AttrGroupEntity;
import pers.prover.mall.product.entity.CategoryEntity;
import pers.prover.mall.product.service.AttrAttrgroupRelationService;
import pers.prover.mall.product.service.AttrGroupService;
import pers.prover.mall.common.utils.PageUtils;
import pers.prover.mall.common.utils.R;
import pers.prover.mall.product.service.CategoryService;
import pers.prover.mall.product.vo.AttrAttrgroupRelationReqVo;
import pers.prover.mall.product.vo.AttrGroupRespVo;


/**
 * 属性分组
 *
 * @author 小·木曾仪仲·哈牛柚子露·蛋卷
 * @email 2391105059@qq.com
 * @date 2022-05-29 15:29:02
 */
@RestController
@RequestMapping("/admin/product/attrgroup")
public class AttrGroupAdminController {
    @Autowired
    private AttrGroupService attrGroupService;
    @Autowired
    private CategoryService categoryService;

    /*
    * 1. 获取会员等级
    * 2. 获取分类关联的品牌
    * 3. 获取分类的所有规格参数分组以及相关的规格参数
    *
    * */

    /**
     * 列表
     */
    @GetMapping("/list/{catelogId}")
    public R list(@RequestParam Map<String, Object> params, @PathVariable Long catelogId) {
        PageUtils page = attrGroupService.queryPage(params, catelogId);
        return R.ok().put("data", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrGroupId}")
    // @RequiresPermissions("product:attrgroup:info")
    public R info(@PathVariable("attrGroupId") Long attrGroupId){
		AttrGroupEntity attrGroup = attrGroupService.getById(attrGroupId);
        List<Long> catlogPath = categoryService.getCatelogPath(attrGroup.getCatelogId());
        attrGroup.setCatelogPath(catlogPath);
        return R.ok().put("attrGroup", attrGroup);
    }

    /**
     * 获取属性分组的关联的所有属性
     * @param attrGroupId
     * @return
     */
    @GetMapping("{attrGroupId}/attr/relation")
    public R getAttrGroupRelationAttr(@PathVariable String attrGroupId) {
        List<AttrEntity> attrEntities = attrGroupService.getAttrGroupRelationAttr(attrGroupId);
        return R.ok().put("data", attrEntities);
    }

    /**
     * 获取属性分组没有关联的其他属性
     * @param attrGroupId
     * @return
     */
    @GetMapping("{attrGroupId}/noattr/relation")
    public R getAttrGroupNoRelationAttr(@RequestParam Map<String, Object> params, @PathVariable String attrGroupId) {
        PageUtils page = attrGroupService.getAttrGroupNoRelationAttr(params, attrGroupId);
        return R.ok().put("page", page);
    }

    @GetMapping("{catlogId}/withattr")
    public R getAttrGroupWithAttrByCatlogId(@PathVariable Long catlogId) {
        List<AttrGroupRespVo> attrGroupWithAttrList = attrGroupService.getAttrGroupWithAttrByCatlogId(catlogId);
        return R.ok().put("data", attrGroupWithAttrList);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    // @RequiresPermissions("product:attrgroup:save")
    public R save(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.save(attrGroup);

        return R.ok();
    }

    @PostMapping("/attr/relation")
    public R saveAttrRelation(@RequestBody List<AttrAttrgroupRelationReqVo> attrAttrgroupRelationReqVos) {
        attrGroupService.saveAttrRelation(attrAttrgroupRelationReqVos);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    // @RequiresPermissions("product:attrgroup:update")
    public R update(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.updateById(attrGroup);

        return R.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/attr/relation/delete")
    public R delete(@RequestBody List<AttrAttrgroupRelationReqVo> attrAttrgroupRelationReqVos) {
        attrGroupService.removeBatch(attrAttrgroupRelationReqVos);
        return R.ok();
    }

}
