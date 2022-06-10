package pers.prover.mall.common.vo;

import lombok.Data;

/**
 * 用户认证实体类
 * @author 小丶木曾义仲丶哈牛柚子露丶蛋卷
 * @version 1.0
 * @date 2022/6/9 18:37
 */
@Data
public class MemberAuthInfoVo {

    private Long memberId;

    private String phone;

    private String password;

    private String nickname;

    private String header;

}
