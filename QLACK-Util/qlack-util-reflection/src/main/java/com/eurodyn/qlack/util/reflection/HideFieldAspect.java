package com.eurodyn.qlack.util.reflection;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.List;

/**
 * A utility aspect nullifying a list of fields within an object or a list of objects. Use this
 * aspect (and the respective {@link HideField} annotation with care as reflection can be slow in
 * large datasets.
 */
@Aspect
@Component
public class HideFieldAspect {

  private Logger logger = LoggerFactory.getLogger(this.getClass());

  /**
   * Sets a named field to null.
   *
   * @param object The object to process.
   * @param fieldName The name of the field to set to null.
   */
  private void nullifyField(Object object, String fieldName)
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

  @Around("@annotation(hideField)")
  public Object test(ProceedingJoinPoint pjp, HideField hideField) throws Throwable {
    // Get result from original method.
    Object reply = pjp.proceed();
    Object data = reply;

    // Check if this is part of a Spring Boot {@link org.springframework.data.domain.Page} and in
    // that case extract the list of results.
    if (reply instanceof Page) {
      data = ((Page)reply).getContent();
    }

    // Check if this is a list or a single object and nullify all requested fields.
    if (data instanceof List) {
      List dataList = (List) data;
      for (Object dataListItem : dataList) {
        for (String fieldToNullify : hideField.value()) {
          nullifyField(dataListItem, fieldToNullify);
        }
      }
    } else { // Treat this as a generic object.
      for (String fieldToNullify : hideField.value()) {
        nullifyField(data, fieldToNullify);
      }
    }

    return reply;
  }
}