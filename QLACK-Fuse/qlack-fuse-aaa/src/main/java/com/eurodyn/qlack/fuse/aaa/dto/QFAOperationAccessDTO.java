package com.eurodyn.qlack.fuse.aaa.dto;

import java.io.Serializable;

public class QFAOperationAccessDTO implements Serializable {

  private QFAOperationDTO operation;
  private QFAResourceDTO resource;
  private boolean deny;

  public QFAOperationDTO getOperation() {
    return operation;
  }

  public void setOperation(QFAOperationDTO operation) {
    this.operation = operation;
  }

  public QFAResourceDTO getResource() {
    return resource;
  }

  public void setResource(QFAResourceDTO resource) {
    this.resource = resource;
  }

  public boolean isDeny() {
    return deny;
  }

  public void setDeny(boolean deny) {
    this.deny = deny;
  }
}
