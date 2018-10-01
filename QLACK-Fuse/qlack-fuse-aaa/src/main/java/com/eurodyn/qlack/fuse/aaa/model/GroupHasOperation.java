package com.eurodyn.qlack.fuse.aaa.model;

import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import lombok.Getter;
import lombok.Setter;

/**
 * The persistent class for the aaa_group_has_operation database table.
 */
@Entity
@Table(name = "aaa_group_has_operation")
@Getter
@Setter
public class GroupHasOperation extends AAAModel {

  private static final long serialVersionUID = 1L;

  @Version
  private long dbversion;

  //bi-directional many-to-one association to Group
  @ManyToOne
  @JoinColumn(name = "group_id")
  private Group group;

  //bi-directional many-to-one association to Operation
  @ManyToOne
  @JoinColumn(name = "operation")
  private Operation operation;

  //bi-directional many-to-one association to Resource
  @ManyToOne
  @JoinColumn(name = "resource_id")
  private Resource resource;

  private boolean deny;

  public GroupHasOperation() {
    setId(UUID.randomUUID().toString());
  }

}