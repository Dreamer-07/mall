package pers.prover.mall.auth.config;

import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

/**
 * SpringSession 配置：
 *  1. 配置 session 序列化问题(使用 json 序列化)
 *  2. 配置 cookie domain(作用域)问题，让二级域名之间可以共享 cookie 以此解决分布式 Session 问题
 * @author 小丶木曾义仲丶哈牛柚子露丶蛋卷
 * @version 1.0
 * @date 2022/6/9 15:37
 */
@EnableRedisHttpSession // 开始 spring session redis
@Configuration
public class SessionConfig {

    /**
     * spring session redis 序列化，方便我们查看 redis 中的数据
     * @return
     */
    @Bean
    public RedisSerializer<Object> springSessionDefaultRedisSerializer() {
        return new GenericFastJsonRedisSerializer();
    }

    @Bean
    public CookieSerializer cookieSerializer() {
        DefaultCookieSerializer defaultCookieSerializer = new DefaultCookieSerializer();
        // 设置 cookie 的名字
        defaultCookieSerializer.setCookieName("MALL_SESSION_ID");
        // 设置 cookie 的作用域
        defaultCookieSerializer.setDomainName("mall.com");
        return defaultCookieSerializer;
    }

}
