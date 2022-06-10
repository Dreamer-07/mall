package pers.prover.mall.thirdparty.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author 小丶木曾义仲丶哈牛柚子露丶蛋卷
 * @version 1.0
 * @date 2022/6/9 19:04
 */
@ConfigurationProperties(prefix = "spring.cloud.alicloud.sms")
@Configuration
@Data
public class AliCloudSmsConfigProperties {

    private String host;

    private String path;

    private String appCode;

    private String smsSignId;

    private String templateId;

}
