package com.eurodyn.qlack.fuse.audit.model;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public abstract class AuditBaseEntity {

  @Id
  private String id;
}
