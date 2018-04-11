package com.eurodyn.qlack.fuse.mailing.dto;

import java.util.Date;
import java.util.List;

/**
 * Data transfer object for internal messages.
 *
 * @author European Dynamics SA.
 */
public class InternalMessagesDTO extends MailBaseDTO {

  private String subject;
  private String message;
  private String from;
  private String to;
  private Date dateSent;
  private Date dateReceived;
  private String status;
  private String deleteType;
  private List<InternalAttachmentDTO> attachments;
  private String fwdAttachmentId;

  // -- Accessors

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getFrom() {
    return from;
  }

  public void setFrom(String from) {
    this.from = from;
  }

  public String getTo() {
    return to;
  }

  public void setTo(String to) {
    this.to = to;
  }

  public Date getDateSent() {
    return dateSent;
  }

  public void setDateSent(Long dateSent) {
    this.dateSent = new Date(dateSent);
  }

  public Date getDateReceived() {
    return dateReceived;
  }

  public void setDateReceived(Long dateReceived) {
    this.dateReceived = new Date(dateReceived);
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getDeleteType() {
    return deleteType;
  }

  public void setDeleteType(String deleteType) {
    this.deleteType = deleteType;
  }

  public List<InternalAttachmentDTO> getAttachments() {
    return attachments;
  }

  public void setAttachments(List<InternalAttachmentDTO> attachments) {
    this.attachments = attachments;
  }

  public String getFwdAttachmentId() {
    return fwdAttachmentId;
  }

  public void setFwdAttachmentId(String fwdAttachmentId) {
    this.fwdAttachmentId = fwdAttachmentId;
  }
}
