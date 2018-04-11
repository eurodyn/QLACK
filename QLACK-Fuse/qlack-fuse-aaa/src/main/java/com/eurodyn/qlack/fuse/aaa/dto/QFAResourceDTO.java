package com.eurodyn.qlack.fuse.aaa.dto;

import java.io.Serializable;

/**
 * @author European Dynamics
 */
public class QFAResourceDTO implements Serializable {

  private String id;
  private String name;
  private String description;
  private String objectID;

  /**
   * @return the id
   */
  public String getId() {
    return id;
  }

  /**
   * @param id the id to set
   */
  public void setId(String id) {
    this.id = id;
  }

  /**
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * @param name the name to set
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * @return the objectID
   */
  public String getObjectID() {
    return objectID;
  }

  /**
   * @param objectID the objectID to set
   */
  public void setObjectID(String objectID) {
    this.objectID = objectID;
  }

  /**
   * @return the description
   */
  public String getDescription() {
    return description;
  }

  /**
   * @param description the description to set
   */
  public void setDescription(String description) {
    this.description = description;
  }
}
