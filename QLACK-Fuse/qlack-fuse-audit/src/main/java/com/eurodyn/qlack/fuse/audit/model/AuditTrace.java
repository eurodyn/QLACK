package com.eurodyn.qlack.fuse.audit.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "al_audit_trace")
public class AuditTrace {

  @Id
  private String id;
  @Column(name = "trace_data")
  private String traceData;

  public AuditTrace() {
    id = java.util.UUID.randomUUID().toString();
  }

  public String getId() {
    return this.id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getTraceData() {
    return this.traceData;
  }

  public void setTraceData(String traceData) {
    this.traceData = traceData;
  }

}
