package com.eurodyn.qlack.fuse.aaa.dto;

import java.io.Serializable;
import java.util.Set;

/**
 * @author European Dynamics SA
 */
public class QFAOpTemplateDTO implements Serializable {

  private String id;
  private String name;
  private String description;
  private String createdBy;
  private Set<QFAOperationAccessDTO> operations;

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

  /**
   * @return the createdBy
   */
  public String getCreatedBy() {
    return createdBy;
  }

  /**
   * @param createdBy the createdBy to set
   */
  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  public Set<QFAOperationAccessDTO> getOperations() {
    return operations;
  }

  public void setOperations(Set<QFAOperationAccessDTO> operations) {
    this.operations = operations;
  }
}
