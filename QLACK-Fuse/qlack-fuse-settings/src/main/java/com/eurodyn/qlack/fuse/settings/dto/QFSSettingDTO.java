package com.eurodyn.qlack.fuse.settings.dto;

import java.io.Serializable;

/**
 * @author European Dynamics SA
 */
public class QFSSettingDTO implements Serializable {

  private static final long serialVersionUID = -1745622761507844077L;
  private String id;
  private String owner;
  private String group;
  private String key;
  private String val;
  private long createdOn;
  private boolean sensitive;
  private boolean password;

  public QFSSettingDTO() {
  }

  public QFSSettingDTO(String key, String val) {
    this.key = key;
    this.val = val;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getOwner() {
    return owner;
  }

  public void setOwner(String owner) {
    this.owner = owner;
  }

  public String getGroup() {
    return group;
  }

  public void setGroup(String group) {
    this.group = group;
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public String getVal() {
    return val;
  }

  public void setVal(String val) {
    this.val = val;
  }

  public long getCreatedOn() {
    return createdOn;
  }

  public void setCreatedOn(long createdOn) {
    this.createdOn = createdOn;
  }

  public boolean isSensitive() {
    return sensitive;
  }

  public void setSensitive(boolean sensitive) {
    this.sensitive = sensitive;
  }

  /**
   * @return the password
   */
  public boolean isPassword() {
    return password;
  }

  /**
   * @param password the password to set
   */
  public void setPassword(boolean password) {
    this.password = password;
  }
}
