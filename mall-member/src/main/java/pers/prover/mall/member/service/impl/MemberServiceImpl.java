package pers.prover.mall.member.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import pers.prover.mall.common.utils.PageUtils;
import pers.prover.mall.common.utils.Query;

import pers.prover.mall.common.vo.MemberAuthInfoVo;
import pers.prover.mall.common.vo.MemberLoginInfoVo;
import pers.prover.mall.member.dao.MemberDao;
import pers.prover.mall.member.entity.MemberEntity;
import pers.prover.mall.member.service.MemberService;
import pers.prover.mall.member.to.MemberRegisterTo;
import pers.prover.mall.member.to.WeiboMemberResgiterTo;


@Service("memberService")
public class MemberServiceImpl extends ServiceImpl<MemberDao, MemberEntity> implements MemberService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<MemberEntity> page = this.page(
                new Query<MemberEntity>().getPage(params),
                new QueryWrapper<MemberEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public MemberAuthInfoVo getMemberInfoByPhone(String phone) {
        LambdaQueryWrapper<MemberEntity> selectLqw = new LambdaQueryWrapper<MemberEntity>()
                .eq(MemberEntity::getMobile, phone);
        MemberEntity memberEntity = this.getOne(selectLqw);
        MemberAuthInfoVo memberAuthInfoVo = new MemberAuthInfoVo();
        BeanUtils.copyProperties(memberEntity, memberAuthInfoVo);
        memberAuthInfoVo.setMemberId(memberEntity.getId());
        memberAuthInfoVo.setPhone(memberEntity.getPassword());
        return memberAuthInfoVo;
    }

    @Override
    public void registerMember(MemberRegisterTo memberRegisterTo) {
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setMobile(memberRegisterTo.getPhone());
        memberEntity.setPassword(memberRegisterTo.getPassword());
        memberEntity.setNickname(memberRegisterTo.getNickname());
        this.save(memberEntity);
    }

    @Override
    public MemberLoginInfoVo getMemberInfoByUid(String uid) {
        LambdaQueryWrapper<MemberEntity> selectLqw = new LambdaQueryWrapper<MemberEntity>()
                .eq(MemberEntity::getWeiboId, uid);
        MemberEntity memberEntity = this.getOne(selectLqw);
        if (memberEntity == null) {
            return null;
        }
        MemberLoginInfoVo memberLoginInfoVo = new MemberLoginInfoVo();
        memberLoginInfoVo.setMemberId(memberEntity.getId());
        memberLoginInfoVo.setNickname(memberEntity.getNickname());
        memberLoginInfoVo.setHeader(memberEntity.getHeader());
        return memberLoginInfoVo;
    }

    @Override
    public MemberLoginInfoVo registerMember(WeiboMemberResgiterTo weiboMemberResgiterTo) {
        // 保存用户西悉尼
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setNickname(weiboMemberResgiterTo.getName());
        memberEntity.setHeader(weiboMemberResgiterTo.getProfile_image_url());
        this.save(memberEntity);
        // 封装需要回传的数据
        MemberLoginInfoVo memberLoginInfoVo = new MemberLoginInfoVo();
        BeanUtils.copyProperties(memberEntity, memberLoginInfoVo);
        memberLoginInfoVo.setMemberId(memberEntity.getId());
        return memberLoginInfoVo;
    }

}