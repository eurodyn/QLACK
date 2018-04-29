package com.eurodyn.qlack.fuse.audit.util;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ConfigurationProperties(prefix = "qlack.fuse.audit")
@PropertySource("classpath:qlack.fuse.audit.application.properties")
public class AuditProperties {

  private boolean traceData;

  public boolean isTraceData() {
    return traceData;
  }

  public void setTraceData(boolean traceData) {
    this.traceData = traceData;
  }
}
