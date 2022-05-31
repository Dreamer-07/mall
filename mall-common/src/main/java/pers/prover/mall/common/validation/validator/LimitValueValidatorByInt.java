package pers.prover.mall.common.validation.validator;

import pers.prover.mall.common.validation.anno.LimitValueValid;
import pers.prover.mall.common.validation.anno.UpdateStrValid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.*;

/**
 * 校验 {@code LimitValueValidator}
 * @author 小丶木曾义仲丶哈牛柚子露丶蛋卷
 * @version 1.0
 * @date 2022/5/31 10:37
 */
public class LimitValueValidatorByInt implements ConstraintValidator<LimitValueValid,Integer> {


    private final HashSet<Integer> values = new HashSet<>();

    @Override
    public void initialize(LimitValueValid constraintAnnotation) {
        int[] nums = constraintAnnotation.value();
        for (int num : nums) {
            values.add(num);
        }
    }

    @Override
    public boolean isValid(Integer i, ConstraintValidatorContext constraintValidatorContext) {
        if (i == null) {
            return true;
        }
        return values.contains(i);
    }

}
