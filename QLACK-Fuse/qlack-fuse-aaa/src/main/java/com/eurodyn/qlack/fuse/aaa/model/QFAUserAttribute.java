/*
 * Copyright 2014 EUROPEAN DYNAMICS SA <info@eurodyn.com>
 *
 * Licensed under the EUPL, Version 1.1 only (the "License").
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 * https://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and
 * limitations under the Licence.
 */
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
public class QFAUserAttribute implements Serializable {

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

  //bi-directional many-to-one association to QFAUser
  @ManyToOne
  @JoinColumn(name = "user_id")
  private QFAUser user;

  public QFAUserAttribute() {
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

  public QFAUser getUser() {
    return this.user;
  }

  public void setUser(QFAUser user) {
    this.user = user;
  }

}