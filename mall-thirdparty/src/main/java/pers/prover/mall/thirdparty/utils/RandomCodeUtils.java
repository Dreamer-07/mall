package pers.prover.mall.thirdparty.utils;

import java.security.SecureRandom;
import java.util.Random;

/**
 * @author 小丶木曾义仲丶哈牛柚子露丶蛋卷
 * @version 1.0
 * @date 2022/6/9 19:14
 */
public class RandomCodeUtils {

    private static final String RANDOM_CODE = "0123456789";

    private static final Random RANDOM = new SecureRandom();


    /**
     * 生成4位的随机数
     * @return
     */
    public static String generateFourRandomCode() {

        // 初始化6位字符数组
        char[] code = new char[4];

        for (int index = 0; index < code.length; index++) {
            code[index] = RANDOM_CODE.charAt(RANDOM.nextInt(RANDOM_CODE.length()));
        }

        return new String(code);
    }

}
