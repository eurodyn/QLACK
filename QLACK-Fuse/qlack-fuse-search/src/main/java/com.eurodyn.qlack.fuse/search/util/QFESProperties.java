package com.eurodyn.qlack.fuse.search.util;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ConfigurationProperties(prefix = "qlack.fuse.search")
@PropertySource("classpath:qlack.fuse.search.application.properties")
public class QFESProperties {

  private String esHosts;
  private String esUsername;
  private String esPassword;
  private boolean verifyHostname;

  public String getEsHosts() {
    return esHosts;
  }

  public void setEsHosts(String esHosts) {
    this.esHosts = esHosts;
  }

  public String getEsUsername() {
    return esUsername;
  }

  public void setEsUsername(String esUsername) {
    this.esUsername = esUsername;
  }

  public String getEsPassword() {
    return esPassword;
  }

  public void setEsPassword(String esPassword) {
    this.esPassword = esPassword;
  }

  public boolean isVerifyHostname() {
    return verifyHostname;
  }

  public void setVerifyHostname(boolean verifyHostname) {
    this.verifyHostname = verifyHostname;
  }
}
