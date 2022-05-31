package pers.prover.mall.common.validation.validator;

import pers.prover.mall.common.validation.anno.UpdateStrValid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 校验 {@code UpdateStrValid}
 * @author 小丶木曾义仲丶哈牛柚子露丶蛋卷
 * @version 1.0
 * @date 2022/5/31 10:32
 */
public class UpdateStrValidator implements ConstraintValidator<UpdateStrValid,String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (s == null) {
            return true;
        }
        return s.trim().length() > 0;
    }
}
