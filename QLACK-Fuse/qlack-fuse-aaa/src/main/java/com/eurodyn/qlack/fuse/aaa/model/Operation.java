package com.eurodyn.qlack.fuse.aaa.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Query;
import javax.persistence.Table;
import javax.persistence.Version;
import lombok.Getter;
import lombok.Setter;

/**
 * The persistent class for the aaa_operation database table.
 */
@Entity
@Table(name = "aaa_operation")
@Getter
@Setter
public class Operation implements Serializable {

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

  //bi-directional many-to-one association to GroupHasOperation
  @OneToMany(mappedBy = "operation")
  private List<GroupHasOperation> groupHasOperations;

  //bi-directional many-to-one association to OpTemplateHasOperation
  @OneToMany(mappedBy = "operation")
  private List<OpTemplateHasOperation> opTemplateHasOperations;

  //bi-directional many-to-one association to UserHasOperation
  @OneToMany(mappedBy = "operation")
  private List<UserHasOperation> userHasOperations;

  public Operation() {
    id = UUID.randomUUID().toString();
  }

  public static Operation find(String operationID, EntityManager em) {
    return em.find(Operation.class, operationID);
  }

  public static Operation findByName(String opName, EntityManager em) {
    Operation retVal = null;

    Query q = em.createQuery("select o from com.eurodyn.qlack.fuse.aaa.model.Operation o where o.name = :operationName");
    q.setParameter("operationName", opName);
    List<Operation> l = q.getResultList();
    if (!l.isEmpty()) {
      retVal = (Operation) l.get(0);
    }

    return retVal;
  }

  public static List<Operation> findAll(EntityManager em) {
    Query q = em.createQuery("SELECT o FROM com.eurodyn.qlack.fuse.aaa.model.Operation o");
    return q.getResultList();
  }

  public GroupHasOperation addGroupHasOperation(GroupHasOperation groupHasOperation) {
    if (getGroupHasOperations() == null) {
      setGroupHasOperations(new ArrayList<GroupHasOperation>());
    }
    getGroupHasOperations().add(groupHasOperation);
    groupHasOperation.setOperation(this);

    return groupHasOperation;
  }

  public GroupHasOperation removeGroupHasOperation(GroupHasOperation groupHasOperation) {
    getGroupHasOperations().remove(groupHasOperation);
    groupHasOperation.setOperation(null);

    return groupHasOperation;
  }

  public OpTemplateHasOperation addOpTemplateHasOperation(
      OpTemplateHasOperation opTemplateHasOperation) {
    if (getOpTemplateHasOperations() == null) {
      setOpTemplateHasOperations(new ArrayList<>());
    }
    getOpTemplateHasOperations().add(opTemplateHasOperation);
    opTemplateHasOperation.setOperation(this);

    return opTemplateHasOperation;
  }

  public OpTemplateHasOperation removeOpTemplateHasOperation(
      OpTemplateHasOperation opTemplateHasOperation) {
    getOpTemplateHasOperations().remove(opTemplateHasOperation);
    opTemplateHasOperation.setOperation(null);

    return opTemplateHasOperation;
  }

  public UserHasOperation addUserHasOperation(UserHasOperation userHasOperation) {
    getUserHasOperations().add(userHasOperation);
    userHasOperation.setOperation(this);

    return userHasOperation;
  }

  public UserHasOperation removeUserHasOperation(UserHasOperation userHasOperation) {
    getUserHasOperations().remove(userHasOperation);
    userHasOperation.setOperation(null);

    return userHasOperation;
  }

}