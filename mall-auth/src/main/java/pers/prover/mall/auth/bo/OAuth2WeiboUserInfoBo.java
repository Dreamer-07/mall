package pers.prover.mall.auth.bo;

import lombok.Data;

/**
 * weibo oauth2 微博登录获取 user_info 的实体类
 * @author 小丶木曾义仲丶哈牛柚子露丶蛋卷
 * @version 1.0
 * @date 2022/6/10 10:25
 */
@Data
public class OAuth2WeiboUserInfoBo {

    private String name;

    private String profile_image_url;

}
