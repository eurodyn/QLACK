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
import javax.persistence.OneToMany;
import javax.persistence.Query;
import javax.persistence.Table;
import javax.persistence.Version;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * The persistent class for the aaa_resource database table.
 */
@Entity
@Table(name = "aaa_resource")
public class QFAResource implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  private String id;

  @Version
  private long dbversion;

  private String name;

  private String description;

  @Column(name = "object_id")
  private String objectId;

  //bi-directional many-to-one association to QFAUserHasOperation
  @OneToMany(mappedBy = "resource")
  private List<QFAUserHasOperation> userHasOperations;

  //bi-directional many-to-one association to QFAGroupHasOperation
  @OneToMany(mappedBy = "resource")
  private List<QFAGroupHasOperation> groupHasOperations;

  //bi-directional many-to-one association to QFAOpTemplateHasOperation
  @OneToMany(mappedBy = "resource")
  private List<QFAOpTemplateHasOperation> opTemplateHasOperations;

  public QFAResource() {
    id = UUID.randomUUID().toString();
  }

  public static QFAResource find(String resourceID, EntityManager em) {
    return em.find(QFAResource.class, resourceID);
  }

  public static QFAResource findByObjectID(final String resourceObjectID, final EntityManager em) {
    QFAResource retVal = null;

    Query q = em.createQuery("select r from QFAResource r where r.objectId = :objectID")
        .setParameter("objectID", resourceObjectID);
    List<QFAResource> l = q.getResultList();
    if (!l.isEmpty()) {
      retVal = l.get(0);
    }

    return retVal;
  }

  public String getId() {
    return this.id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getDescription() {
    return this.description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getObjectId() {
    return this.objectId;
  }

  public void setObjectId(String objectId) {
    this.objectId = objectId;
  }

  public List<QFAUserHasOperation> getUserHasOperations() {
    return userHasOperations;
  }

  public void setUserHasOperations(List<QFAUserHasOperation> userHasOperations) {
    this.userHasOperations = userHasOperations;
  }

  public QFAUserHasOperation addUserHasOperation(QFAUserHasOperation userHasOperation) {
    if (getUserHasOperations() == null) {
      setUserHasOperations(new ArrayList<QFAUserHasOperation>());
    }
    getUserHasOperations().add(userHasOperation);
    userHasOperation.setResource(this);

    return userHasOperation;
  }

  public QFAUserHasOperation removeUserHasOperation(QFAUserHasOperation userHasOperation) {
    getUserHasOperations().remove(userHasOperation);
    userHasOperation.setResource(null);

    return userHasOperation;
  }

  public List<QFAGroupHasOperation> getGroupHasOperations() {
    return groupHasOperations;
  }

  public void setGroupHasOperations(List<QFAGroupHasOperation> groupHasOperations) {
    this.groupHasOperations = groupHasOperations;
  }

  public QFAGroupHasOperation addGroupHasOperation(QFAGroupHasOperation groupHasOperation) {
    if (getGroupHasOperations() == null) {
      setGroupHasOperations(new ArrayList<QFAGroupHasOperation>());
    }
    getGroupHasOperations().add(groupHasOperation);
    groupHasOperation.setResource(this);

    return groupHasOperation;
  }

  public QFAGroupHasOperation removeGroupHasOperation(QFAGroupHasOperation groupHasOperation) {
    getGroupHasOperations().remove(groupHasOperation);
    groupHasOperation.setResource(null);

    return groupHasOperation;
  }

  public List<QFAOpTemplateHasOperation> getOpTemplateHasOperations() {
    return opTemplateHasOperations;
  }

  public void setOpTemplateHasOperations(
      List<QFAOpTemplateHasOperation> opTemplateHasOperations) {
    this.opTemplateHasOperations = opTemplateHasOperations;
  }

  public QFAOpTemplateHasOperation addOpTemplateHasOperation(
      QFAOpTemplateHasOperation opTemplateHasOperation) {
    if (getOpTemplateHasOperations() == null) {
      setOpTemplateHasOperations(new ArrayList<QFAOpTemplateHasOperation>());
    }
    getOpTemplateHasOperations().add(opTemplateHasOperation);
    opTemplateHasOperation.setResource(this);

    return opTemplateHasOperation;
  }

  public QFAOpTemplateHasOperation removeOpTemplateHasOperation(
      QFAOpTemplateHasOperation opTemplateHasOperation) {
    getOpTemplateHasOperations().remove(opTemplateHasOperation);
    opTemplateHasOperation.setResource(null);

    return opTemplateHasOperation;
  }

}