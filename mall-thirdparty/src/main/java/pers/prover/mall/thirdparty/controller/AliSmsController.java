package pers.prover.mall.thirdparty.controller;

import jdk.management.resource.ResourceId;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pers.prover.mall.common.constant.RedisKeyConstant;
import pers.prover.mall.common.utils.R;
import pers.prover.mall.thirdparty.components.SmsComponent;
import pers.prover.mall.thirdparty.constant.ThirdPartyConstant;
import pers.prover.mall.thirdparty.utils.RandomCodeUtils;

import java.util.concurrent.TimeUnit;

/**
 * @author 小丶木曾义仲丶哈牛柚子露丶蛋卷
 * @version 1.0
 * @date 2022/6/9 19:09
 */
@RestController
@RequestMapping("/thirdparty/sms")
public class AliSmsController {

    @Autowired
    private SmsComponent smsComponent;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @GetMapping("/code/{phone}")
    public R sendPhoneCode(@PathVariable String phone) {
        String key = RedisKeyConstant.AUTH_REGISTER_PHONE_CODE + phone;
        // redis 查重
        String cachedCode = redisTemplate.opsForValue().get(key);
        if (!StringUtils.isBlank(cachedCode)) {
            throw new RuntimeException("请勿重复索取验证码!");
        }
        String randomCode = RandomCodeUtils.generateFourRandomCode();
        // 发送
        smsComponent.sendCode(phone, randomCode);
        // 保存到 redis
        redisTemplate.opsForValue().set(key, randomCode, Long.parseLong(ThirdPartyConstant.CODE_INVALID_TIME), TimeUnit.MINUTES);
        return R.ok();
    }


}
