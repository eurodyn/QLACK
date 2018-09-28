package com.eurodyn.qlack.fuse.lexicon.model;

import java.util.List;
import java.util.UUID;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import lombok.Getter;
import lombok.Setter;

@Entity
@Cacheable
@DynamicUpdate
@DynamicInsert
@Table(name = "lex_group")
@Getter
@Setter
public class Group {

  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  private String id;
  @Version
  private long dbversion;
  private String title;
  private String description;
  @OneToMany(mappedBy = "group")
  private List<Key> keys;

  public Group() {
    id = UUID.randomUUID().toString();
  }

//  public static Group find(String groupID, EntityManager em) {
//    return em.find(Group.class, groupID);
//  }

}
