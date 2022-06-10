package pers.prover.mall.member.controller.api;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import pers.prover.mall.common.vo.MemberAuthInfoVo;
import pers.prover.mall.common.vo.MemberLoginInfoVo;
import pers.prover.mall.member.entity.MemberEntity;
import pers.prover.mall.member.service.MemberService;
import pers.prover.mall.common.utils.PageUtils;
import pers.prover.mall.common.utils.R;
import pers.prover.mall.member.to.MemberRegisterTo;
import pers.prover.mall.member.to.WeiboMemberResgiterTo;


/**
 * 会员
 *
 * @author 小·木曾仪仲·哈牛柚子露·蛋卷
 * @email 2391105059@qq.com
 * @date 2022-05-29 15:27:52
 */
@RestController
@RequestMapping("api/member/member")
public class MemberApiController {
    @Autowired
    private MemberService memberService;

    // /**
    //  * 列表
    //  */
    // @RequestMapping("/list")
    // // @RequiresPermissions("member:member:list")
    // public R list(@RequestParam Map<String, Object> params){
    //     PageUtils page = memberService.queryPage(params);
    //
    //     return R.ok().put("page", page);
    // }
    //
    //
    // /**
    //  * 信息
    //  */
    // @RequestMapping("/info/{id}")
    // // @RequiresPermissions("member:member:info")
    // public R info(@PathVariable("id") Long id){
	// 	MemberEntity member = memberService.getById(id);
    //
    //     return R.ok().put("member", member);
    // }
    //
    // /**
    //  * 保存
    //  */
    // @RequestMapping("/save")
    // // @RequiresPermissions("member:member:save")
    // public R save(@RequestBody MemberEntity member){
	// 	memberService.save(member);
    //
    //     return R.ok();
    // }
    //
    // /**
    //  * 修改
    //  */
    // @RequestMapping("/update")
    // // @RequiresPermissions("member:member:update")
    // public R update(@RequestBody MemberEntity member){
	// 	memberService.updateById(member);
    //
    //     return R.ok();
    // }
    //
    // /**
    //  * 删除
    //  */
    // @RequestMapping("/delete")
    // // @RequiresPermissions("member:member:delete")
    // public R delete(@RequestBody Long[] ids){
	// 	memberService.removeByIds(Arrays.asList(ids));
    //
    //     return R.ok();
    // }

    @GetMapping("inner/phone/{phone}")
    public R getMemberInfoByPhone(@PathVariable String phone) {
        MemberAuthInfoVo memberAuthInfoVo = memberService.getMemberInfoByPhone(phone);
        return R.ok().put("data", memberAuthInfoVo);
    }

    @GetMapping("inner/uid/{uid}")
    public R getMemberInfoByUid(@PathVariable String uid) {
        MemberLoginInfoVo memberLoginInfoVo = memberService.getMemberInfoByUid(uid);
        return R.ok().put("data", memberLoginInfoVo);
    }

    @PostMapping("/inner/register")
    public R registerMember(@RequestBody MemberRegisterTo memberRegisterTo) {
        memberService.registerMember(memberRegisterTo);
        return R.ok();
    }

    @PostMapping("/inner/register/weibo")
    public R registerWeiBoMember(@RequestBody WeiboMemberResgiterTo weiboMemberResgiterTo) {
        MemberLoginInfoVo memberLoginInfoVo = memberService.registerMember(weiboMemberResgiterTo);
        return R.ok().put("data", memberLoginInfoVo);
    }

}
