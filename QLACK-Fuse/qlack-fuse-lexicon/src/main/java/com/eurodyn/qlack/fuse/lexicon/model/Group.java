package com.eurodyn.qlack.fuse.lexicon.model;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;

import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "lex_group")
@Getter
@Setter
public class Group {

  @Id
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

  public static Group find(String groupID, EntityManager em) {
    return em.find(Group.class, groupID);
  }

}
