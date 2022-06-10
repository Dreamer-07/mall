package pers.prover.mall.auth.service;

import pers.prover.mall.auth.bo.OAuth2WeiboAccessTokenBo;
import pers.prover.mall.common.vo.MemberLoginInfoVo;

import java.net.MalformedURLException;
import java.net.URISyntaxException;

/**
 * @author 小丶木曾义仲丶哈牛柚子露丶蛋卷
 * @version 1.0
 * @date 2022/6/10 10:16
 */
public interface OAuth2AuthService {

    /**
     * 获取微博用户的信息
     * @param oAuth2WeiboAccessTokenBo
     * @return
     */
    MemberLoginInfoVo getWeiboMemberInfo(OAuth2WeiboAccessTokenBo oAuth2WeiboAccessTokenBo) throws URISyntaxException;

}
