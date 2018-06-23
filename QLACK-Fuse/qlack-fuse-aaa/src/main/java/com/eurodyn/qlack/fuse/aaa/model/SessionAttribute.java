package com.eurodyn.qlack.fuse.aaa.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import java.io.Serializable;
import java.util.UUID;


/**
 * The persistent class for the aaa_session_attributes database table.
 */
@Entity
@Table(name = "aaa_session_attributes")
public class SessionAttribute implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  private String id;

  @Version
  private long dbversion;

  private String name;

  private String value;

  //bi-directional many-to-one association to Session
  @ManyToOne
  @JoinColumn(name = "session_id")
  private Session session;

  public SessionAttribute() {
    id = UUID.randomUUID().toString();
  }

  public String getId() {
    return this.id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getValue() {
    return this.value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public Session getSession() {
    return this.session;
  }

  public void setSession(Session session) {
    this.session = session;
  }

}