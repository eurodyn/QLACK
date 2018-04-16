package com.eurodyn.qlack.fuse.aaa.dto;

import java.io.Serializable;
import java.util.Set;

/**
 * @author European Dynamics SA
 */
public class GroupDTO implements Serializable {

  private String id;
  private String name;
  private String objectID;
  private String description;
  private GroupDTO parent;
  private Set<GroupDTO> children;

  public GroupDTO() {
  }

  public GroupDTO(String id) {
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

  public GroupDTO getParent() {
    return parent;
  }

  public void setParent(GroupDTO parent) {
    this.parent = parent;
  }

  public Set<GroupDTO> getChildren() {
    return children;
  }

  public void setChildren(Set<GroupDTO> children) {
    this.children = children;
  }
}
