package com.eurodyn.qlack.fuse.mailing.dto;

import java.io.Serializable;

public class BaseDTO extends AttributeDTO implements Serializable {

  private String id;

  /**
   * This user id is used to identify the user calling an action in order to use it when generating
   * notification messages or when this information should be stored along with the relevant item in
   * the db. Please note that it should *not* be used for security checks.
   */
  private String srcUserId;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getSrcUserId() {
    return srcUserId;
  }

  public void setSrcUserId(String srcUserId) {
    this.srcUserId = srcUserId;
  }
}
