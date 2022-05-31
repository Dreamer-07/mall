package pers.prover.mall.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 错误码和错误信息定义类
 *  规则：
 *      1. 错误码定义规则为5为数字
 *      2. 前两位表示业务场景，最后三位表示错误码。例如：100001。10:通用 001:系统未知异常
 *      3. 维护错误码后需要维护错误描述，将他们定义为枚举形式
 *  错误码列表：
 *      10: 通用
 *          000: 未知错误
 *          001: 参数校验错误
 *      11: 商品
 *      12: 订单
 *      13: 购物车
 *      14: 物流
 * @author 小丶木曾义仲丶哈牛柚子露丶蛋卷
 * @version 1.0
 * @date 2022/5/31 9:52
 */
@Getter
public enum ErrorCodeEnum {

    /**
     * 通用 - 系统未知异常
     */
    UNKNOWN_EXCEPTION(10000,"系统未知异常"),
    /**
     * 通用 - 参数格式校验失败
     */
    VALID_EXCEPTION(10001,"参数格式校验失败");


    private final int code;
    private final String message;

    private ErrorCodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

}
