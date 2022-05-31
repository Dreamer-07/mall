package pers.prover.mall.product.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pers.prover.mall.common.enums.ErrorCodeEnum;
import pers.prover.mall.common.utils.R;

import java.util.HashMap;
import java.util.List;

/**
 * 全局异常捕获
 *
 * @author 小丶木曾义仲丶哈牛柚子露丶蛋卷
 * @version 1.0
 * @date 2022/5/31 9:59
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理参数校验异常
     * @param exception
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        // 获取错误的参数字段
        BindingResult bindingResult = exception.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        // 将参数字段和信息封装
        HashMap<String, String> dataMap = new HashMap<>(fieldErrors.size());
        fieldErrors.forEach(fieldError -> {
            dataMap.put(fieldError.getField(), fieldError.getDefaultMessage());
        });
        // 返回
        return R.error(ErrorCodeEnum.VALID_EXCEPTION).put("data", dataMap);
    }

    /**
     * 处理全局异常
     * @param throwable
     * @return
     */
    @ExceptionHandler(Throwable.class)
    public R handleException(Throwable throwable) {

        log.error("出现新的全局异常: 异常类型 - {}, 异常信息 - {}", throwable.getClass(), throwable.getMessage());
        return R.error(ErrorCodeEnum.UNKNOWN_EXCEPTION);
    }
}
