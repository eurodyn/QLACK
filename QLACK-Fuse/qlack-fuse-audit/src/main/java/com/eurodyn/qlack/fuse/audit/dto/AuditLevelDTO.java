package com.eurodyn.qlack.fuse.audit.dto;

import java.io.Serializable;
import java.util.Date;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AuditLevelDTO implements Serializable {

  private String name;
  private String id;
  private String description;
  private String prinSessionId;
  private Date createdOn;

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
