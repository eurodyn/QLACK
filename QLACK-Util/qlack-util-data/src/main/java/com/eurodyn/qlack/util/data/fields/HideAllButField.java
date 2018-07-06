package com.eurodyn.qlack.util.data.fields;

import static java.lang.annotation.ElementType.METHOD;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation to use together with {@link HideAllButFieldAspect}.
 */
@Target(METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface HideAllButField {
  // The list of fields to hide.
  String[] value();
}
