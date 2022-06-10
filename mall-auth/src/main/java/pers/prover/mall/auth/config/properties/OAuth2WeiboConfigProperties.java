package pers.prover.mall.auth.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author 小丶木曾义仲丶哈牛柚子露丶蛋卷
 * @version 1.0
 * @date 2022/6/9 22:15
 */
@ConfigurationProperties(prefix = "auth.oauth2.weibo")
@Configuration
@Data
public class OAuth2WeiboConfigProperties {

    private String accessTokenUrl;

    private String showUserUrl;

    private String appKey;

    private String appSecret;

    private String redirectUri;

    private String grantType = "authorization_code";


}
