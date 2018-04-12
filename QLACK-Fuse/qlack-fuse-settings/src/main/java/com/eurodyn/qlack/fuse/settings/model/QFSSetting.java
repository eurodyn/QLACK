package com.eurodyn.qlack.fuse.settings.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "set_setting")
public class QFSSetting implements java.io.Serializable {

  @Id
  private String id;

  @Version
  private long dbversion;

  private String owner;

  @Column(name = "group_name")
  private String group;

  @Column(name = "key_name")
  private String key;

  private String val;

  @Column(name = "sensitivity")
  private boolean sensitive;

  @Column(name = "psswrd")
  private Boolean password;

  @Column(name = "created_on")
  private long createdOn;

  public QFSSetting() {
    this.id = UUID.randomUUID().toString();
    this.createdOn = Instant.now().toEpochMilli();
  }

  @Override
  public String toString() {
    return "QFSSetting [id=" + id + ", dbversion=" + dbversion + ", owner=" + owner + ", group="
        + group + ", key="
        + key + ", val=" + val + ", createdOn=" + createdOn + "]";
  }

  public boolean isSensitive() {
    return sensitive;
  }

  public void setSensitive(boolean sensitive) {
    this.sensitive = sensitive;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public long getDbversion() {
    return dbversion;
  }

  public void setDbversion(long dbversion) {
    this.dbversion = dbversion;
  }

  public String getOwner() {
    return owner;
  }

  public void setOwner(String owner) {
    this.owner = owner;
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

  public String getGroup() {
    return group;
  }

  public void setGroup(String group) {
    this.group = group;
  }

  /**
   * @return the password
   */
  public Boolean isPassword() {
    return password;
  }

  /**
   * @param password the password to set
   */
  public void setPassword(Boolean password) {
    this.password = password;
  }

}
