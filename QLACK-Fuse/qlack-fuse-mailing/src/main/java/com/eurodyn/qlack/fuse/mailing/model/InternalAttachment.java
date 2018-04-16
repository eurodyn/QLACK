package com.eurodyn.qlack.fuse.mailing.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NoResultException;
import javax.persistence.Table;

import java.util.List;

@Entity
@Table(name = "mai_internal_attachment")
public class InternalAttachment implements java.io.Serializable {

  @Id
  private String id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "messages_id", nullable = false)
  private InternalMessages messages;

  @Column(name = "filename", length = 254)
  private String filename;

  @Column(name = "content_type", length = 254)
  private String contentType;

  @Column(name = "data")
  private byte[] data;

  @Column(name = "format", length = 45)
  private String format;

  // -- Constructors

  public InternalAttachment() {
    this.id = java.util.UUID.randomUUID().toString();
  }

  // -- Queries

  public static InternalAttachment findById(EntityManager em, String id) {
    String jpql = "SELECT a FROM InternalAttachment a WHERE a.id = :id";

    try {
      return em.createQuery(jpql, InternalAttachment.class).setParameter("id", id)
          .getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

  public static List<InternalAttachment> findByMessagesId(EntityManager em, String messageId) {
    String jpql = "SELECT a FROM InternalAttachment a WHERE a.messages.id = :messageId";

    return em.createQuery(jpql, InternalAttachment.class).setParameter("messageId", messageId)
        .getResultList();
  }

  // -- Accessors

  public String getId() {
    return this.id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public InternalMessages getMessages() {
    return this.messages;
  }

  public void setMessages(InternalMessages messages) {
    this.messages = messages;
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

  public String getFormat() {
    return this.format;
  }

  public void setFormat(String format) {
    this.format = format;
  }

}
