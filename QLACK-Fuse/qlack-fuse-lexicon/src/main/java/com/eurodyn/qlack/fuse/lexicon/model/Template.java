package com.eurodyn.qlack.fuse.lexicon.model;

import java.util.UUID;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "lex_template")
@Getter
@Setter
public class Template extends LexiconModel{

	@Id
	// @GeneratedValue(generator = "uuid")
	// @GenericGenerator(name = "uuid", strategy = "uuid2")
	private String id;
	@Version
	private long dbversion;
	private String name;
	private String content;
	@ManyToOne
	@JoinColumn(name = "language_id")
	private Language language;

//	public Template() {
//		id = UUID.randomUUID().toString();
//	}

	//
	// public static Template find(String templateID, EntityManager em) {
	// return em.find(Template.class, templateID);
	// }
	//
	// public static List<Template> findByName(String templateName, EntityManager
	// em) {
	// Query q = em.createQuery("SELECT t FROM Template t WHERE t.name = :name");
	// q.setParameter("name", templateName);
	// List<Template> resultList = q.getResultList();
	// return resultList;
	// }
	//
	// public static Template findByNameAndLanguageId(String templateName, String
	// languageId,
	// EntityManager em) {
	// Query q;
	// if (languageId == null) {
	// q = em.createQuery(
	// "SELECT t FROM Template t WHERE t.name = :name AND t.language.id is null");
	// } else {
	// q = em.createQuery(
	// "SELECT t FROM Template t WHERE t.name = :name AND t.language.id =
	// :languageId");
	// q.setParameter("languageId", languageId);
	// }
	//
	// q.setParameter("name", templateName);
	// List<Template> resultList = q.getResultList();
	// if (resultList.isEmpty()) {
	// return null;
	// }
	// return resultList.get(0);
	// }
	//
	// public static Template findByNameAndLocale(String templateName, String
	// locale,
	// EntityManager em) {
	// Query q = em.createQuery(
	// "SELECT t FROM Template t WHERE t.name = :name AND t.language.locale =
	// :locale");
	// q.setParameter("name", templateName);
	// q.setParameter("locale", locale);
	// List<Template> resultList = q.getResultList();
	// if (resultList.isEmpty()) {
	// return null;
	// }
	// return resultList.get(0);
	// }

}
