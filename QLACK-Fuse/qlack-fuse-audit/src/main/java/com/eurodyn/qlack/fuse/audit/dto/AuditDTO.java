package com.eurodyn.qlack.fuse.audit.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuditDTO extends AuditBaseDTO {

  private Long createdOn;

  private String prinSessionId;

  private String shortDescription;

  private String event;

  private String groupName;

  private String correlationId;

  private String referenceId;

  private String opt1;

  private String opt2;

  private String opt3;

  private String level;

  private AuditTraceDTO trace;
}
