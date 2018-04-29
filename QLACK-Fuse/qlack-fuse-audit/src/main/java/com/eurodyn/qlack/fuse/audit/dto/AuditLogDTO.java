package com.eurodyn.qlack.fuse.audit.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AuditLogDTO implements Serializable {

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

  public Date getCreatedOn() {
    return createdOn;

  }

  public void setCreatedOn(Date createdOn) {
    this.createdOn = createdOn;
  }

  public String getEvent() {
    return event;
  }

  public void setEvent(String event) {
    this.event = event;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getLevel() {
    return level;
  }

  public void setLevel(String level) {
    this.level = level;
  }

  public String getPrinSessionId() {
    return prinSessionId;
  }

  public void setPrinSessionId(String prinSessionId) {
    this.prinSessionId = prinSessionId;
  }

  public String getShortDescription() {
    return shortDescription;
  }

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

  public String getTraceData() {
    return traceData;
  }

  public void setTraceData(String traceData) {
    this.traceData = traceData;
  }

  public String getReferenceId() {
    return referenceId;
  }

  public void setReferenceId(String referenceId) {
    this.referenceId = referenceId;
  }

  public String getGroupName() {
    return groupName;
  }

  public void setGroupName(String groupName) {
    this.groupName = groupName;
  }

  public String getCorrelationId() {
    return correlationId;
  }

  public void setCorrelationId(String correlationId) {
    this.correlationId = correlationId;
  }

  public String getOpt1() {
    return opt1;
  }

  public void setOpt1(String opt1) {
    this.opt1 = opt1;
  }

  public String getOpt2() {
    return opt2;
  }

  public void setOpt2(String opt2) {
    this.opt2 = opt2;
  }

  public String getOpt3() {
    return opt3;
  }

  public void setOpt3(String opt3) {
    this.opt3 = opt3;
  }
}
