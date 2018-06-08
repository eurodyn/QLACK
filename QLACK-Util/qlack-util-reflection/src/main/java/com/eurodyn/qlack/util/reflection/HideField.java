package com.eurodyn.qlack.util.reflection;

import static java.lang.annotation.ElementType.METHOD;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation to use together with {@link HideFieldAspect}.
 */
@Target(METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface HideField {

  String[] value();
}
