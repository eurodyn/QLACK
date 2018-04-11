package com.eurodyn.qlack.fuse.mailing.monitor;

import com.eurodyn.qlack.fuse.mailing.dto.AttachmentDTO;
import com.eurodyn.qlack.fuse.mailing.dto.EmailDTO;
import com.eurodyn.qlack.fuse.mailing.dto.EmailDTO.EMAIL_TYPE;
import com.eurodyn.qlack.fuse.mailing.exception.QMailingException;
import com.eurodyn.qlack.fuse.mailing.util.QFMailingProperties;
import javax.activation.DataSource;
import javax.mail.util.ByteArrayDataSource;
import javax.validation.Valid;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.MultiPartEmail;
import org.apache.commons.mail.SimpleEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.Arrays;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@Validated
@Transactional(noRollbackFor = {QMailingException.class})
public class MailQueueSender {

   // Logger reference
  private static final Logger LOGGER = Logger.getLogger(MailQueueSender.class.getName());

  // Service refs.
  private QFMailingProperties qfMailingProperties;

  @Autowired
  public MailQueueSender(QFMailingProperties qfMailingProperties) {
    this.qfMailingProperties = qfMailingProperties;
  }

  /**
   * Setup commons attributes of the email not related to its type.
   *
   * @param email The Email to set the attributes to.
   * @param vo The DTO with the attributes to set.
   * @throws EmailException Indicating an error while setting recipients.
   */
  private void setupCommons(Email email, @Valid EmailDTO vo) throws EmailException {
    email.setHostName(qfMailingProperties.getServerHost());
    email.setSmtpPort(qfMailingProperties.getServerPort());
    email.setFrom(vo.getFrom());
    email.setSubject(vo.getSubject());
    email.setSentDate(new Date());

    if (CollectionUtils.isNotEmpty(vo.getToContact())) {
      for (String recipient : vo.getToContact()) {
        email.addTo(recipient);
      }
    }

    if (CollectionUtils.isNotEmpty(vo.getCcContact())) {
      for (String recipient : vo.getCcContact()) {
        email.addCc(recipient);
      }
    }

    if (CollectionUtils.isNotEmpty(vo.getBccContact())) {
      for (String recipient : vo.getBccContact()) {
        email.addBcc(recipient);
      }
    }

    if (CollectionUtils.isNotEmpty(vo.getReplyToContact())) {
      for (String recipient : vo.getReplyToContact()) {
        email.addReplyTo(recipient);
      }
    }
  }

  /**
   * Attaches email attachments.
   *
   * @param email The email to attach the attachments to.
   * @param vo The DTO with the attachments to attach.
   * @throws EmailException Indicating an error while processing attachments.
   */
  private void setupAttachments(MultiPartEmail email, EmailDTO vo) throws EmailException {
    if (!CollectionUtils.isEmpty(vo.getAttachments())) {
      for (AttachmentDTO attachmentDTO : vo.getAttachments()) {
        DataSource source = new ByteArrayDataSource(attachmentDTO.getData(),
            attachmentDTO.getContentType());
        email.attach(source, attachmentDTO.getFilename(), attachmentDTO.getFilename());
      }
    }
  }

  /**
   * Send the email.
   */
  public void send(EmailDTO vo) throws QMailingException {
    Email email;

    try {
      if (vo.getEmailType() == EMAIL_TYPE.HTML) { // HTML email
        email = new HtmlEmail();
        setupAttachments(((HtmlEmail) email), vo);
        ((HtmlEmail) email).setHtmlMsg(vo.getBody());
      } else {  // Plaintext email
        if (!CollectionUtils.isEmpty(vo.getAttachments())) {
          email = new MultiPartEmail();
          setupAttachments(((MultiPartEmail) email), vo);
        } else {
          email = new SimpleEmail();
        }
        email.setMsg(vo.getBody());
      }
      setupCommons(email, vo);

      LOGGER.log(Level.FINEST, "Sending email {0} to {1} with TLS={2}.", new Object[]{
          vo.getSubject(), Arrays.asList(vo.getToContact()), qfMailingProperties.isStartTls()
      });

      /** Enable authentication */
      if (StringUtils.isNotBlank(qfMailingProperties.getServerUsername()) && StringUtils
          .isNotBlank(qfMailingProperties.getServerPassword())) {
        email.setAuthentication(qfMailingProperties.getServerUsername(),
            qfMailingProperties.getServerPassword());
      }

      /** Enable STARTTLS */
      email.setStartTLSRequired(qfMailingProperties.isStartTls());

      email.send();
    } catch (Exception e) {
      throw new QMailingException("There was a problem sending email.", e);
    }
  }
}
