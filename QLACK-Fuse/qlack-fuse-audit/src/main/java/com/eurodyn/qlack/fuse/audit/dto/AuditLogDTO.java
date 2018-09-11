package com.eurodyn.qlack.fuse.audit.dto;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuditLogDTO {

  private String id;
  private String level;
  private Date createdOn;
  private String prinSessionId;
  private String shortDescription;
  private String event;
  private String traceData;
  private String referenceId;
  private String groupName;
  private String correlationId;
  private String opt1;
  private String opt2;
  private String opt3;

  public void setShortDescription(String shortDescription) {
    if ((shortDescription != null) && (shortDescription.length() > 2048)) {
      Logger.getLogger(AuditLogDTO.class.getName()).log(
          Level.WARNING,
          "shortDescription value "
              + "was truncated to 2048 characters");
      shortDescription = shortDescription.substring(0, 2047);
    }
    this.shortDescription = shortDescription;
  }

}
