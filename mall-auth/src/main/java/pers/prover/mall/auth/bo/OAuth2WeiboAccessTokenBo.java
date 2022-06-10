package pers.prover.mall.auth.bo;

import lombok.Data;

/**
 * weibo oauth2 微博登录获取 access_token 业务实体类
 * @author 小丶木曾义仲丶哈牛柚子露丶蛋卷
 * @version 1.0
 * @date 2022/6/9 22:25
 */
@Data
public class OAuth2WeiboAccessTokenBo {

    private String access_token;

    private String expires_in;

    private String uid;

}
