package com.eurodyn.qlack.fuse.fileupload.util;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ConfigurationProperties(prefix = "qlack.fuse.fileupoad")
@PropertySource("classpath:qlack.fuse.fileupload.application.properties")
public class QFFIProperties {

  private boolean enableCleanup;
  private long cleanupThreshold;
  private String clamAV;

  public boolean isEnableCleanup() {
    return enableCleanup;
  }

  public void setEnableCleanup(boolean enableCleanup) {
    this.enableCleanup = enableCleanup;
  }

  public long getCleanupThreshold() {
    return cleanupThreshold;
  }

  public void setCleanupThreshold(long cleanupThreshold) {
    this.cleanupThreshold = cleanupThreshold;
  }

  public String getClamAV() {
    return clamAV;
  }

  public void setClamAV(String clamAV) {
    this.clamAV = clamAV;
  }
}
