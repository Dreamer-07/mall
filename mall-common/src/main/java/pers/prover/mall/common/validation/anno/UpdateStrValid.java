package pers.prover.mall.common.validation.anno;

import pers.prover.mall.common.validation.validator.UpdateStrValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * update 时有关 str 的校验
 */
@Documented
@Constraint(validatedBy = {UpdateStrValidator.class})
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface UpdateStrValid {

    String message() default "{pers.prover.mall.common.validation.anno.UpdateStrValid.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
