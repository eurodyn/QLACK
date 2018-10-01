package com.eurodyn.qlack.fuse.audit.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AuditLevelDTO extends AuditBaseDTO {

  private String name;
  private String description;
  private String prinSessionId;
  private Long createdOn;

  /**
   * parameterized Constructor
   */
  public AuditLevelDTO(String name) {
    this.setName(name);
  }

  /**
   * parameterized Constructor
   */
  public AuditLevelDTO(String id, String name) {
    this.setName(name);
    this.setId(id);
  }

}
