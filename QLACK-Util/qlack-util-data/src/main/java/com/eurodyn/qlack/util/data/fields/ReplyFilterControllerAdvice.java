package com.eurodyn.qlack.util.data.fields;

import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.github.bohnman.squiggly.context.provider.SimpleSquigglyContextProvider;
import com.github.bohnman.squiggly.filter.SquigglyPropertyFilter;
import com.github.bohnman.squiggly.parser.SquigglyParser;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.AbstractMappingJacksonResponseBodyAdvice;

/**
 * A {@link ControllerAdvice} to process {@link ReplyFilter} annotations.
 */
@ControllerAdvice
public class ReplyFilterControllerAdvice extends AbstractMappingJacksonResponseBodyAdvice {

  @Override
  protected void beforeBodyWriteInternal(MappingJacksonValue bodyContainer, MediaType contentType,
      MethodParameter returnType, ServerHttpRequest req, ServerHttpResponse res) {
    ReplyFilter filter = returnType.getMethodAnnotation(ReplyFilter.class);
    if (filter != null) {

      SquigglyPropertyFilter propertyFilter = new SquigglyPropertyFilter(
          new SimpleSquigglyContextProvider(new SquigglyParser(), filter.value()));
      final SimpleFilterProvider filters = new SimpleFilterProvider()
          .addFilter(SquigglyPropertyFilter.FILTER_ID, propertyFilter);
      bodyContainer.setFilters(filters);
    }
  }
}
