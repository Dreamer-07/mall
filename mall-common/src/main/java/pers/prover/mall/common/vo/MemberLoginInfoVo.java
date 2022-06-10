package pers.prover.mall.common.vo;

import lombok.Data;
import lombok.ToString;

/**
 * 用户登录信息
 * @author 小丶木曾义仲丶哈牛柚子露丶蛋卷
 * @version 1.0
 * @date 2022/6/9 15:57
 */
@Data
@ToString
public class MemberLoginInfoVo {

    private Long memberId;

    private String nickname;

    private String header;

}
