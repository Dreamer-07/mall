package pers.prover.mall.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import pers.prover.mall.common.utils.PageUtils;
import pers.prover.mall.common.vo.MemberAuthInfoVo;
import pers.prover.mall.common.vo.MemberLoginInfoVo;
import pers.prover.mall.member.entity.MemberEntity;
import pers.prover.mall.member.to.MemberRegisterTo;
import pers.prover.mall.member.to.WeiboMemberResgiterTo;

import java.util.Map;

/**
 * 会员
 *
 * @author 小·木曾仪仲·哈牛柚子露·蛋卷
 * @email 2391105059@qq.com
 * @date 2022-05-29 15:27:52
 */
public interface MemberService extends IService<MemberEntity> {

    PageUtils queryPage(Map<String, Object> params);

    MemberAuthInfoVo getMemberInfoByPhone(String phone);

    /**
     * 注册用户
     * @param memberRegisterTo
     */
    void registerMember(MemberRegisterTo memberRegisterTo);

    /**
     * 根据 uid 查找用户信息
     * @param uid
     */
    MemberLoginInfoVo getMemberInfoByUid(String uid);

    /**
     * 注册微博账户
     * @param weiboMemberResgiterTo
     * @return
     */
    MemberLoginInfoVo registerMember(WeiboMemberResgiterTo weiboMemberResgiterTo);
}

