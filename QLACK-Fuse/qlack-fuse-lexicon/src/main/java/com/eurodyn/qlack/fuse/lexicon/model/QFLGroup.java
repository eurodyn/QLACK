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
public class QFLGroup {

  @Id
  private String id;
  @Version
  private long dbversion;
  private String title;
  private String description;
  @OneToMany(mappedBy = "group")
  private List<QFLKey> keys;

  public QFLGroup() {
    id = UUID.randomUUID().toString();
  }

  public static QFLGroup find(String groupID, EntityManager em) {
    return em.find(QFLGroup.class, groupID);
  }

  public static QFLGroup findByName(String groupName, EntityManager em) {
    Query query = em.createQuery("SELECT g FROM QFLGroup g WHERE g.title = :groupName");
    query.setParameter("groupName", groupName);
    List<QFLGroup> resultList = query.getResultList();
    if (resultList.isEmpty()) {
      return null;
    }
    return resultList.get(0);
  }

  public static List<QFLGroup> getAllGroups(EntityManager em) {
    Query query = em.createQuery("SELECT g FROM QFLGroup g");
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

  public List<QFLKey> getKeys() {
    return keys;
  }

  public void setKeys(List<QFLKey> keys) {
    this.keys = keys;
  }

}
