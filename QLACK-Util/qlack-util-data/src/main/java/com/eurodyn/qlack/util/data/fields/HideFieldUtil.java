package com.eurodyn.qlack.util.data.fields;

import org.springframework.beans.BeanUtils;

import java.lang.reflect.Field;

public class HideFieldUtil {
  /**
   * Sets a named field to null. This is a pretty basic implementation only taking care of simple
   * cases. A better alternative based on SpEL (or similar) should be implemented in the future.
   *
   * @param object The object to process.
   * @param fieldName The name of the field to set to null.
   */
  public static void nullifyField(Object object, String fieldName)
      throws NoSuchFieldException, IllegalAccessException {

    // Get a reference to the field to nullify.
    final Field field = object.getClass().getDeclaredField(fieldName);

    // Skip primitive types since they can not be set to null.
    if (!BeanUtils.isSimpleProperty(field.getClass())) {
      return;
    }

    // Make the field accessible.
    final boolean isAccessible = field.isAccessible();
    if (!isAccessible) {
      field.setAccessible(true);
    }

    // Nullify field.
    field.set(object, null);

    // Return field's accessibility back to its original value.
    field.setAccessible(isAccessible);
  }

}
