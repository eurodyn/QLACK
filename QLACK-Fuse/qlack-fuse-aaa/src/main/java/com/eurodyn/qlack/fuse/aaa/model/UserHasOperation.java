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
@Table(name = "aaa_user_has_operation")
@Getter
@Setter
public class UserHasOperation implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  private String id;

  @Version
  private long dbversion;

  //bi-directional many-to-one association to Group
  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  //bi-directional many-to-one association to Operation
  @ManyToOne
  @JoinColumn(name = "operation")
  private Operation operation;

  //bi-directional many-to-one association to Resource
  @ManyToOne
  @JoinColumn(name = "resource_id")
  private Resource resource;

  private boolean deny;

  public UserHasOperation() {
    id = UUID.randomUUID().toString();
  }

  public static UserHasOperation findByUserIDAndOperationName(String userID,
      String operationName, EntityManager em) {
    Query q = em.createQuery("SELECT o FROM com.eurodyn.qlack.fuse.aaa.model.UserHasOperation o WHERE "
        + "o.user.id = :userID AND o.operation.name = :operationName AND o.resource IS NULL");
    q.setParameter("userID", userID);
    q.setParameter("operationName", operationName);
    List<UserHasOperation> queryResults = q.getResultList();
    if (queryResults.isEmpty()) {
      return null;
    }
    return queryResults.get(0);
  }

  public static UserHasOperation findByUserAndResourceIDAndOperationName(
      String userID, String operationName, String resourceID, EntityManager em) {
    Query q = em.createQuery("SELECT o FROM com.eurodyn.qlack.fuse.aaa.model.UserHasOperation o WHERE "
        + "o.user.id = :userID AND o.operation.name = :operationName AND o.resource.id = :resourceID");
    q.setParameter("userID", userID);
    q.setParameter("operationName", operationName);
    q.setParameter("resourceID", resourceID);
    List<UserHasOperation> queryResults = q.getResultList();
    if (queryResults.isEmpty()) {
      return null;
    }
    return queryResults.get(0);
  }

  public static List<UserHasOperation> findByOperationName(String operationName,
      EntityManager em) {
    Query q = em.createQuery("SELECT o FROM com.eurodyn.qlack.fuse.aaa.model.UserHasOperation o WHERE "
        + "o.operation.name = :operationName AND o.resource IS NULL");
    q.setParameter("operationName", operationName);
    return q.getResultList();
  }

  public static List<UserHasOperation> findByResourceIDAndOperationName(String operationName,
      String resourceID, EntityManager em) {
    Query q = em.createQuery("SELECT o FROM com.eurodyn.qlack.fuse.aaa.model.UserHasOperation o WHERE "
        + "o.operation.name = :operationName AND o.resource.id = :resourceID");
    q.setParameter("operationName", operationName);
    q.setParameter("resourceID", resourceID);
    return q.getResultList();
  }

  public static List<UserHasOperation> findByUserID(String userID, EntityManager em) {
    Query q = em.createQuery("SELECT o FROM com.eurodyn.qlack.fuse.aaa.model.UserHasOperation o WHERE o.user.id = :userID");
    q.setParameter("userID", userID);
    return q.getResultList();
  }

}