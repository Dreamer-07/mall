package pers.prover.mall.thirdparty.components;

import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pers.prover.mall.thirdparty.config.properties.AliCloudSmsConfigProperties;
import pers.prover.mall.thirdparty.constant.ThirdPartyConstant;
import pers.prover.mall.thirdparty.utils.HttpUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 小丶木曾义仲丶哈牛柚子露丶蛋卷
 * @version 1.0
 * @date 2022/6/9 19:10
 */
@Component
public class SmsComponent {

    @Autowired
    private AliCloudSmsConfigProperties aliCloudSmsConfigProperties;

    /**
     * 向指定手机发送验证码
     * @param phone
     * @param code
     */
    public void sendCode(String phone, String code) {
        String host = aliCloudSmsConfigProperties.getHost();
        String path = aliCloudSmsConfigProperties.getPath();
        String method = "POST";
        String appcode = aliCloudSmsConfigProperties.getAppCode();
        Map<String, String> headers = new HashMap<>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<>();
        querys.put("mobile", phone);
        querys.put("param", String.format("**code**:%s,**minute**:%s", code, ThirdPartyConstant.CODE_INVALID_TIME));
        querys.put("smsSignId", aliCloudSmsConfigProperties.getSmsSignId());
        querys.put("templateId", aliCloudSmsConfigProperties.getTemplateId());
        Map<String, String> bodys = new HashMap<>();


        try {
            /**
             * 重要提示如下:
             * HttpUtils请从
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
             * 下载
             *
             * 相应的依赖请参照
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
             */
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            System.out.println(response.toString());
            //获取response的body
            //System.out.println(EntityUtils.toString(response.getEntity()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
