package com.eurodyn.qlack.fuse.aaa.dto;

import java.io.Serializable;

public class GroupHasOperationDTO implements Serializable {

  private String id;
  private GroupDTO groupDTO;
  private OperationDTO operationDTO;
  private ResourceDTO resourceDTO;

  private boolean deny;

  public GroupDTO getGroupDTO() {
    return groupDTO;
  }

  public void setGroupDTO(GroupDTO groupDTO) {
    this.groupDTO = groupDTO;
  }

  public OperationDTO getOperationDTO() {
    return operationDTO;
  }

  public void setOperationDTO(OperationDTO operationDTO) {
    this.operationDTO = operationDTO;
  }

  public ResourceDTO getResourceDTO() {
    return resourceDTO;
  }

  public void setResourceDTO(ResourceDTO resourceDTO) {
    this.resourceDTO = resourceDTO;
  }

  public boolean isDeny() {
    return deny;
  }

  public void setDeny(boolean deny) {
    this.deny = deny;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }
}
