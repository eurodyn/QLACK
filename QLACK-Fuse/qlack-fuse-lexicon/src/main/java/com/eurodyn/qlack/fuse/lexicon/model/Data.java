package com.eurodyn.qlack.fuse.lexicon.model;

import javax.persistence.Cacheable;
import javax.persistence.Column;
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
@Table(name = "lex_data")
@Getter
@Setter
public class Data extends LexiconModel {

	private static final long serialVersionUID = 1L;
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

}
