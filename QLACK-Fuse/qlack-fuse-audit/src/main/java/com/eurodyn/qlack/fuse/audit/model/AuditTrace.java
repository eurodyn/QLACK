package com.eurodyn.qlack.fuse.audit.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "al_audit_trace")
@Getter
@Setter
public class AuditTrace {

  @Id
  private String id;
  @Column(name = "trace_data")
  private String traceData;

  public AuditTrace() {
    id = java.util.UUID.randomUUID().toString();
  }

}
