package pers.prover.mall.cart.bo;

import lombok.Data;

/**
 * 标识购物车数据的用户数据体
 * @author 小丶木曾义仲丶哈牛柚子露丶蛋卷
 * @version 1.0
 * @date 2022/6/11 16:16
 */
@Data
public class UserKeyBo {

    /**
     * 登录用户的标识
     */
    private String userId;

    /**
     * 为登录用户的标识
     */
    private String userKey;

    /**
     * 判断用户是否为临时用户(未登录 + 未持有 userkey)
     */
    private Boolean isTempUser = false;

}
