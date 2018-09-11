package com.eurodyn.qlack.fuse.audit.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "al_audit")
@Getter
@Setter
public class Audit {

  @Id
  private String id;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "level_id")
  private AuditLevel levelId;
  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "trace_id")
  private AuditTrace traceId;
  @Column(name = "prin_session_id")
  private String prinSessionId;
  @Column(name = "short_description")
  private String shortDescription;
  @Column(name = "event")
  private String event;
  @Column(name = "created_on")
  private Long createdOn;
  @Column(name = "reference_id")
  private String referenceId;
  @Column(name = "group_name")
  private String groupName;
  @Column(name = "correlation_id")
  private String correlationId;
  @Column(name = "opt1")
  private String opt1;
  @Column(name = "opt2")
  private String opt2;
  @Column(name = "opt3")
  private String opt3;

  public Audit() {
    id = java.util.UUID.randomUUID().toString();
  }

}