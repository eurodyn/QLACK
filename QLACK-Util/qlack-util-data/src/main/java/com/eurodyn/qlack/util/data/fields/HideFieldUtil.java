package com.eurodyn.qlack.util.data.fields;

import java.lang.reflect.Field;

public class HideFieldUtil {
  /**
   * Sets a named field to null.
   *
   * @param object The object to process.
   * @param fieldName The name of the field to set to null.
   */
  public static void nullifyField(Object object, String fieldName)
      throws NoSuchFieldException, IllegalAccessException {
    // Get a reference to the field to nullify.
    final Field field = object.getClass().getDeclaredField(fieldName);

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
