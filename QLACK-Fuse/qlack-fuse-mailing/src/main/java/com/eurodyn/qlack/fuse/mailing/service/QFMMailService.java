package com.eurodyn.qlack.fuse.mailing.service;

import com.eurodyn.qlack.fuse.mailing.dto.QFMAttachmentDTO;
import com.eurodyn.qlack.fuse.mailing.dto.QFMEmailDTO;
import com.eurodyn.qlack.fuse.mailing.model.QFMAttachment;
import com.eurodyn.qlack.fuse.mailing.model.QFMEmail;
import com.eurodyn.qlack.fuse.mailing.monitor.QFMMailQueueMonitor;
import com.eurodyn.qlack.fuse.mailing.util.QFMConverterUtil;
import com.eurodyn.qlack.fuse.mailing.util.QFMMailConstants.EMAIL_STATUS;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Bean implementation for Send and Search Mail functionality
 *
 * @author European Dynamics SA.
 */
@Transactional
@Service
@Validated
public class QFMMailService {

  private QFMMailQueueMonitor mailQueueMonitor;

  @PersistenceContext
  private EntityManager em;

  @Autowired
  public QFMMailService(QFMMailQueueMonitor mailQueueMonitor) {
    this.mailQueueMonitor = mailQueueMonitor;
  }

  /**
   * Queue a list of Emails.
   *
   * @param dtos - list of email data transfer objects.
   * @return List of email ids
   */
  public List<String> queueEmails(List<QFMEmailDTO> dtos) {
    List<String> emails = new ArrayList<>();
    for (QFMEmailDTO dto : dtos) {
      emails.add(queueEmail(dto));
    }

    return emails;
  }

  /**
   * Queue an email.
   *
   * @param emailDto - an email data transfer object.
   * @return email id
   */
  public String queueEmail(QFMEmailDTO emailDto) {
    QFMEmail email = new QFMEmail();

    email.setSubject(emailDto.getSubject());
    email.setBody(emailDto.getBody());
    email.setFromEmail(emailDto.getFrom());
    email.setToEmails(QFMConverterUtil.createRecepientlist(emailDto.getToContact()));
    email.setCcEmails(QFMConverterUtil.createRecepientlist(emailDto.getCcContact()));
    email.setBccEmails(QFMConverterUtil.createRecepientlist(emailDto.getBccContact()));
    email.setReplyToEmails(QFMConverterUtil.createRecepientlist(emailDto.getReplyToContact()));
    email.setEmailType(emailDto.getEmailType().toString());

    email.setTries((byte) 0);
    email.setStatus(EMAIL_STATUS.QUEUED.toString());
    email.setAddedOnDate(System.currentTimeMillis());

    em.persist(email);

    // Process attachements.
    if (emailDto.getAttachments() != null && !emailDto.getAttachments().isEmpty()) {
      Set<QFMAttachment> attachments = new HashSet<QFMAttachment>();
      for (QFMAttachmentDTO attachmentDto : emailDto.getAttachments()) {
        QFMAttachment attachment = new QFMAttachment();

        attachment.setEmail(email);
        attachment.setFilename(attachmentDto.getFilename());
        attachment.setContentType(attachmentDto.getContentType());
        attachment.setData(attachmentDto.getData());
        attachment.setAttachmentSize(Long.valueOf(attachmentDto.getData().length));

        attachments.add(attachment);

        em.persist(attachment);
      }
      email.setAttachments(attachments);
    }

    return email.getId();
  }

  /**
   * Removes all e-mails prior to the specified date having the requested status. Warning: If you
   * pass a <code>null</code> date all emails irrespectively of date will be removed.
   *
   * @param date the date before which all e-mails will be removed.
   * @param status the status to be processed. Be cautious to not include e-mails of status QUEUED
   * as such e-mails might not have been tried to be delivered yet.
   */
  public void cleanup(Long date, EMAIL_STATUS[] status) {
    List<QFMEmail> emails = QFMEmail.findByDateAndStatus(em, date, status);
    for (QFMEmail email : emails) {
      em.remove(email);
    }
  }

  /**
   * Delete an email from the queue.
   *
   * @param emailId - the email id.
   */
  public void deleteFromQueue(String emailId) {
    QFMEmail email = QFMEmail.find(em, emailId);
    em.remove(email);
  }

  public void updateStatus(String emailId, EMAIL_STATUS status) {
    QFMEmail email = QFMEmail.find(em, emailId);
    email.setStatus(status.toString());
  }

  public QFMEmailDTO getMail(String emailId) {
    QFMEmail email = QFMEmail.find(em, emailId);
    if (email == null) {
      return null;
    }

    return QFMConverterUtil.emailConvert(email);
  }

  public List<QFMEmailDTO> getByStatus(EMAIL_STATUS status) {
    return QFMEmail.findByDateAndStatus(em, null, status)
        .stream()
        .map(o -> QFMConverterUtil.emailConvert(o))
        .collect(Collectors.toList());
  }

  public void sendOne(String emailId) {
    mailQueueMonitor.sendOne(emailId);
  }

}
