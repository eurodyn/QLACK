package com.eurodyn.qlack.fuse.mailing.service;

import com.eurodyn.qlack.fuse.mailing.dto.AttachmentDTO;
import com.eurodyn.qlack.fuse.mailing.dto.EmailDTO;
import com.eurodyn.qlack.fuse.mailing.model.Attachment;
import com.eurodyn.qlack.fuse.mailing.model.Email;
import com.eurodyn.qlack.fuse.mailing.monitor.MailQueueMonitor;
import com.eurodyn.qlack.fuse.mailing.util.ConverterUtil;
import com.eurodyn.qlack.fuse.mailing.util.MailConstants.EMAIL_STATUS;
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
public class MailService {

  private MailQueueMonitor mailQueueMonitor;

  @PersistenceContext
  private EntityManager em;

  @Autowired
  public MailService(MailQueueMonitor mailQueueMonitor) {
    this.mailQueueMonitor = mailQueueMonitor;
  }

  /**
   * Queue a list of Emails.
   *
   * @param dtos - list of email data transfer objects.
   * @return List of email ids
   */
  public List<String> queueEmails(List<EmailDTO> dtos) {
    List<String> emails = new ArrayList<>();
    for (EmailDTO dto : dtos) {
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
  public String queueEmail(EmailDTO emailDto) {
    Email email = new Email();

    email.setSubject(emailDto.getSubject());
    email.setBody(emailDto.getBody());
    email.setFromEmail(emailDto.getFrom());
    email.setToEmails(ConverterUtil.createRecepientlist(emailDto.getToContact()));
    email.setCcEmails(ConverterUtil.createRecepientlist(emailDto.getCcContact()));
    email.setBccEmails(ConverterUtil.createRecepientlist(emailDto.getBccContact()));
    email.setReplyToEmails(ConverterUtil.createRecepientlist(emailDto.getReplyToContact()));
    email.setEmailType(emailDto.getEmailType().toString());

    email.setTries((byte) 0);
    email.setStatus(EMAIL_STATUS.QUEUED.toString());
    email.setAddedOnDate(System.currentTimeMillis());

    em.persist(email);

    // Process attachements.
    if (emailDto.getAttachments() != null && !emailDto.getAttachments().isEmpty()) {
      Set<Attachment> attachments = new HashSet<Attachment>();
      for (AttachmentDTO attachmentDto : emailDto.getAttachments()) {
        Attachment attachment = new Attachment();

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
    List<Email> emails = Email.findByDateAndStatus(em, date, status);
    for (Email email : emails) {
      em.remove(email);
    }
  }

  /**
   * Delete an email from the queue.
   *
   * @param emailId - the email id.
   */
  public void deleteFromQueue(String emailId) {
    Email email = Email.find(em, emailId);
    em.remove(email);
  }

  public void updateStatus(String emailId, EMAIL_STATUS status) {
    Email email = Email.find(em, emailId);
    email.setStatus(status.toString());
  }

  public EmailDTO getMail(String emailId) {
    Email email = Email.find(em, emailId);
    if (email == null) {
      return null;
    }

    return ConverterUtil.emailConvert(email);
  }

  public List<EmailDTO> getByStatus(EMAIL_STATUS status) {
    return Email.findByDateAndStatus(em, null, status)
        .stream()
        .map(o -> ConverterUtil.emailConvert(o))
        .collect(Collectors.toList());
  }

  public void sendOne(String emailId) {
    mailQueueMonitor.sendOne(emailId);
  }

}
