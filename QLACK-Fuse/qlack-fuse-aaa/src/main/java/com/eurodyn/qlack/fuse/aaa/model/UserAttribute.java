package com.eurodyn.qlack.fuse.aaa.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import java.io.Serializable;
import java.util.UUID;


/**
 * The persistent class for the aaa_user_attributes database table.
 */
@Entity
@Table(name = "aaa_user_attributes")
public class UserAttribute implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  private String id;

  @Version
  private long dbversion;


  private byte[] bindata;

  @Column(name = "content_type")
  private String contentType;

  private String data;

  private String name;

  //bi-directional many-to-one association to User
  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  public UserAttribute() {
    id = UUID.randomUUID().toString();
  }

  public String getId() {
    return this.id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public byte[] getBindata() {
    return this.bindata;
  }

  public void setBindata(byte[] bindata) {
    this.bindata = bindata;
  }

  public String getContentType() {
    return this.contentType;
  }

  public void setContentType(String contentType) {
    this.contentType = contentType;
  }

  public String getData() {
    return this.data;
  }

  public void setData(String data) {
    this.data = data;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public User getUser() {
    return this.user;
  }

  public void setUser(User user) {
    this.user = user;
  }

}