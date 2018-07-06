package com.eurodyn.qlack.util.data.fields;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Stream;

/**
 * A utility aspect nullifying a list of fields within an object or a list of objects. Use this
 * aspect (and the respective {@link HideAllButField} annotation with care as reflection can be slow
 * in large datasets.
 *
 * This aspect is useful around REST endpoints where your back-end service may return more data that
 * you care to send to your front-end. Note that an alternative way to do such data hiding is using
 * JsonViews, however this aspect can be modified on a per-method level (where otherwise you would
 * require a large number of JsonViews).
 *
 * As fields do not magically disappear but are only set to 'null' values, you should have your JSON
 * mapper to ignore such fields when serialising your object for 'hiding' to properly work.
 */
@Aspect
@Component
public class HideAllButFieldAspect {

  private void hide(Object object, String[] fieldsToNotHide)
      throws NoSuchFieldException, IllegalAccessException {
    for (Field field : object.getClass().getDeclaredFields()) {
      if (Stream.of(fieldsToNotHide).noneMatch(o -> o.equals(field.getName()))) {
        HideFieldUtil.nullifyField(object, field.getName());
      }
    }
  }

  @Around("@annotation(hideAllButField)")
  public Object hide(ProceedingJoinPoint pjp, HideAllButField hideAllButField) throws Throwable {
    // Get result from original method.
    Object reply = pjp.proceed();
    Object data = reply;

    // Check if this is part of a Spring Boot {@link org.springframework.data.domain.Page} and in
    // that case extract the list of results.
    if (reply instanceof Page) {
      data = ((Page) reply).getContent();
    }

    // Check if this is a list or a single object and nullify all requested fields.
    if (data instanceof List) {
      List dataList = (List) data;
      for (Object dataListItem : dataList) {
        hide(dataListItem, hideAllButField.value());
      }
    } else { // Treat this as a generic object.
      hide(data, hideAllButField.value());
    }

    return reply;
  }
}