package com.javachen.web.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = UniqueAdValidator.class)
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueAd {

    String message() default "{io.github.hwestphal.todo.validation.UniqueTodo.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
