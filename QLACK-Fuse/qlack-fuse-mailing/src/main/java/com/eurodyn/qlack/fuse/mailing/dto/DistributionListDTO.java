package com.eurodyn.qlack.fuse.mailing.dto;

import java.util.List;

/**
 * Data transfer object for distribution lists.
 *
 * @author European Dynamics SA.
 */
public class DistributionListDTO extends MailBaseDTO {

  private String name;
  private String description;
  private List<ContactDTO> contacts;

  private String createdBy;
  private Long createdOn;

  // -- Constructors

  public DistributionListDTO() {
  }

  // -- Accessors

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public List<ContactDTO> getContacts() {
    return contacts;
  }

  public void setContacts(List<ContactDTO> contacts) {
    this.contacts = contacts;
  }

  public String getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  public Long getCreatedOn() {
    return createdOn;
  }

  public void setCreatedOn(Long createdOn) {
    this.createdOn = createdOn;
  }
}
