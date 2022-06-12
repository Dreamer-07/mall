package pers.prover.mall.cart.config;

import org.aspectj.weaver.tools.WeavingAdaptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import pers.prover.mall.cart.interceptor.UserKeyInterceptor;

/**
 * @author 小丶木曾义仲丶哈牛柚子露丶蛋卷
 * @version 1.0
 * @date 2022/6/11 16:46
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * 向容器中添加拦截器
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new UserKeyInterceptor()).addPathPatterns("/**");
    }
}
