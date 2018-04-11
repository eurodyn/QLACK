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
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Query;
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
public class QFASession implements Serializable {

  private static final long serialVersionUID = 1L;

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

  //bi-directional many-to-one association to QFAUser
  @ManyToOne
  @JoinColumn(name = "user_id")
  private QFAUser user;

  //bi-directional many-to-one association to QFASessionAttribute
  @OneToMany(mappedBy = "session")
  private List<QFASessionAttribute> sessionAttributes;

  public QFASession() {
    id = UUID.randomUUID().toString();
  }

  public static QFASession find(String sessionID, EntityManager em) {
    return em.find(QFASession.class, sessionID);
  }

  public static QFASessionAttribute findAttribute(String sessionId, String attributeName,
      EntityManager em) {
    QFASessionAttribute retVal = null;
    Query query = em.createQuery("SELECT a FROM QFASessionAttribute a "
        + "WHERE a.session.id = :id AND a.name = :name");
    query.setParameter("id", sessionId);
    query.setParameter("name", attributeName);
    List<QFASessionAttribute> l = query.getResultList();
    if (!l.isEmpty()) {
      retVal = (QFASessionAttribute) l.get(0);
    }

    return retVal;
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

  public QFAUser getUser() {
    return this.user;
  }

  public void setUser(QFAUser user) {
    this.user = user;
  }

  public List<QFASessionAttribute> getSessionAttributes() {
    return this.sessionAttributes;
  }

  public void setSessionAttributes(List<QFASessionAttribute> sessionAttributes) {
    this.sessionAttributes = sessionAttributes;
  }

}