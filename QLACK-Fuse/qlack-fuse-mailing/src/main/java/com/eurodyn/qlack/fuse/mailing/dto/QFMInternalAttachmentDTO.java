package com.eurodyn.qlack.fuse.mailing.dto;

/**
 * Data transfer object for internal messages attachments.
 *
 * @author European Dynamics SA.
 */
public class QFMInternalAttachmentDTO extends QFMMailBaseDTO {

  private String messagesId;
  private String filename;
  private String contentType;
  private byte[] data;
  private String format;

  // -- Accessors

  public String getMessagesId() {
    return messagesId;
  }

  public void setMessagesId(String messagesId) {
    this.messagesId = messagesId;
  }

  public String getFilename() {
    return filename;
  }

  public void setFilename(String filename) {
    this.filename = filename;
  }

  public String getContentType() {
    return contentType;
  }

  public void setContentType(String contentType) {
    this.contentType = contentType;
  }

  public byte[] getData() {
    return data;
  }

  public void setData(byte[] data) {
    this.data = data;
  }

  public String getFormat() {
    return format;
  }

  public void setFormat(String format) {
    this.format = format;
  }
}
