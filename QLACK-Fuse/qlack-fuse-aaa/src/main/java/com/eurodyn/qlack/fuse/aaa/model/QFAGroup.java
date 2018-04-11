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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
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
 * The persistent class for the aaa_group database table.
 */
@Entity
@Table(name = "aaa_group")
public class QFAGroup implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  private String id;

  @Version
  private long dbversion;

  private String description;

  private String name;

  @Column(name = "object_id")
  private String objectId;

  @ManyToOne
  @JoinColumn(name = "parent")
  private QFAGroup parent;

  @OneToMany(mappedBy = "parent")
  private List<QFAGroup> children;

  //bi-directional many-to-one association to QFAGroupHasOperation
  @OneToMany(mappedBy = "group")
  private List<QFAGroupHasOperation> groupHasOperations;

  //bi-directional many-to-many association to QFAGroup
  @ManyToMany
  @JoinTable(
      name = "aaa_user_has_group",
      joinColumns = {
          @JoinColumn(name = "group_id")
      },
      inverseJoinColumns = {
          @JoinColumn(name = "user_id")
      })
  private List<QFAUser> users;

  public QFAGroup() {
    id = UUID.randomUUID().toString();
  }

  public static QFAGroup find(String groupID, EntityManager em) {
    return em.find(QFAGroup.class, groupID);
  }

  public static QFAGroup findByName(String name, EntityManager em) {
    Query q = em.createQuery("SELECT g FROM QFAGroup g WHERE g.name = :groupName");
    q.setParameter("groupName", name);
    List<QFAGroup> l = q.getResultList();
    if (l.isEmpty()) {
      return null;
    } else {
      return l.get(0);
    }
  }

  public static QFAGroup findByObjectId(String objectId, EntityManager em) {
    QFAGroup retVal = null;

    Query q = em.createQuery("select g from QFAGroup g where g.objectId = :objectID")
        .setParameter("objectID", objectId);
    List<QFAGroup> l = q.getResultList();
    if (!l.isEmpty()) {
      retVal = (QFAGroup) l.get(0);
    }

    return retVal;
  }

  public static Set<String> getAllGroupIds(EntityManager em) {
    Set<String> retVal = new HashSet<>();
    Query query = em.createQuery("SELECT g.id FROM QFAGroup g");
    retVal.addAll(query.getResultList());
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
    groupHasOperation.setGroup(this);

    return groupHasOperation;
  }

  public QFAGroupHasOperation removeGroupHasOperation(QFAGroupHasOperation groupHasOperation) {
    getGroupHasOperations().remove(groupHasOperation);
    groupHasOperation.setGroup(null);

    return groupHasOperation;
  }

  public List<QFAUser> getUsers() {
    return this.users;
  }

  public void setUsers(List<QFAUser> users) {
    this.users = users;
  }

  public QFAGroup getParent() {
    return parent;
  }

  public void setParent(QFAGroup parent) {
    this.parent = parent;
  }

  public List<QFAGroup> getChildren() {
    return children;
  }

  public void setChildren(List<QFAGroup> children) {
    this.children = children;
  }

}