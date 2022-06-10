package pers.prover.mall.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pers.prover.mall.common.constant.AuthConstant;
import pers.prover.mall.auth.service.BaseAuthService;
import pers.prover.mall.auth.vo.BaseAuthVo;
import pers.prover.mall.auth.vo.BaseRegisterVo;
import pers.prover.mall.common.utils.R;
import pers.prover.mall.common.vo.MemberLoginInfoVo;

import javax.servlet.http.HttpSession;

/**
 * 基础的登录(认证)方式
 * @author 小丶木曾义仲丶哈牛柚子露丶蛋卷
 * @version 1.0
 * @date 2022/6/9 15:46
 */
@RestController
@RequestMapping("/auth/base")
public class BaseAuthController {

    @Autowired
    private BaseAuthService baseAuthService;

    @PostMapping("/login/phone")
    public R phoneAuth(@RequestBody BaseAuthVo baseAuthVo, HttpSession session) {
        MemberLoginInfoVo memberLoginInfoVo = baseAuthService.phoneAuth(baseAuthVo);
        // 在 session 中保存用户信息
        session.setAttribute(AuthConstant.SESSION_LOGIN_INFO, memberLoginInfoVo);
        return R.ok();
    }

    @PostMapping("/register")
    public R registerMember(@RequestBody BaseRegisterVo baseRegisterVo) {
        baseAuthService.registerMember(baseRegisterVo);
        return R.ok();
    }

}
