package com.eurodyn.qlack.fuse.search.util;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "qlack.fuse.search")
@PropertySource("classpath:qlack.fuse.search.application.properties")
public class Properties {

  private String esHosts;
  private String esUsername;
  private String esPassword;
  private boolean verifyHostname;
}
