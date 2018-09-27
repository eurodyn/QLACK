package com.eurodyn.qlack.fuse.aaa.model;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Query;
import javax.persistence.Table;
import javax.persistence.Version;
import lombok.Getter;
import lombok.Setter;

/**
 * The persistent class for the aaa_group_has_operation database table.
 */
@Entity
@Table(name = "aaa_group_has_operation")
@Getter
@Setter
public class GroupHasOperation extends AAAModel {

  private static final long serialVersionUID = 1L;

  @Version
  private long dbversion;

  //bi-directional many-to-one association to Group
  @ManyToOne
  @JoinColumn(name = "group_id")
  private Group group;

  //bi-directional many-to-one association to Operation
  @ManyToOne
  @JoinColumn(name = "operation")
  private Operation operation;

  //bi-directional many-to-one association to Resource
  @ManyToOne
  @JoinColumn(name = "resource_id")
  private Resource resource;

  private boolean deny;

  public GroupHasOperation() {
    setId(UUID.randomUUID().toString());
  }

//  public static GroupHasOperation findByGroupIDAndOperationName(String groupID,
//      String operationName, EntityManager em) {
//    Query q = em.createQuery("SELECT o FROM com.eurodyn.qlack.fuse.aaa.model.GroupHasOperation o WHERE "
//        + "o.group.id = :groupID AND o.operation.name = :operationName AND o.resource IS NULL");
//    q.setParameter("groupID", groupID);
//    q.setParameter("operationName", operationName);
//    List<GroupHasOperation> queryResults = q.getResultList();
//    if (queryResults.isEmpty()) {
//      return null;
//    }
//    return queryResults.get(0);
//  }

//  public static GroupHasOperation findByGroupIDAndOperationNameAndResourceName(String groupID,
//      String operationName, String resourceName, EntityManager em) {
//    Query q = em.createQuery("SELECT o FROM com.eurodyn.qlack.fuse.aaa.model.GroupHasOperation o WHERE "
//        + "o.group.id = :groupID AND o.operation.name = :operationName AND o.resource.name = :resourceName");
//    q.setParameter("groupID", groupID);
//    q.setParameter("operationName", operationName);
//    q.setParameter("resourceName", resourceName);
//    List<GroupHasOperation> queryResults = q.getResultList();
//    if (queryResults.isEmpty()) {
//      return null;
//    }
//    return queryResults.get(0);
//  }

//  public static GroupHasOperation findByGroupAndResourceIDAndOperationName(
//      String groupID, String operationName, String resourceID, EntityManager em) {
//    Query q = em.createQuery("SELECT o FROM com.eurodyn.qlack.fuse.aaa.model.GroupHasOperation o WHERE "
//        + "o.group.id = :groupID AND o.operation.name = :operationName AND o.resource.id = :resourceID");
//    q.setParameter("groupID", groupID);
//    q.setParameter("operationName", operationName);
//    q.setParameter("resourceID", resourceID);
//    List<GroupHasOperation> queryResults = q.getResultList();
//    if (queryResults.isEmpty()) {
//      return null;
//    }
//    return queryResults.get(0);
//  }

//  public static List<GroupHasOperation> findByOperationName(String operationName,
//      EntityManager em) {
//    Query q = em.createQuery("SELECT o FROM com.eurodyn.qlack.fuse.aaa.model.GroupHasOperation o WHERE "
//        + "o.operation.name = :operationName AND o.resource IS NULL");
//    q.setParameter("operationName", operationName);
//    return q.getResultList();
//  }

//  public static List<GroupHasOperation> findByResourceIDAndOperationName(String operationName,
//      String resourceID, EntityManager em) {
//    Query q = em.createQuery("SELECT o FROM com.eurodyn.qlack.fuse.aaa.model.GroupHasOperation o WHERE "
//        + "o.operation.name = :operationName AND o.resource.id = :resourceID");
//    q.setParameter("operationName", operationName);
//    q.setParameter("resourceID", resourceID);
//    return q.getResultList();
//  }

//  public static List<GroupHasOperation> findByGroupName(String groupName, EntityManager em) {
//    Query q = em.createQuery("SELECT o FROM com.eurodyn.qlack.fuse.aaa.model.GroupHasOperation o WHERE group.name = :groupName");
//    q.setParameter("groupName", groupName);
//    return q.getResultList();
//  }

//  public static List<GroupHasOperation> findByGroupName(List<String> groupNames,
//      EntityManager em) {
//    Query q = em
//        .createQuery("SELECT o FROM com.eurodyn.qlack.fuse.aaa.model.GroupHasOperation o WHERE group.name in :groupNames");
//    q.setParameter("groupNames", groupNames);
//    return q.getResultList();
//  }

}