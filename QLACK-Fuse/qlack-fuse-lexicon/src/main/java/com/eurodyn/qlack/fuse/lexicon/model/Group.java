package com.eurodyn.qlack.fuse.lexicon.model;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Query;
import javax.persistence.Table;
import javax.persistence.Version;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "lex_group")
public class Group {

  @Id
  private String id;
  @Version
  private long dbversion;
  private String title;
  private String description;
  @OneToMany(mappedBy = "group")
  private List<Key> keys;

  public Group() {
    id = UUID.randomUUID().toString();
  }

  public static Group find(String groupID, EntityManager em) {
    return em.find(Group.class, groupID);
  }

  public static Group findByName(String groupName, EntityManager em) {
    Query query = em.createQuery("SELECT g FROM Group g WHERE g.title = :groupName");
    query.setParameter("groupName", groupName);
    List<Group> resultList = query.getResultList();
    if (resultList.isEmpty()) {
      return null;
    }
    return resultList.get(0);
  }

  public static List<Group> getAllGroups(EntityManager em) {
    Query query = em.createQuery("SELECT g FROM Group g");
    return query.getResultList();
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public List<Key> getKeys() {
    return keys;
  }

  public void setKeys(List<Key> keys) {
    this.keys = keys;
  }

}
