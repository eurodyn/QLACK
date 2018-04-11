package com.eurodyn.qlack.fuse.aaa.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import java.io.Serializable;

@Entity
@Table(name = "aaa_verification")
public class QFAVerificationToken implements Serializable {

  private static final long serialVersionUID = 2487733740070965552L;

  @Id
  private String id;

  @Column(name = "created_on")
  private long createdOn;

  @Column(name = "expires_on")
  private long expiresOn;

  private String data;

  // bi-directional many-to-one association to QFAUser.
  @ManyToOne
  @JoinColumn(name = "user_id")
  private QFAUser user;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public long getCreatedOn() {
    return createdOn;
  }

  public void setCreatedOn(long createdOn) {
    this.createdOn = createdOn;
  }

  public long getExpiresOn() {
    return expiresOn;
  }

  public void setExpiresOn(long expiresOn) {
    this.expiresOn = expiresOn;
  }

  public String getData() {
    return data;
  }

  public void setData(String data) {
    this.data = data;
  }

  public QFAUser getUser() {
    return user;
  }

  public void setUser(QFAUser user) {
    this.user = user;
  }
}
