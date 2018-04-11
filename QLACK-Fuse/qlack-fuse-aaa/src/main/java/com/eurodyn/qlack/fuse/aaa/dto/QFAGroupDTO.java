package com.eurodyn.qlack.fuse.aaa.dto;

import java.io.Serializable;
import java.util.Set;

/**
 * @author European Dynamics SA
 */
public class QFAGroupDTO implements Serializable {

  private String id;
  private String name;
  private String objectID;
  private String description;
  private QFAGroupDTO parent;
  private Set<QFAGroupDTO> children;

  public QFAGroupDTO() {
  }

  public QFAGroupDTO(String id) {
    this.id = id;
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

  public QFAGroupDTO getParent() {
    return parent;
  }

  public void setParent(QFAGroupDTO parent) {
    this.parent = parent;
  }

  public Set<QFAGroupDTO> getChildren() {
    return children;
  }

  public void setChildren(Set<QFAGroupDTO> children) {
    this.children = children;
  }
}
