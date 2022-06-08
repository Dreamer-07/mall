package pers.prover.mall.product.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author 小丶木曾义仲丶哈牛柚子露丶蛋卷
 * @version 1.0
 * @date 2022/6/8 16:04
 */
@ConfigurationProperties(prefix = "mall.thread-pool")
@Data
public class ThreadPoolConfigProperties {

    /**
     * 核心线程数
     */
    private int corePoolSize;

    /**
     * 最大线程数
     */
    private int maximumPoolSize;

    /**
     * 等待时间
     */
    private long keepAliveTime;

}
