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
@Table(name = "lex_group")
@Getter
@Setter
public class Group extends LexiconModel {

	private static final long serialVersionUID = 1L;
	@Version
	private long dbversion;
	private String title;
	private String description;
	@OneToMany(mappedBy = "group")
	private List<Key> keys;

}
