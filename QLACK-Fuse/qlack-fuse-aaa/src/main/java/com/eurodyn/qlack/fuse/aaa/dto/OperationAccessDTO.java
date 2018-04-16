package com.eurodyn.qlack.fuse.aaa.dto;

import java.io.Serializable;

public class OperationAccessDTO implements Serializable {

  private OperationDTO operation;
  private ResourceDTO resource;
  private boolean deny;

  public OperationDTO getOperation() {
    return operation;
  }

  public void setOperation(OperationDTO operation) {
    this.operation = operation;
  }

  public ResourceDTO getResource() {
    return resource;
  }

  public void setResource(ResourceDTO resource) {
    this.resource = resource;
  }

  public boolean isDeny() {
    return deny;
  }

  public void setDeny(boolean deny) {
    this.deny = deny;
  }
}
