package com.eurodyn.qlack.fuse.mailing.dto;

/**
 * Base Data transfer object for Mailing.
 *
 * @author European Dynamics SA.
 */
public class MailBaseDTO extends BaseDTO {

  private String id;

  @Override
  public String getId() {
    return id;
  }

  @Override
  public void setId(String id) {
    this.id = id;
  }
}
