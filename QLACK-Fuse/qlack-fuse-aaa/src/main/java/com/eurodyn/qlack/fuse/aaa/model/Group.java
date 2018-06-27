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
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
public class Group implements Serializable {

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
  private Group parent;

  @OneToMany(mappedBy = "parent")
  private List<Group> children;

  //bi-directional many-to-one association to GroupHasOperation
  @OneToMany(mappedBy = "group")
  @OnDelete(action = OnDeleteAction.CASCADE)
  private List<GroupHasOperation> groupHasOperations;

  //bi-directional many-to-many association to Group
  @ManyToMany
  @JoinTable(
      name = "aaa_user_has_group",
      joinColumns = {
          @JoinColumn(name = "group_id")
      },
      inverseJoinColumns = {
          @JoinColumn(name = "user_id")
      })
  private List<User> users;

  public Group() {
    id = UUID.randomUUID().toString();
  }

  public static Group find(String groupID, EntityManager em) {
    return em.find(Group.class, groupID);
  }

  public static Group findByName(String name, EntityManager em) {
    Query q = em.createQuery("SELECT g FROM com.eurodyn.qlack.fuse.aaa.model.Group g WHERE g.name = :groupName");
    q.setParameter("groupName", name);
    List<Group> l = q.getResultList();
    if (l.isEmpty()) {
      return null;
    } else {
      return l.get(0);
    }
  }

  public static Group findByObjectId(String objectId, EntityManager em) {
    Group retVal = null;

    Query q = em.createQuery("select g from com.eurodyn.qlack.fuse.aaa.model.Group g where g.objectId = :objectID")
        .setParameter("objectID", objectId);
    List<Group> l = q.getResultList();
    if (!l.isEmpty()) {
      retVal = (Group) l.get(0);
    }

    return retVal;
  }

  public static Set<String> getAllGroupIds(EntityManager em) {
    Set<String> retVal = new HashSet<>();
    Query query = em.createQuery("SELECT g.id FROM com.eurodyn.qlack.fuse.aaa.model.Group g");
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

  public List<GroupHasOperation> getGroupHasOperations() {
    return this.groupHasOperations;
  }

  public void setGroupHasOperations(List<GroupHasOperation> groupHasOperations) {
    this.groupHasOperations = groupHasOperations;
  }

  public GroupHasOperation addGroupHasOperation(GroupHasOperation groupHasOperation) {
    if (getGroupHasOperations() == null) {
      setGroupHasOperations(new ArrayList<GroupHasOperation>());
    }
    getGroupHasOperations().add(groupHasOperation);
    groupHasOperation.setGroup(this);

    return groupHasOperation;
  }

  public GroupHasOperation removeGroupHasOperation(GroupHasOperation groupHasOperation) {
    getGroupHasOperations().remove(groupHasOperation);
    groupHasOperation.setGroup(null);

    return groupHasOperation;
  }

  public List<User> getUsers() {
    return this.users;
  }

  public void setUsers(List<User> users) {
    this.users = users;
  }

  public Group getParent() {
    return parent;
  }

  public void setParent(Group parent) {
    this.parent = parent;
  }

  public List<Group> getChildren() {
    return children;
  }

  public void setChildren(List<Group> children) {
    this.children = children;
  }

}