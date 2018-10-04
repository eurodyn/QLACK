package com.eurodyn.qlack.fuse.mailing.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO for Email data.
 */
@Getter
@Setter
@ToString
public class EmailDTO implements Serializable {

  private String id;

//  private String messageId;
  private @NotBlank String subject;
  private @NotBlank String body;
  private @NotBlank String fromEmail;
  private @NotEmpty List<String> toEmails;
  private List<String> ccEmails;
  private List<String> bccEmails;
  private List<String> replyToEmails;
  private @NotNull EMAIL_TYPE emailType;
  private String status;
  private List<AttachmentDTO> attachments;
  private Date dateSent;
  private String serverResponse;
  private Date serverResponseDate;

  public EmailDTO() {
    this.emailType = EMAIL_TYPE.TEXT;
  }

  public void setToContact(List<String> toEmails) {
    this.toEmails = toEmails;
  }

  public void setToContact(String toContact) {
    List<String> l = new ArrayList<String>();
    l.add(toContact);
    setToContact(l);
  }

  public void addAttachment(AttachmentDTO attachmentDTO) {
    if (attachments == null) {
      attachments = new ArrayList<>();
    }
    attachments.add(attachmentDTO);
  }

  public void setDateSent(Long dateSent) {
    if (dateSent != null) {
      this.dateSent = new Date(dateSent);
    }
  }

  public void resetAllRecipients() {
    this.toEmails = null;
    this.ccEmails = null;
    this.bccEmails = null;
  }

//  @Override
//  public String toString() {
//    StringBuffer strBuf = new StringBuffer();
//    strBuf.append("DTO id is: " + getId())
//        .append("Subject is: " + getSubject())
//        .append("To contact List: ")
//        .append(getToContact() != null ? getToContact().toString() : null)
//        .append("CC contact List: ")
//        .append(getCcContact() != null ? getCcContact().toString() : null)
//        .append("BCC contact List: ")
//        .append(getBccContact() != null ? getBccContact().toString() : null)
//        .append("body: ").append(body)
//        .append("status: ").append(status)
//        .append("Date sent: ").append(dateSent)
//        .append("Server Response: ").append(serverResponse)
//        .append("attachment: ").append(attachments)
//        .append("email type: ").append(emailType)
////        .append("message Id: ").append(messageId);
//    return strBuf.toString();
//  }

  public static enum EMAIL_TYPE {
    TEXT, HTML
  }

}
