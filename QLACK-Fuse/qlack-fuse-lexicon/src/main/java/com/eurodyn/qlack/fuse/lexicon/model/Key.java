package com.eurodyn.qlack.fuse.lexicon.model;

import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "lex_key")
@Getter
@Setter
public class Key extends LexiconModel {

	private static final long serialVersionUID = 1L;
	private String id;
	@Version
	private long dbversion;
	private String name;
	@ManyToOne
	@JoinColumn(name = "group_id")
	private Group group;
	
	@OneToMany(mappedBy = "key")
	private List<Data> data;

}
