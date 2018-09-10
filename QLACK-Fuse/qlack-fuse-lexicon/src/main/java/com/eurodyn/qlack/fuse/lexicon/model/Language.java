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
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "lex_language")
@Getter
@Setter
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

}
