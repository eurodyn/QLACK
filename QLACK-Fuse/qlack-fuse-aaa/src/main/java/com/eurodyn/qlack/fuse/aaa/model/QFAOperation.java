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
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Query;
import javax.persistence.Table;
import javax.persistence.Version;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * The persistent class for the aaa_operation database table.
 */
@Entity
@Table(name = "aaa_operation")
public class QFAOperation implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  private String id;

  @Version
  private long dbversion;

  private String name;

  private String description;

  private boolean dynamic;

  @Lob
  @Column(name = "dynamic_code")
  private String dynamicCode;

  //bi-directional many-to-one association to QFAGroupHasOperation
  @OneToMany(mappedBy = "operation")
  private List<QFAGroupHasOperation> groupHasOperations;

  //bi-directional many-to-one association to QFAOpTemplateHasOperation
  @OneToMany(mappedBy = "operation")
  private List<QFAOpTemplateHasOperation> opTemplateHasOperations;

  //bi-directional many-to-one association to QFAUserHasOperation
  @OneToMany(mappedBy = "operation")
  private List<QFAUserHasOperation> userHasOperations;

  public QFAOperation() {
    id = UUID.randomUUID().toString();
  }

  public static QFAOperation find(String operationID, EntityManager em) {
    return em.find(QFAOperation.class, operationID);
  }

  public static QFAOperation findByName(String opName, EntityManager em) {
    QFAOperation retVal = null;

    Query q = em.createQuery("select o from QFAOperation o where o.name = :operationName");
    q.setParameter("operationName", opName);
    List<QFAOperation> l = q.getResultList();
    if (!l.isEmpty()) {
      retVal = (QFAOperation) l.get(0);
    }

    return retVal;
  }

  public static List<QFAOperation> findAll(EntityManager em) {
    Query q = em.createQuery("SELECT o FROM QFAOperation o");
    return q.getResultList();
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

  public boolean isDynamic() {
    return dynamic;
  }

  public void setDynamic(boolean dynamic) {
    this.dynamic = dynamic;
  }

  public String getDynamicCode() {
    return this.dynamicCode;
  }

  public void setDynamicCode(String dynamicCode) {
    this.dynamicCode = dynamicCode;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<QFAGroupHasOperation> getGroupHasOperations() {
    return this.groupHasOperations;
  }

  public void setGroupHasOperations(List<QFAGroupHasOperation> groupHasOperations) {
    this.groupHasOperations = groupHasOperations;
  }

  public QFAGroupHasOperation addGroupHasOperation(QFAGroupHasOperation groupHasOperation) {
    if (getGroupHasOperations() == null) {
      setGroupHasOperations(new ArrayList<QFAGroupHasOperation>());
    }
    getGroupHasOperations().add(groupHasOperation);
    groupHasOperation.setOperation(this);

    return groupHasOperation;
  }

  public QFAGroupHasOperation removeGroupHasOperation(QFAGroupHasOperation groupHasOperation) {
    getGroupHasOperations().remove(groupHasOperation);
    groupHasOperation.setOperation(null);

    return groupHasOperation;
  }

  public List<QFAOpTemplateHasOperation> getOpTemplateHasOperations() {
    return this.opTemplateHasOperations;
  }

  public void setOpTemplateHasOperations(List<QFAOpTemplateHasOperation> opTemplateHasOperations) {
    this.opTemplateHasOperations = opTemplateHasOperations;
  }

  public QFAOpTemplateHasOperation addOpTemplateHasOperation(
      QFAOpTemplateHasOperation opTemplateHasOperation) {
    if (getOpTemplateHasOperations() == null) {
      setOpTemplateHasOperations(new ArrayList<>());
    }
    getOpTemplateHasOperations().add(opTemplateHasOperation);
    opTemplateHasOperation.setOperation(this);

    return opTemplateHasOperation;
  }

  public QFAOpTemplateHasOperation removeOpTemplateHasOperation(
      QFAOpTemplateHasOperation opTemplateHasOperation) {
    getOpTemplateHasOperations().remove(opTemplateHasOperation);
    opTemplateHasOperation.setOperation(null);

    return opTemplateHasOperation;
  }

  public List<QFAUserHasOperation> getUserHasOperations() {
    return userHasOperations;
  }

  public void setUserHasOperations(List<QFAUserHasOperation> userHasOperations) {
    this.userHasOperations = userHasOperations;
  }

  public QFAUserHasOperation addUserHasOperation(QFAUserHasOperation userHasOperation) {
    getUserHasOperations().add(userHasOperation);
    userHasOperation.setOperation(this);

    return userHasOperation;
  }

  public QFAUserHasOperation removeUserHasOperation(QFAUserHasOperation userHasOperation) {
    getUserHasOperations().remove(userHasOperation);
    userHasOperation.setOperation(null);

    return userHasOperation;
  }
}