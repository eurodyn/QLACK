package com.eurodyn.qlack.fuse.lexicon.model;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
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


	private static final long serialVersionUID = 1L;
	@Version
	private long dbversion;
	private String name;
	private String content;
	@ManyToOne
	@JoinColumn(name = "language_id")
	private Language language;

}
