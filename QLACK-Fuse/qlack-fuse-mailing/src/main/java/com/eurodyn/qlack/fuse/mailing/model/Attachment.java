package com.eurodyn.qlack.fuse.mailing.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "mai_attachment")
@Getter
@Setter
public class Attachment implements java.io.Serializable {

  @Id
  private String id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "email_id", nullable = false)
  private Email email;

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

  public Attachment() {
    this.id = java.util.UUID.randomUUID().toString();
  }

}
