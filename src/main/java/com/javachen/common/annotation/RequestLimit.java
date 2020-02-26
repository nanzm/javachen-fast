package com.javachen.common.annotation;

import java.lang.annotation.*;

/**
 * 限流注释
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestLimit {
}
