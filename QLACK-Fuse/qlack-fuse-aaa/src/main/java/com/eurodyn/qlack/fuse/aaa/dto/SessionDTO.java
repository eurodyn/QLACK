package com.eurodyn.qlack.fuse.aaa.dto;

import java.io.Serializable;
import java.util.Set;

/**
 * This is transfer object for AaaSession entity.
 *
 * @author European Dynamics SA
 */
public class SessionDTO implements Serializable {

  private String id;
  private String userId;
  private long createdOn;
  private Long terminatedOn;
  private String applicationSessionID;
  private Set<SessionAttributeDTO> attributes;


  public long getCreatedOn() {
    return createdOn;
  }


  public void setCreatedOn(long createdOn) {
    this.createdOn = createdOn;
  }


  public Long getTerminatedOn() {
    return terminatedOn;
  }


  public void setTerminatedOn(Long terminatedOn) {
    this.terminatedOn = terminatedOn;
  }


  public String getUserId() {
    return userId;
  }


  public void setUserId(String userId) {
    this.userId = userId;
  }


  public Set<SessionAttributeDTO> getAttributes() {
    return attributes;
  }


  public void setAttributes(Set<SessionAttributeDTO> attributes) {
    this.attributes = attributes;
  }

  /**
   * @return the applicationSessionID
   */
  public String getApplicationSessionID() {
    return applicationSessionID;
  }

  /**
   * @param applicationSessionID the applicationSessionID to set
   */
  public void setApplicationSessionID(String applicationSessionID) {
    this.applicationSessionID = applicationSessionID;
  }


  public String getId() {
    return id;
  }


  public void setId(String id) {
    this.id = id;
  }
}