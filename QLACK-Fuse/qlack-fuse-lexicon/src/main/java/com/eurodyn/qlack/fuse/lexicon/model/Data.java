package com.eurodyn.qlack.fuse.lexicon.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Query;
import javax.persistence.Table;
import javax.persistence.Version;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "lex_data")
public class Data {

  @Id
  private String id;
  @Version
  private long dbversion;
  @ManyToOne
  @JoinColumn(name = "key_id")
  private Key key;
  private String value;
  @ManyToOne
  @JoinColumn(name = "language_id")
  private Language language;
  @Column(name = "last_updated_on")
  private long lastUpdatedOn;

  public Data() {
    id = UUID.randomUUID().toString();
  }

  public static Data findByKeyAndLanguageId(String keyId, String languageId,
      EntityManager em) {
    Query query = em.createQuery("SELECT d FROM Data d WHERE d.key.id = :keyId "
        + "AND d.language.id = :languageId");
    query.setParameter("keyId", keyId);
    query.setParameter("languageId", languageId);
    List<Data> queryResult = query.getResultList();
    if (queryResult.isEmpty()) {
      return null;
    }
    return queryResult.get(0);
  }

  public static Data findByKeyNameAndLanguageId(String keyName,
      String languageId, EntityManager em) {
    Query query = em.createQuery("SELECT d FROM Data d WHERE d.key.name = :keyName "
        + "AND d.language.id = :languageId");
    query.setParameter("keyName", keyName);
    query.setParameter("languageId", languageId);
    List<Data> queryResult = query.getResultList();
    if (queryResult.isEmpty()) {
      return null;
    }
    return queryResult.get(0);
  }

  public static Data findByKeyIdAndLocale(String keyId, String locale,
      EntityManager em) {
    Query query = em.createQuery("SELECT d FROM Data d WHERE d.key.id = :keyId "
        + "AND d.language.locale = :locale");
    query.setParameter("keyId", keyId);
    query.setParameter("locale", locale);
    List<Data> queryResult = query.getResultList();
    if (queryResult.isEmpty()) {
      return null;
    }
    return queryResult.get(0);
  }

  public static Data findByKeyNameAndLocale(String keyName, String locale,
      EntityManager em) {
    Query query = em.createQuery("SELECT d FROM Data d WHERE d.key.name = :keyName "
        + "AND d.language.locale = :locale");
    query.setParameter("keyName", keyName);
    query.setParameter("locale", locale);
    List<Data> queryResult = query.getResultList();
    if (queryResult.isEmpty()) {
      return null;
    }
    return queryResult.get(0);
  }

  public static List<Data> findByGroupIDAndLocale(String groupId,
      String locale, EntityManager em) {
    Query query = null;
    if (groupId != null) {
      query = em.createQuery("SELECT d FROM Data d WHERE d.key.group.id = :groupId "
          + "AND d.language.locale = :locale");
      query.setParameter("groupId", groupId);
      query.setParameter("locale", locale);
    } else {
      query = em.createQuery("SELECT d FROM Data d WHERE d.key.group IS NULL "
          + "AND d.language.locale = :locale");
      query.setParameter("locale", locale);
    }
    return query.getResultList();
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Key getKey() {
    return key;
  }

  public void setKey(Key key) {
    this.key = key;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public Language getLanguage() {
    return language;
  }

  public void setLanguage(Language language) {
    this.language = language;
  }

  public long getLastUpdatedOn() {
    return lastUpdatedOn;
  }

  public void setLastUpdatedOn(long lastUpdatedOn) {
    this.lastUpdatedOn = lastUpdatedOn;
  }

}
