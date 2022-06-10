package pers.prover.mall.auth.service;

import pers.prover.mall.auth.vo.BaseAuthVo;
import pers.prover.mall.auth.vo.BaseRegisterVo;
import pers.prover.mall.common.vo.MemberLoginInfoVo;

public interface BaseAuthService {
    /**
     * 手机认证
     * @param baseAuthVo
     * @return
     */
    MemberLoginInfoVo phoneAuth(BaseAuthVo baseAuthVo);

    /**
     * 注册用户
     * @param baseRegisterVo
     */
    void registerMember(BaseRegisterVo baseRegisterVo);
}
