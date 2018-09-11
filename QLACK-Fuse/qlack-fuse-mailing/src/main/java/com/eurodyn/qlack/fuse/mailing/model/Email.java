package com.eurodyn.qlack.fuse.mailing.model;

import com.eurodyn.qlack.fuse.mailing.util.MailConstants.EMAIL_STATUS;
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
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "mai_email")
@Getter
@Setter
public class Email implements java.io.Serializable {

  @Id
  private String id;

  @Column(name = "subject", length = 254)
  private String subject;

  @Column(name = "body", length = 65535)
  @Lob
  private String body;

  @Column(name = "from_email")
  private String fromEmail;

  @Column(name = "to_emails", length = 1024)
  private String toEmails;

  @Column(name = "cc_emails", length = 1024)
  private String ccEmails;

  @Column(name = "bcc_emails", length = 1024)
  private String bccEmails;

  @Column(name = "reply_to_emails", length = 1024)
  private String replyToEmails;

  @Column(name = "email_type", length = 64)
  private String emailType;

  @Column(name = "status", length = 32)
  private String status;

  @Column(name = "tries", nullable = false)
  private byte tries;

  @Column(name = "added_on_date", nullable = false)
  private long addedOnDate;

  @Column(name = "date_sent")
  private Long dateSent;

  @Column(name = "server_response_date")
  private Long serverResponseDate;

  @Column(name = "server_response", length = 1024)
  private String serverResponse;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "email")
  private Set<Attachment> attachments = new HashSet<Attachment>(0);

  // -- Constructors

  public Email() {
    this.id = java.util.UUID.randomUUID().toString();
  }

  // -- Queries
  public static Email find(EntityManager em, String id) {
    return em.find(Email.class, id);
  }

  public static List<Email> findQueued(EntityManager em, byte maxTries) {
    String jpql =
        "SELECT m FROM Email m " +
            "WHERE m.status = :status AND m.tries < :tries";

    return em.createQuery(jpql, Email.class)
        .setParameter("status", EMAIL_STATUS.QUEUED.toString())
        .setParameter("tries", maxTries)
        .getResultList();
  }

  public static List<Email> findByDateAndStatus(EntityManager em, Long date,
      EMAIL_STATUS... statuses) {
    String select = "SELECT m FROM Email m ";

    List<String> predicates = new ArrayList<>(2);
    if (date != null) {
      predicates.add("(addedOnDate <= " + date.longValue() + ")");
    }
    if (statuses != null && statuses.length > 0) {
      // open-coded join()
      StringBuilder sb = new StringBuilder("(status IN ('");
      sb.append(statuses[0].toString());
      for (int i = 1; i < statuses.length; i++) {
        sb.append("',' ").append(statuses[i].toString());
      }
      sb.append("'))");
      predicates.add(sb.toString());
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

    return em.createQuery(jpql, Email.class).getResultList();
  }

}
