package pers.prover.mall.common.validation.anno;

import pers.prover.mall.common.validation.validator.LimitValueValidatorByInt;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 限定 number 的值
 */
@Documented
@Constraint(validatedBy = {LimitValueValidatorByInt.class})
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface LimitValueValid {

    String message() default "{pers.prover.mall.common.validation.anno.LimitValue.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    int[] value() default {};

}
