package pers.prover.mall.auth.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import pers.prover.mall.auth.bo.OAuth2WeiboUserInfoBo;
import pers.prover.mall.auth.vo.BaseRegisterVo;
import pers.prover.mall.common.utils.R;
import pers.prover.mall.common.vo.MemberLoginInfoVo;


/**
 * @author 小丶木曾义仲丶哈牛柚子露丶蛋卷
 * @version 1.0
 * @date 2022/6/9 18:41
 */
@FeignClient("mall-member")
@RequestMapping("/api/member/member")
public interface MemberFeignClient {

    @GetMapping("/inner/phone/{phone}")
    R getMemberInfoByPhone(@PathVariable("phone") String phone);

    @GetMapping("/inner/uid/{uid}")
    R getMemberInfoByUid(@PathVariable("uid") String uid);

    @PostMapping("/inner/register")
    R registerMember(@RequestBody BaseRegisterVo baseRegisterVo);

    @PostMapping("/inner/register/weibo")
    R registerWeiboMember(@RequestBody OAuth2WeiboUserInfoBo oAuth2WeiboUserInfoBo);
}
