package pers.prover.mall.member.to;

import lombok.Data;

/**
 * 用户注册信息
 *
 * @author 小丶木曾义仲丶哈牛柚子露丶蛋卷
 * @version 1.0
 * @date 2022/6/9 19:45
 */
@Data
public class MemberRegisterTo {

    private String nickname;

    private String password;

    private String phone;

}
