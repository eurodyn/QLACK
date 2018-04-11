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
@Table(name = "lex_language")
public class Language {

  @Id
  private String id;
  @Version
  private long dbversion;
  private String name;
  private String locale;
  private boolean active;
  @OneToMany(mappedBy = "language")
  private List<Data> data;
  @OneToMany(mappedBy = "language")
  private List<Template> templates;

  public Language() {
    id = UUID.randomUUID().toString();
  }

  public static Language find(String languageId, EntityManager em) {
    return em.find(Language.class, languageId);
  }

  public static Language findByLocale(String locale, EntityManager em) {
    Query query = em.createQuery("SELECT l FROM Language l WHERE l.locale = :locale");
    query.setParameter("locale", locale);
    List<Language> queryResult = query.getResultList();
    if (queryResult.isEmpty()) {
      return null;
    }
    return queryResult.get(0);
  }

  public static List<Language> getAllLanguages(EntityManager em) {
    Query query = em.createQuery("SELECT l FROM Language l ORDER BY l.name ASC");
    return query.getResultList();
  }

  public static List<Language> getActiveLanguages(EntityManager em) {
    Query query = em
        .createQuery("SELECT l FROM Language l WHERE l.active = true ORDER BY l.name ASC");
    return query.getResultList();
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getLocale() {
    return locale;
  }

  public void setLocale(String locale) {
    this.locale = locale;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public List<Data> getData() {
    return data;
  }

  public void setData(List<Data> data) {
    this.data = data;
  }

  public List<Template> getTemplates() {
    return templates;
  }

  public void setTemplates(List<Template> templates) {
    this.templates = templates;
  }

}
