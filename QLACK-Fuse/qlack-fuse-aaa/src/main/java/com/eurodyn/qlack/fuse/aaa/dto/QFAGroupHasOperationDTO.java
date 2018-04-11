package com.eurodyn.qlack.fuse.aaa.dto;

import java.io.Serializable;

public class QFAGroupHasOperationDTO implements Serializable {

  private String id;
  private QFAGroupDTO groupDTO;
  private QFAOperationDTO operationDTO;
  private QFAResourceDTO resourceDTO;

  private boolean deny;

  public QFAGroupDTO getGroupDTO() {
    return groupDTO;
  }

  public void setGroupDTO(QFAGroupDTO groupDTO) {
    this.groupDTO = groupDTO;
  }

  public QFAOperationDTO getOperationDTO() {
    return operationDTO;
  }

  public void setOperationDTO(QFAOperationDTO operationDTO) {
    this.operationDTO = operationDTO;
  }

  public QFAResourceDTO getResourceDTO() {
    return resourceDTO;
  }

  public void setResourceDTO(QFAResourceDTO resourceDTO) {
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
