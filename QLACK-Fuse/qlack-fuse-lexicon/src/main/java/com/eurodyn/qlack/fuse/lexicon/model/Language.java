package com.eurodyn.qlack.fuse.lexicon.model;

import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import lombok.Getter;
import lombok.Setter;

@Entity
@Cacheable
@DynamicUpdate
@DynamicInsert
@Table(name = "lex_language")
@Getter
@Setter
public class Language extends LexiconModel {

	private static final long serialVersionUID = -1L;

	@Version
	private long dbversion;
	private String name;
	private String locale;
	private boolean active;
	@OneToMany(mappedBy = "language")
	private List<Data> data;
	@OneToMany(mappedBy = "language")
	private List<Template> templates;

	// public static Language find(String languageId, EntityManager em) {
	// return em.find(Language.class, languageId);
	// }
	//
	// public static Language findByLocale(String locale, EntityManager em) {
	// Query query = em.createQuery("SELECT l FROM Language l WHERE l.locale =
	// :locale");
	// query.setParameter("locale", locale);
	// List<Language> queryResult = query.getResultList();
	// if (queryResult.isEmpty()) {
	// return null;
	// }
	// return queryResult.get(0);
	// }
	//
	// public static List<Language> getAllLanguages(EntityManager em) {
	// Query query = em.createQuery("SELECT l FROM Language l ORDER BY l.name ASC");
	// return query.getResultList();
	// }
	//
	// public static List<Language> getActiveLanguages(EntityManager em) {
	// Query query = em
	// .createQuery("SELECT l FROM Language l WHERE l.active = true ORDER BY l.name
	// ASC");
	// return query.getResultList();
	// }

}
