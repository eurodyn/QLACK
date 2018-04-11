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
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Query;
import javax.persistence.Table;
import javax.persistence.Version;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;


/**
 * The persistent class for the aaa_user database table.
 */
@Entity
@Table(name = "aaa_user")
public class QFAUser implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  private String id;

  @Version
  private long dbversion;

  @Column(name = "pswd")
  private String password;

  private String salt;

  private byte status;

  private String username;

  private boolean superadmin;

  /**
   * An indicator that this user's password is not held in the database of AAA.
   */
  private Boolean external = false;

  //bi-directional many-to-one association to QFAUserHasOperation
  @OneToMany(mappedBy = "user")
  private List<QFAUserHasOperation> userHasOperations;

  //bi-directional many-to-one association to QFASession
  @OneToMany(mappedBy = "user")
  private List<QFASession> sessions;

  //bi-directional many-to-many association to QFAGroup
  @ManyToMany(mappedBy = "users")
  private List<QFAGroup> groups;

  //bi-directional many-to-one association to QFAUserAttribute
  @OneToMany(mappedBy = "user")
  private List<QFAUserAttribute> userAttributes;

  // bi-directional many-to-one association to QFAVerificationToken.
  @OneToMany(mappedBy = "user")
  private List<QFAVerificationToken> verificationTokens;

  public QFAUser() {
    id = UUID.randomUUID().toString();
  }

  public static QFAUser find(String userID, EntityManager em) {
    return em.find(QFAUser.class, userID);
  }

  public static QFAUser findByUsername(String username, EntityManager em) {
    Query query = em.createQuery(
        "SELECT u FROM QFAUser u WHERE u.username = :username");
    query.setParameter("username", username);
    List<QFAUser> resultList = query.getResultList();

    return resultList.isEmpty() ? null : resultList.get(0);
  }

  public static QFAUserAttribute findAttribute(String userId, String attributeName, EntityManager em) {
    QFAUserAttribute retVal = null;
    Query query = em.createQuery("SELECT a FROM QFAUserAttribute a "
        + "WHERE a.user.id = :id AND a.name = :name");
    query.setParameter("id", userId);
    query.setParameter("name", attributeName);
    List<QFAUserAttribute> l = query.getResultList();
    if (!l.isEmpty()) {
      retVal = l.get(0);
    }

    return retVal;
  }

  public static Set<String> getAllUserIds(EntityManager em) {
    Set<String> retVal = new HashSet<>();
    Query query = em.createQuery("SELECT u.id FROM QFAUser u");
    retVal.addAll(query.getResultList());
    return retVal;
  }

  public static Set<String> getNormalUserIds(EntityManager em) {
    Set<String> retVal = new HashSet<>();
    Query query = em.createQuery("SELECT u.id FROM QFAUser u WHERE u.superadmin = false");
    retVal.addAll(query.getResultList());
    return retVal;
  }

  public static Set<String> getSuperadminUserIds(EntityManager em) {
    Set<String> retVal = new HashSet<>();
    Query query = em.createQuery("SELECT u.id FROM QFAUser u WHERE u.superadmin = true");
    retVal.addAll(query.getResultList());
    return retVal;
  }

  public String getId() {
    return this.id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getPassword() {
    return this.password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getSalt() {
    return salt;
  }

  public void setSalt(String salt) {
    this.salt = salt;
  }

  public byte getStatus() {
    return this.status;
  }

  public void setStatus(byte status) {
    this.status = status;
  }

  public String getUsername() {
    return this.username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public List<QFAUserHasOperation> getUserHasOperations() {
    return this.userHasOperations;
  }

  public void setUserHasOperations(List<QFAUserHasOperation> userHasOperations) {
    this.userHasOperations = userHasOperations;
  }

  public boolean isSuperadmin() {
    return superadmin;
  }

  public void setSuperadmin(boolean superadmin) {
    this.superadmin = superadmin;
  }

  public Boolean isExternal() {
    return external;
  }

  public void setExternal(Boolean external) {
    this.external = external;
  }

  public QFAUserHasOperation addUserHasOperation(QFAUserHasOperation userHasOperations) {
    if (getUserHasOperations() == null) {
      setUserHasOperations(new ArrayList<QFAUserHasOperation>());
    }
    getUserHasOperations().add(userHasOperations);
    userHasOperations.setUser(this);

    return userHasOperations;
  }

  public QFAUserHasOperation removeUserHasOperation(QFAUserHasOperation userHasOperations) {
    getUserHasOperations().remove(userHasOperations);
    userHasOperations.setUser(null);

    return userHasOperations;
  }

  public List<QFASession> getSessions() {
    return this.sessions;
  }

  public void setSessions(List<QFASession> sessions) {
    this.sessions = sessions;
  }

  public QFASession addSession(QFASession session) {
    getSessions().add(session);
    session.setUser(this);

    return session;
  }

  public QFASession removeSession(QFASession session) {
    getSessions().remove(session);
    session.setUser(null);

    return session;
  }

  public List<QFAGroup> getGroups() {
    return this.groups;
  }

  public void setGroups(List<QFAGroup> groups) {
    this.groups = groups;
  }

  public List<QFAUserAttribute> getUserAttributes() {
    return this.userAttributes;
  }

  public void setUserAttributes(List<QFAUserAttribute> userAttributes) {
    this.userAttributes = userAttributes;
  }

  public List<QFAVerificationToken> getVerificationTokens() {
    return verificationTokens;
  }

  public void setVerificationTokens(
      List<QFAVerificationToken> verificationTokens) {
    this.verificationTokens = verificationTokens;
  }

  public QFAUserAttribute addUserAttribute(QFAUserAttribute userAttribute) {
    getUserAttributes().add(userAttribute);
    userAttribute.setUser(this);

    return userAttribute;
  }

  public QFAUserAttribute removeUserAttribute(QFAUserAttribute userAttribute) {
    getUserAttributes().remove(userAttribute);
    userAttribute.setUser(null);

    return userAttribute;
  }

}