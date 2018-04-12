package com.eurodyn.qlack.fuse.mailing.util;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ConfigurationProperties(prefix = "qlack.fuse.mailing")
@PropertySource("classpath:qlack.fuse.mailing.application.properties")
public class QFMailingProperties {

  private byte maxTries;
  private boolean debug;
  private String serverHost;
  private int serverPort;
  private String serverUsername;
  private String serverPassword;
  private boolean startTls;

  public boolean isDebug() {
    return debug;
  }

  public void setDebug(boolean debug) {
    this.debug = debug;
  }

  public String getServerHost() {
    return serverHost;
  }

  public void setServerHost(String serverHost) {
    this.serverHost = serverHost;
  }

  public int getServerPort() {
    return serverPort;
  }

  public void setServerPort(int serverPort) {
    this.serverPort = serverPort;
  }

  public String getServerUsername() {
    return serverUsername;
  }

  public void setServerUsername(String serverUsername) {
    this.serverUsername = serverUsername;
  }

  public String getServerPassword() {
    return serverPassword;
  }

  public void setServerPassword(String serverPassword) {
    this.serverPassword = serverPassword;
  }

  public boolean isStartTls() {
    return startTls;
  }

  public void setStartTls(boolean startTls) {
    this.startTls = startTls;
  }

  public byte getMaxTries() {
    return maxTries;
  }

  public void setMaxTries(byte maxTries) {
    this.maxTries = maxTries;
  }
}
