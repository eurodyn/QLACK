package com.eurodyn.qlack.fuse.lexicon.model;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Query;
import javax.persistence.Table;
import javax.persistence.Version;

import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "lex_key")
@Getter
@Setter
public class Key {

  @Id
  private String id;
  @Version
  private long dbversion;
  private String name;
  @ManyToOne
  @JoinColumn(name = "group_id")
  private Group group;
  @OneToMany(mappedBy = "key")
  private List<Data> data;

  public Key() {
    id = UUID.randomUUID().toString();
  }

  public static Key find(String keyId, EntityManager em) {
    return em.find(Key.class, keyId);
  }

  public static Key findByName(String keyName, String groupId, EntityManager em) {
    Query query = null;
    if (groupId != null) {
      query = em
          .createQuery("SELECT k FROM Key k WHERE k.name = :name AND k.group.id = :groupId");
      query.setParameter("name", keyName);
      query.setParameter("groupId", groupId);
    } else {
      query = em.createQuery("SELECT k FROM Key k WHERE k.name = :name AND k.group IS NULL");
      query.setParameter("name", keyName);
    }
    List<Key> queryResult = query.getResultList();
    if (queryResult.isEmpty()) {
      return null;
    }
    return queryResult.get(0);
  }

  public static List<Key> getAllKeys(EntityManager em) {
    Query query = em.createQuery("SELECT k FROM Key k");
    return query.getResultList();
  }

}
