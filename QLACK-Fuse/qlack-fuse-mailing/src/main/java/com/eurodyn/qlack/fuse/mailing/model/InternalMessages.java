package com.eurodyn.qlack.fuse.mailing.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TypedQuery;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "mai_internal_messages")
@Getter
@Setter
public class InternalMessages implements java.io.Serializable {

  @Id
  private String id;

  @Column(name = "subject", nullable = false, length = 100)
  private String subject;

  @Column(name = "message", nullable = false, length = 65535)
  @Lob
  private String message;

  @Column(name = "mail_from", nullable = false, length = 36)
  private String mailFrom;

  @Column(name = "mail_to", nullable = false, length = 36)
  private String mailTo;

  @Column(name = "date_sent")
  private Long dateSent;

  @Column(name = "date_received")
  private Long dateReceived;

  @Column(name = "status", length = 7)
  private String status;

  @Column(name = "delete_type", nullable = false, length = 1)
  private String deleteType;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "messages")
  private Set<InternalAttachment> attachments = new HashSet<InternalAttachment>(0);

  // -- Constructors

  public InternalMessages() {
    this.id = java.util.UUID.randomUUID().toString();
  }

  // -- Queries

  public static List<InternalMessages> findAll(EntityManager em) {
    String jpql = "SELECT m FROM InternalMessages m";

    return em.createQuery(jpql, InternalMessages.class).getResultList();
  }

  public static List<InternalMessages> findUserInbox(EntityManager em, String userId) {
    String jpql =
        "SELECT m FROM InternalMessages m " +
            "WHERE m.mailTo = :userId AND m.deleteType <> 'I'";

    return em.createQuery(jpql, InternalMessages.class)
        .setParameter("userId", userId)
        .getResultList();
  }

  public static List<InternalMessages> findUserSent(EntityManager em, String userId) {
    String jpql =
        "SELECT m FROM InternalMessages m " +
            "WHERE m.mailFrom = :userId AND m.deleteType <> 'S'";

    return em.createQuery(jpql, InternalMessages.class)
        .setParameter("userId", userId)
        .getResultList();
  }

  public static long countByUserAndStatus(EntityManager em, String userId, String status) {
    String select = "SELECT count(m) FROM InternalMessages m";

    List<String> predicates = new ArrayList<>(2);
    if (userId != null) {
      predicates.add("(m.mailTo = :userId AND m.deleteType <> 'I')");
    }
    if (status != null) {
      predicates.add("(UPPER(m.status) = :status)");
    }

    // open-coded join()
    StringBuilder sb = new StringBuilder(select);
    Iterator<String> iter = predicates.iterator();
    if (iter.hasNext()) {
      sb.append(" WHERE ").append(iter.next());
      while (iter.hasNext()) {
        sb.append(" AND ").append(iter.next());
      }
    }
    String jpql = sb.toString();

    TypedQuery<Long> q = em.createQuery(jpql, Long.class);
    if (userId != null) {
      q.setParameter("mailTo", userId);
    }
    if (status != null) {
      q.setParameter("status", status.toUpperCase());
    }

    return q.getSingleResult();
  }

}
