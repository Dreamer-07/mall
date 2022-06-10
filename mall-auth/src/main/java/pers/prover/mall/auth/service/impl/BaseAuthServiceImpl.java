package pers.prover.mall.auth.service.impl;

import com.alibaba.fastjson.TypeReference;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pers.prover.mall.auth.client.MemberFeignClient;
import pers.prover.mall.auth.service.BaseAuthService;
import pers.prover.mall.auth.vo.BaseAuthVo;
import pers.prover.mall.auth.vo.BaseRegisterVo;
import pers.prover.mall.common.constant.RedisKeyConstant;
import pers.prover.mall.common.utils.R;
import pers.prover.mall.common.vo.MemberAuthInfoVo;
import pers.prover.mall.common.vo.MemberLoginInfoVo;

/**
 * @author 小丶木曾义仲丶哈牛柚子露丶蛋卷
 * @version 1.0
 * @date 2022/6/9 16:10
 */
@Service
public class BaseAuthServiceImpl implements BaseAuthService {

    @Autowired
    private MemberFeignClient memberFeignClient;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public MemberLoginInfoVo phoneAuth(BaseAuthVo baseAuthVo) {
        // 查找出用户的信息
        R memberInfoResult = memberFeignClient.getMemberInfoByPhone(baseAuthVo.getPhone());
        MemberAuthInfoVo memberAuthInfoVo = memberInfoResult.getData(new TypeReference<MemberAuthInfoVo>() {});
        if (memberAuthInfoVo != null) {
            String passwordForDB = memberAuthInfoVo.getPassword();
            String passwordForInput = baseAuthVo.getPassword();
            if (passwordEncoder.matches(passwordForInput, passwordForDB)) {
                MemberLoginInfoVo memberLoginInfoVo = new MemberLoginInfoVo();
                BeanUtils.copyProperties(memberAuthInfoVo, memberLoginInfoVo);
                return memberLoginInfoVo;
            }
            throw new RuntimeException("用户密码错误");
        }
        throw new RuntimeException((String) memberInfoResult.get("msg"));
    }

    @Override
    public void registerMember(BaseRegisterVo baseRegisterVo) {
        String cachedCode = stringRedisTemplate.opsForValue().get(RedisKeyConstant.AUTH_REGISTER_PHONE_CODE + baseRegisterVo.getPhone());
        if (cachedCode == null) {
            throw new RuntimeException("验证码失效了，请重新获取");
        }
        if (!cachedCode.equalsIgnoreCase(baseRegisterVo.getCode())) {
            throw new RuntimeException("验证码不一致，请重新输入");
        }
        String encodePassword = passwordEncoder.encode(baseRegisterVo.getPassword());
        baseRegisterVo.setPassword(encodePassword);
        R registerMemberResult = memberFeignClient.registerMember(baseRegisterVo);
        if (registerMemberResult.getCode() != 0) {
            throw new RuntimeException((String) registerMemberResult.get("msg"));
        }
    }
}
