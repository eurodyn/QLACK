package com.eurodyn.qlack.fuse.aaa.dto;

import java.io.Serializable;

/**
 * @author European Dynamics SA
 */
public class QFAOperationDTO implements Serializable {

  private String id;
  private String name;
  private boolean dynamic;
  private String dynamicCode;
  private String description;

  public QFAOperationDTO() {
  }

  public QFAOperationDTO(String name) {
    this.name = name;
  }

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
   * @return the dynamic
   */
  public boolean isDynamic() {
    return dynamic;
  }

  /**
   * @param dynamic the dynamic to set
   */
  public void setDynamic(boolean dynamic) {
    this.dynamic = dynamic;
  }

  /**
   * @return the dynamicCode
   */
  public String getDynamicCode() {
    return dynamicCode;
  }

  /**
   * @param dynamicCode the dynamicCode to set
   */
  public void setDynamicCode(String dynamicCode) {
    this.dynamicCode = dynamicCode;
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
