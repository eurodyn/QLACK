package com.eurodyn.qlack.fuse.mailing.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "mai_attachment")
public class QFMAttachment implements java.io.Serializable {

  @Id
  private String id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "email_id", nullable = false)
  private QFMEmail email;

  @Column(name = "filename", nullable = false, length = 254)
  private String filename;

  @Column(name = "content_type", nullable = false, length = 254)
  private String contentType;

  @Lob
  @Column(name = "data", nullable = false)
  private byte[] data;

  @Column(name = "attachment_size")
  private Long attachmentSize;

  // -- Constructors

  public QFMAttachment() {
    this.id = java.util.UUID.randomUUID().toString();
  }

  // -- Accessors

  public String getId() {
    return this.id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public QFMEmail getEmail() {
    return this.email;
  }

  public void setEmail(QFMEmail email) {
    this.email = email;
  }

  public String getFilename() {
    return this.filename;
  }

  public void setFilename(String filename) {
    this.filename = filename;
  }

  public String getContentType() {
    return this.contentType;
  }

  public void setContentType(String contentType) {
    this.contentType = contentType;
  }

  public byte[] getData() {
    return this.data;
  }

  public void setData(byte[] data) {
    this.data = data;
  }

  public Long getAttachmentSize() {
    return this.attachmentSize;
  }

  public void setAttachmentSize(Long attachmentSize) {
    this.attachmentSize = attachmentSize;
  }

}
