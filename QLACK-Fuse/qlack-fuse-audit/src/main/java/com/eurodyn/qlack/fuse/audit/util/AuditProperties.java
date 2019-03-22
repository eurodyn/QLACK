package com.eurodyn.qlack.fuse.audit.util;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "qlack.fuse.audit")
public class AuditProperties {

  private boolean traceData;

  public boolean isTraceData() {
    return traceData;
  }

  public void setTraceData(boolean traceData) {
    this.traceData = traceData;
  }
}
