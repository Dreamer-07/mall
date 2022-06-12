package pers.prover.mall.auth.service.impl;

import com.alibaba.fastjson.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import pers.prover.mall.auth.bo.OAuth2WeiboAccessTokenBo;
import pers.prover.mall.auth.bo.OAuth2WeiboUserInfoBo;
import pers.prover.mall.auth.client.MemberFeignClient;
import pers.prover.mall.auth.config.properties.OAuth2WeiboConfigProperties;
import pers.prover.mall.auth.service.OAuth2AuthService;
import pers.prover.mall.common.utils.R;
import pers.prover.mall.common.vo.MemberLoginInfoVo;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

/**
 * @author 小丶木曾义仲丶哈牛柚子露丶蛋卷
 * @version 1.0
 * @date 2022/6/10 10:16
 */
@Service
public class OAuth2AuthServiceImpl implements OAuth2AuthService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private OAuth2WeiboConfigProperties oAuth2WeiboConfigProperties;

    @Autowired
    private MemberFeignClient memberFeignClient;


    @Override
    public MemberLoginInfoVo getWeiboMemberInfo(OAuth2WeiboAccessTokenBo oAuth2WeiboAccessTokenBo) throws URISyntaxException {
        if (oAuth2WeiboAccessTokenBo == null) {
            throw new RuntimeException("获取授权信息失败!!");
        }
        // 先通过远程调用判断当前系统中是否存在对应的用户(根据 uid)
        R memberInfoByUidResult = memberFeignClient.getMemberInfoByUid(oAuth2WeiboAccessTokenBo.getUid());
        // 获取账户信息
        MemberLoginInfoVo memberLoginInfoVo = memberInfoByUidResult.getData(new TypeReference<MemberLoginInfoVo>() {
        });
        // 如果用户已经注册过就直接返回
        if (memberLoginInfoVo != null) {
            return memberLoginInfoVo;
        }
        // 如果用户是第一次登录，就需要通过微博的api获取对应的信息，完成注册后再返回
        HashMap<String, String> paramMap = new HashMap<>();
        paramMap.put("access_token", oAuth2WeiboAccessTokenBo.getAccess_token());
        paramMap.put("uid", oAuth2WeiboAccessTokenBo.getUid());
        // 不存在就需要调用微博的接口获取用户信息
        OAuth2WeiboUserInfoBo oAuth2WeiboUserInfoBo = restTemplate.getForObject(oAuth2WeiboConfigProperties.getShowUserUrl(), OAuth2WeiboUserInfoBo.class, paramMap);
        R registerWeiboMemberResult = memberFeignClient.registerWeiboMember(oAuth2WeiboUserInfoBo);
        if (registerWeiboMemberResult.getCode() != 0) {
            throw new RuntimeException("注册用户失败，请稍后重试");
        }
        memberLoginInfoVo = registerWeiboMemberResult.getData(new TypeReference<MemberLoginInfoVo>() {});
        return memberLoginInfoVo;
    }
}
