package com.eurodyn.qlack.fuse.aaa.dto;

import java.io.Serializable;


/**
 * @author European Dynamics S.A.
 */
public class SessionAttributeDTO implements Serializable {

  private String id;
  private String name;
  private String value;
  private String sessionId;


  public String getName() {
    return name;
  }


  public void setName(String name) {
    this.name = name;
  }


  public String getValue() {
    return value;
  }


  public void setValue(String value) {
    this.value = value;
  }


  public String getId() {
    return id;
  }


  public void setId(String id) {
    this.id = id;
  }


  public String getSessionId() {
    return sessionId;
  }


  public void setSessionId(String sessionId) {
    this.sessionId = sessionId;
  }

}
