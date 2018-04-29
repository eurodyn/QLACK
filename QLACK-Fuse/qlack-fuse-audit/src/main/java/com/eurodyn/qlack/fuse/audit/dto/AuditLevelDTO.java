package com.eurodyn.qlack.fuse.audit.dto;

import java.io.Serializable;
import java.util.Date;

public class AuditLevelDTO implements Serializable {

  private String name;
  private String id;
  private String description;
  private String prinSessionId;
  private Date createdOn;

  /**
   * Default constructor
   */
  public AuditLevelDTO() {

  }

  /**
   * parameterized Constructor
   */
  public AuditLevelDTO(String name) {
    this.setName(name);
  }

  /**
   * Get the audit level description
   */
  public String getDescription() {
    return description;
  }

  /**
   * Set the audit level description
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * get id
   *
   * @return id
   */
  public String getId() {
    return id;
  }

  /**
   * set the id
   */
  public void setId(String id) {
    this.id = id;
  }

  /**
   * get name
   *
   * @return name
   */
  public String getName() {
    return name;
  }

  /**
   * set the name
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * get principal session id
   */
  public String getPrinSessionId() {
    return prinSessionId;
  }

  /**
   * set principal id
   */
  public void setPrinSessionId(String prinSessionId) {
    this.prinSessionId = prinSessionId;
  }

  /**
   * Get the creation date of the level
   */
  public Date getCreatedOn() {
    return createdOn;
  }

  /**
   * Set the creation date of the level
   */
  public void setCreatedOn(Date createdOn) {
    this.createdOn = createdOn;
  }
}
