package pers.prover.mall.auth.vo;

import lombok.Data;

/**
 * Base Auth(基础登录)时封装用户的实体类
 * @author 小丶木曾义仲丶哈牛柚子露丶蛋卷
 * @version 1.0
 * @date 2022/6/9 15:47
 */
@Data
public class BaseAuthVo {

    private String phone;

    private String password;

}
