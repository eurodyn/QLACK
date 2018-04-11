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

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Query;
import javax.persistence.Table;
import javax.persistence.Version;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;


/**
 * The persistent class for the aaa_group_has_operation database table.
 */
@Entity
@Table(name = "aaa_group_has_operation")
public class QFAGroupHasOperation implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  private String id;

  @Version
  private long dbversion;

  //bi-directional many-to-one association to QFAGroup
  @ManyToOne
  @JoinColumn(name = "group_id")
  private QFAGroup group;

  //bi-directional many-to-one association to QFAOperation
  @ManyToOne
  @JoinColumn(name = "operation")
  private QFAOperation operation;

  //bi-directional many-to-one association to QFAResource
  @ManyToOne
  @JoinColumn(name = "resource_id")
  private QFAResource resource;

  private boolean deny;

  public QFAGroupHasOperation() {
    id = UUID.randomUUID().toString();
  }

  public static QFAGroupHasOperation findByGroupIDAndOperationName(String groupID,
      String operationName, EntityManager em) {
    Query q = em.createQuery("SELECT o FROM QFAGroupHasOperation o WHERE "
        + "o.group.id = :groupID AND o.operation.name = :operationName AND o.resource IS NULL");
    q.setParameter("groupID", groupID);
    q.setParameter("operationName", operationName);
    List<QFAGroupHasOperation> queryResults = q.getResultList();
    if (queryResults.isEmpty()) {
      return null;
    }
    return queryResults.get(0);
  }

  public static QFAGroupHasOperation findByGroupIDAndOperationNameAndResourceName(String groupID,
      String operationName, String resourceName, EntityManager em) {
    Query q = em.createQuery("SELECT o FROM QFAGroupHasOperation o WHERE "
        + "o.group.id = :groupID AND o.operation.name = :operationName AND o.resource.name = :resourceName");
    q.setParameter("groupID", groupID);
    q.setParameter("operationName", operationName);
    q.setParameter("resourceName", resourceName);
    List<QFAGroupHasOperation> queryResults = q.getResultList();
    if (queryResults.isEmpty()) {
      return null;
    }
    return queryResults.get(0);
  }

  public static QFAGroupHasOperation findByGroupAndResourceIDAndOperationName(
      String groupID, String operationName, String resourceID, EntityManager em) {
    Query q = em.createQuery("SELECT o FROM QFAGroupHasOperation o WHERE "
        + "o.group.id = :groupID AND o.operation.name = :operationName AND o.resource.id = :resourceID");
    q.setParameter("groupID", groupID);
    q.setParameter("operationName", operationName);
    q.setParameter("resourceID", resourceID);
    List<QFAGroupHasOperation> queryResults = q.getResultList();
    if (queryResults.isEmpty()) {
      return null;
    }
    return queryResults.get(0);
  }

  public static List<QFAGroupHasOperation> findByOperationName(String operationName,
      EntityManager em) {
    Query q = em.createQuery("SELECT o FROM QFAGroupHasOperation o WHERE "
        + "o.operation.name = :operationName AND o.resource IS NULL");
    q.setParameter("operationName", operationName);
    return q.getResultList();
  }

  public static List<QFAGroupHasOperation> findByResourceIDAndOperationName(String operationName,
      String resourceID, EntityManager em) {
    Query q = em.createQuery("SELECT o FROM QFAGroupHasOperation o WHERE "
        + "o.operation.name = :operationName AND o.resource.id = :resourceID");
    q.setParameter("operationName", operationName);
    q.setParameter("resourceID", resourceID);
    return q.getResultList();
  }

  public static List<QFAGroupHasOperation> findByGroupName(String groupName, EntityManager em) {
    Query q = em.createQuery("SELECT o FROM QFAGroupHasOperation o WHERE group.name = :groupName");
    q.setParameter("groupName", groupName);
    return q.getResultList();
  }

  public static List<QFAGroupHasOperation> findByGroupName(List<String> groupNames, EntityManager em) {
    Query q = em.createQuery("SELECT o FROM QFAGroupHasOperation o WHERE group.name in :groupNames");
    q.setParameter("groupNames", groupNames);
    return q.getResultList();
  }

  public String getId() {
    return this.id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public QFAGroup getGroup() {
    return this.group;
  }

  public void setGroup(QFAGroup group) {
    this.group = group;
  }

  public QFAOperation getOperation() {
    return this.operation;
  }

  public void setOperation(QFAOperation operation) {
    this.operation = operation;
  }

  public QFAResource getResource() {
    return resource;
  }

  public void setResource(QFAResource resource) {
    this.resource = resource;
  }

  public boolean isDeny() {
    return deny;
  }

  public void setDeny(boolean deny) {
    this.deny = deny;
  }
}