package com.eurodyn.qlack.fuse.aaa.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;


/**
 * The persistent class for the aaa_session database table.
 */
@Entity
@Table(name = "aaa_session")
public class Session implements Serializable {

  @Id
  private String id;

  @Version
  private long dbversion;

  @Column(name = "application_session_id")
  private String applicationSessionId;

  @Column(name = "created_on")
  private long createdOn;

  @Column(name = "terminated_on")
  private Long terminatedOn;

  //bi-directional many-to-one association to User
  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  //bi-directional many-to-one association to SessionAttribute
  @OneToMany(mappedBy = "session")
  private List<SessionAttribute> sessionAttributes;

  public Session() {
    id = UUID.randomUUID().toString();
  }
  
  public String getId() {
    return this.id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getApplicationSessionId() {
    return this.applicationSessionId;
  }

  public void setApplicationSessionId(String applicationSessionId) {
    this.applicationSessionId = applicationSessionId;
  }

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

  public User getUser() {
    return this.user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public List<SessionAttribute> getSessionAttributes() {
    return this.sessionAttributes;
  }

  public void setSessionAttributes(List<SessionAttribute> sessionAttributes) {
    this.sessionAttributes = sessionAttributes;
  }

}