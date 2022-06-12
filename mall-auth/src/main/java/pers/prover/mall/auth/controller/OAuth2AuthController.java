package pers.prover.mall.auth.controller;

import com.alibaba.fastjson.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import pers.prover.mall.auth.bo.OAuth2WeiboAccessTokenBo;
import pers.prover.mall.auth.client.MemberFeignClient;
import pers.prover.mall.auth.config.properties.OAuth2WeiboConfigProperties;
import pers.prover.mall.auth.service.OAuth2AuthService;
import pers.prover.mall.common.constant.AuthConstant;
import pers.prover.mall.common.constant.RedisKeyConstant;
import pers.prover.mall.common.utils.R;
import pers.prover.mall.common.vo.MemberLoginInfoVo;

import javax.servlet.http.HttpSession;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

/**
 * @author 小丶木曾义仲丶哈牛柚子露丶蛋卷
 * @version 1.0
 * @date 2022/6/9 20:27
 */
@RestController
@RequestMapping("/auth/oauth2")
public class OAuth2AuthController {

    @Autowired
    private MemberFeignClient memberFeignClient;

    @Autowired
    private OAuth2AuthService oAuth2AuthService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private OAuth2WeiboConfigProperties oAuth2WeiboConfigProperties;

    /**
     * 微博(oauth2)授权成功回调
     *
     * @return
     */
    @GetMapping("/weibo/authorize/success")
    public R weiboAuthorizeSuccessCallback(@RequestParam("code") String code, HttpSession session) {
        // 设置请求参数
        MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("client_id", oAuth2WeiboConfigProperties.getAppKey());
        paramMap.add("client_secret", oAuth2WeiboConfigProperties.getAppSecret());
        paramMap.add("grant_type", oAuth2WeiboConfigProperties.getGrantType());
        paramMap.add("redirect_uri", oAuth2WeiboConfigProperties.getRedirectUri());
        paramMap.add("code", code);

        // 获取用户 token 和 uid
        try {
            // 构建请求体
            RequestEntity<MultiValueMap<String, String>> requestEntity = RequestEntity
                    .post(new URI(""))
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .accept(MediaType.ALL).acceptCharset(StandardCharsets.UTF_8)
                    .body(paramMap);
            // 发送请求
            ResponseEntity<OAuth2WeiboAccessTokenBo> accessTokenBoResponseEntity = restTemplate.postForEntity(oAuth2WeiboConfigProperties.getAccessTokenUrl(), requestEntity, OAuth2WeiboAccessTokenBo.class);
            OAuth2WeiboAccessTokenBo oAuth2WeiboAccessTokenBo = accessTokenBoResponseEntity.getBody();
            // 通过 uid/token 获取微博用户信息
            MemberLoginInfoVo memberLoginInfoVo = oAuth2AuthService.getWeiboMemberInfo(oAuth2WeiboAccessTokenBo);
            // 保存到会话中
            session.setAttribute(AuthConstant.SESSION_LOGIN_INFO, memberLoginInfoVo);
            return R.ok();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return R.error();
    }

}
