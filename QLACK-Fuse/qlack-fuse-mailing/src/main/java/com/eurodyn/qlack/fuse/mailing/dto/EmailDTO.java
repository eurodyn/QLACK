package com.eurodyn.qlack.fuse.mailing.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * DTO for Email data.
 */
public class EmailDTO implements Serializable {

  private String id;

  private String messageId;
  private @NotBlank String subject;
  private @NotBlank String body;
  private @NotBlank String from;
  private @NotEmpty List<String> toContact;
  private List<String> ccContact;
  private List<String> bccContact;
  private List<String> replyToContact;
  private @NotNull EMAIL_TYPE emailType;
  private String status;
  private List<AttachmentDTO> attachments;
  private Date dateSent;
  private String serverResponse;

  public EmailDTO() {
    this.emailType = EMAIL_TYPE.TEXT;
  }

  // -- Constructors

  public String getId() {
    return id;
  }

  // -- Accessors

  public void setId(String id) {
    this.id = id;
  }

  public String getMessageId() {
    return messageId;
  }

  public void setMessageId(String messageId) {
    this.messageId = messageId;
  }

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.body = body;
  }

  public String getFrom() {
    return from;
  }

  public void setFrom(String from) {
    this.from = from;
  }

  public List<String> getToContact() {
    return toContact;
  }

  public void setToContact(List<String> toContact) {
    this.toContact = toContact;
  }

  public void setToContact(String toContact) {
    List<String> l = new ArrayList<String>();
    l.add(toContact);
    setToContact(l);
  }

  public List<String> getCcContact() {
    return ccContact;
  }

  public void setCcContact(List<String> ccContact) {
    this.ccContact = ccContact;
  }

  public List<String> getBccContact() {
    return bccContact;
  }

  public void setBccContact(List<String> bccContact) {
    this.bccContact = bccContact;
  }

  public List<String> getReplyToContact() {
    return replyToContact;
  }

  public void setReplyToContact(List<String> replyToContact) {
    this.replyToContact = replyToContact;
  }

  public EMAIL_TYPE getEmailType() {
    return emailType;
  }

  public void setEmailType(EMAIL_TYPE emailType) {
    this.emailType = emailType;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public List<AttachmentDTO> getAttachments() {
    return attachments;
  }

  public void setAttachments(List<AttachmentDTO> attachment) {
    this.attachments = attachment;
  }

  public void addAttachment(AttachmentDTO attachmentDTO) {
    if (attachments == null) {
      attachments = new ArrayList<>();
    }
    attachments.add(attachmentDTO);
  }

  public Date getDateSent() {
    return dateSent;
  }

  public void setDateSent(Long dateSent) {
    if (dateSent != null) {
      this.dateSent = new Date(dateSent);
    }
  }

  public String getServerResponse() {
    return serverResponse;
  }

  public void setServerResponse(String serverResponse) {
    this.serverResponse = serverResponse;
  }

  public void resetAllRecipients() {
    this.toContact = null;
    this.ccContact = null;
    this.bccContact = null;
  }

  @Override
  public String toString() {
    StringBuffer strBuf = new StringBuffer();
    strBuf.append("DTO id is: " + getId())
        .append("Subject is: " + getSubject())
        .append("To contact List: ")
        .append(getToContact() != null ? getToContact().toString() : null)
        .append("CC contact List: ")
        .append(getCcContact() != null ? getCcContact().toString() : null)
        .append("BCC contact List: ")
        .append(getBccContact() != null ? getBccContact().toString() : null)
        .append("body: ").append(body)
        .append("status: ").append(status)
        .append("Date sent: ").append(dateSent)
        .append("Server Response: ").append(serverResponse)
        .append("attachment: ").append(attachments)
        .append("email type: ").append(emailType)
        .append("message Id: ").append(messageId);
    return strBuf.toString();
  }

  public static enum EMAIL_TYPE {
    TEXT, HTML
  }
}
