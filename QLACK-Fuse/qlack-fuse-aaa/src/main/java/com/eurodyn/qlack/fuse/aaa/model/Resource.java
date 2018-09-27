package com.eurodyn.qlack.fuse.aaa.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;
import lombok.Getter;
import lombok.Setter;

/**
 * The persistent class for the aaa_resource database table.
 */
@Entity
@Table(name = "aaa_resource")
@Getter
@Setter
public class Resource extends AAAModel {

  private static final long serialVersionUID = 1L;

  @Version
  private long dbversion;

  private String name;

  private String description;

  @Column(name = "object_id")
  private String objectId;

  //bi-directional many-to-one association to UserHasOperation
  @OneToMany(mappedBy = "resource")
  private List<UserHasOperation> userHasOperations;

  //bi-directional many-to-one association to GroupHasOperation
  @OneToMany(mappedBy = "resource")
  private List<GroupHasOperation> groupHasOperations;

  //bi-directional many-to-one association to OpTemplateHasOperation
  @OneToMany(mappedBy = "resource")
  private List<OpTemplateHasOperation> opTemplateHasOperations;

  public Resource() {
    setId(UUID.randomUUID().toString());
  }

//  public static Resource find(String resourceID, EntityManager em) {
//    return em.find(Resource.class, resourceID);
//  }

//  public static Resource findByObjectID(final String resourceObjectID, final EntityManager em) {
//    Resource retVal = null;
//
//    Query q = em.createQuery("select r from com.eurodyn.qlack.fuse.aaa.model.Resource r where r.objectId = :objectID")
//        .setParameter("objectID", resourceObjectID);
//    List<Resource> l = q.getResultList();
//    if (!l.isEmpty()) {
//      retVal = l.get(0);
//    }
//
//    return retVal;
//  }

  public UserHasOperation addUserHasOperation(UserHasOperation userHasOperation) {
    if (getUserHasOperations() == null) {
      setUserHasOperations(new ArrayList<UserHasOperation>());
    }
    getUserHasOperations().add(userHasOperation);
    userHasOperation.setResource(this);

    return userHasOperation;
  }

  public UserHasOperation removeUserHasOperation(UserHasOperation userHasOperation) {
    getUserHasOperations().remove(userHasOperation);
    userHasOperation.setResource(null);

    return userHasOperation;
  }

  public GroupHasOperation addGroupHasOperation(GroupHasOperation groupHasOperation) {
    if (getGroupHasOperations() == null) {
      setGroupHasOperations(new ArrayList<GroupHasOperation>());
    }
    getGroupHasOperations().add(groupHasOperation);
    groupHasOperation.setResource(this);

    return groupHasOperation;
  }

  public GroupHasOperation removeGroupHasOperation(GroupHasOperation groupHasOperation) {
    getGroupHasOperations().remove(groupHasOperation);
    groupHasOperation.setResource(null);

    return groupHasOperation;
  }

  public OpTemplateHasOperation addOpTemplateHasOperation(
      OpTemplateHasOperation opTemplateHasOperation) {
    if (getOpTemplateHasOperations() == null) {
      setOpTemplateHasOperations(new ArrayList<OpTemplateHasOperation>());
    }
    getOpTemplateHasOperations().add(opTemplateHasOperation);
    opTemplateHasOperation.setResource(this);

    return opTemplateHasOperation;
  }

  public OpTemplateHasOperation removeOpTemplateHasOperation(
      OpTemplateHasOperation opTemplateHasOperation) {
    getOpTemplateHasOperations().remove(opTemplateHasOperation);
    opTemplateHasOperation.setResource(null);

    return opTemplateHasOperation;
  }

}