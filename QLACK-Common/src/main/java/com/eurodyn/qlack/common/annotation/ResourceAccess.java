package com.eurodyn.qlack.fuse.security.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author European Dynamics
 */

/***
 * Describes access
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ResourceAccess {

    String[] roleAccess() default {};
    ResourceOperation[] operations() default {};

}
