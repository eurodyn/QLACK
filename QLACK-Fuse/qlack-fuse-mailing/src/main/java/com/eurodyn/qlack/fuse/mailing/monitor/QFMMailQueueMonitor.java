package com.eurodyn.qlack.fuse.mailing.monitor;


import com.eurodyn.qlack.fuse.mailing.dto.QFMAttachmentDTO;
import com.eurodyn.qlack.fuse.mailing.dto.QFMEmailDTO;
import com.eurodyn.qlack.fuse.mailing.exception.QFMMailingException;
import com.eurodyn.qlack.fuse.mailing.model.QFMAttachment;
import com.eurodyn.qlack.fuse.mailing.model.QFMEmail;
import com.eurodyn.qlack.fuse.mailing.util.QFMConverterUtil;
import com.eurodyn.qlack.fuse.mailing.util.QFMMailConstants.EMAIL_STATUS;
import com.eurodyn.qlack.fuse.mailing.util.QFMailingProperties;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Monitor email queue.
 *
 * @author European Dynamics SA
 */
@Service
@Validated
@Transactional(noRollbackFor = {QFMMailingException.class})
public class QFMMailQueueMonitor {

  /**
   * Logger reference
   */
  private static final Logger LOGGER = Logger.getLogger(QFMMailQueueMonitor.class.getName());

  @PersistenceContext
  private EntityManager em;

  // Service references.
  private QFMMailQueueSender mailQueueSender;
  private QFMailingProperties qfMailingProperties;

  @Autowired
  public QFMMailQueueMonitor(QFMMailQueueSender mailQueueSender,
      QFMailingProperties qfMailingProperties) {
    this.mailQueueSender = mailQueueSender;
    this.qfMailingProperties = qfMailingProperties;
  }

  private void send(QFMEmail email) {
    /** Create a DTO for the email about to be sent */
    QFMEmailDTO dto = new QFMEmailDTO();
    dto.setId(email.getId());
    dto.setSubject(email.getSubject());
    dto.setBody(email.getBody());
    dto.setFrom(email.getFromEmail());
    if (email.getToEmails() != null) {
      dto.setToContact(QFMConverterUtil.createRecepientlist(email.getToEmails()));
    }
    if (email.getCcEmails() != null) {
      dto.setCcContact(QFMConverterUtil.createRecepientlist(email.getCcEmails()));
    }
    if (email.getBccEmails() != null) {
      dto.setBccContact(QFMConverterUtil.createRecepientlist(email.getBccEmails()));
    }
    if (email.getReplyToEmails() != null) {
      dto.setReplyToContact(QFMConverterUtil.createRecepientlist(email.getReplyToEmails()));
    }
    if (email.getEmailType().equals("HTML")) {
      dto.setEmailType(QFMEmailDTO.EMAIL_TYPE.HTML);
    } else {
      dto.setEmailType(QFMEmailDTO.EMAIL_TYPE.TEXT);
    }

    /** Process attachments. */
    Set<QFMAttachment> attachments = email.getAttachments();
    for (QFMAttachment attachment : attachments) {
      QFMAttachmentDTO attachmentDTO = new QFMAttachmentDTO();
      attachmentDTO.setContentType(attachment.getContentType());
      attachmentDTO.setData(attachment.getData());
      attachmentDTO.setFilename(attachment.getFilename());
      dto.addAttachment(attachmentDTO);
    }

    /** Update email's tries and date sent in the database, irrespectively of the outcome of the
     * sendig process.
     */
    email.setTries((byte) (email.getTries() + 1));

    /** Try to send the email */
    try {
      mailQueueSender.send(dto);
      email.setDateSent(System.currentTimeMillis());

      /** If the email was sent successfully, we can update its status to Sent, so that the scheduler
       * does not try to resend it.
       */
      email.setStatus(EMAIL_STATUS.SENT.toString());
    } catch (QFMMailingException ex) {
      LOGGER.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
      /** Set the reason for failure in the database */
      Throwable t = ex.getCause() != null ? ex.getCause() : ex;
      email.setServerResponse(t.getLocalizedMessage());
      email.setServerResponseDate(System.currentTimeMillis());
      /** If anything went wrong during delivery check if the maximum attempts have been reached
       * and mark the email as Failed in that case.
       */
      if (email.getTries() >= qfMailingProperties.getMaxTries()) {
        email.setStatus(EMAIL_STATUS.FAILED.toString());
      }
    }
    em.merge(email);
  }

  public void sendOne(String emailId) {
    send(QFMEmail.find(em, emailId));
  }

  /**
   * Check for QUEUED emails and send them.
   */
  @Scheduled(initialDelay = 30000, fixedDelay = 10000)
  public void checkAndSendQueued() {
    List<QFMEmail> emails = QFMEmail.findQueued(em, qfMailingProperties.getMaxTries());
    LOGGER.log(Level.FINEST, "Found {0} email(s) to be sent.", emails.size());

    for (QFMEmail email : emails) {
      send(email);
    }
  }

}
