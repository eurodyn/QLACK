package com.eurodyn.qlack.fuse.mailing.monitor;


import com.eurodyn.qlack.fuse.mailing.dto.AttachmentDTO;
import com.eurodyn.qlack.fuse.mailing.dto.EmailDTO;
import com.eurodyn.qlack.fuse.mailing.exception.MailingException;
import com.eurodyn.qlack.fuse.mailing.mappers.AttachmentMapper;
import com.eurodyn.qlack.fuse.mailing.mappers.EmailMapper;
import com.eurodyn.qlack.fuse.mailing.mappers.MailingMapper;
import com.eurodyn.qlack.fuse.mailing.model.Attachment;
import com.eurodyn.qlack.fuse.mailing.model.Email;
import com.eurodyn.qlack.fuse.mailing.model.QEmail;
import com.eurodyn.qlack.fuse.mailing.repository.EmailRepository;
import com.eurodyn.qlack.fuse.mailing.util.ConverterUtil;
import com.eurodyn.qlack.fuse.mailing.util.MailConstants.EMAIL_STATUS;
import com.eurodyn.qlack.fuse.mailing.util.MailingProperties;
import com.querydsl.core.types.Predicate;

//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
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
@Transactional(noRollbackFor = {MailingException.class})
public class MailQueueMonitor {

  /**
   * Logger reference
   */
  private static final Logger LOGGER = Logger.getLogger(MailQueueMonitor.class.getName());

  // Service references.
  private MailQueueSender mailQueueSender;
  private MailingProperties mailingProperties;
  private EmailMapper emailMapper;
  private AttachmentMapper attachmentMapper;
  
  private final EmailRepository emailRepository; 
  
  private static QEmail qEmail = QEmail.email;

  @Autowired
  public MailQueueMonitor(MailQueueSender mailQueueSender, MailingProperties mailingProperties,
		EmailRepository emailRepository, EmailMapper emailMapper, AttachmentMapper attachmentMapper) {
	this.mailQueueSender = mailQueueSender;
	this.mailingProperties = mailingProperties;
	this.emailRepository = emailRepository;
	this.emailMapper = emailMapper;
	this.attachmentMapper = attachmentMapper;
}
  
	private void send(Email email) {
		/** Create a DTO for the email about to be sent */
		EmailDTO dto = new EmailDTO();

		dto = emailMapper.mapToDTOyWithRecipilents(email, true);

		//// dto.setId(email.getId());
		//// dto.setSubject(email.getSubject());
		//// dto.setBody(email.getBody());
		//// dto.setFrom(email.getFromEmail());
		////
		// if (email.getToEmails() != null) {
		// dto.setToContact(ConverterUtil.createRecepientlist(email.getToEmails()));
		// }
		// if (email.getCcEmails() != null) {
		// dto.setCcContact(ConverterUtil.createRecepientlist(email.getCcEmails()));
		// }
		// if (email.getBccEmails() != null) {
		// dto.setBccContact(ConverterUtil.createRecepientlist(email.getBccEmails()));
		// }
		// if (email.getReplyToEmails() != null) {
		// dto.setReplyToContact(ConverterUtil.createRecepientlist(email.getReplyToEmails()));
		// }
		// if (email.getEmailType().equals("HTML")) {
		// dto.setEmailType(EmailDTO.EMAIL_TYPE.HTML);
		// } else {
		// dto.setEmailType(EmailDTO.EMAIL_TYPE.TEXT);
		// }

		/** Process attachments. */
		Set<Attachment> attachments = email.getAttachments();
		for (Attachment attachment : attachments) {
			AttachmentDTO attachmentDTO = new AttachmentDTO();
			attachmentDTO = attachmentMapper.mapToDTO(attachment);
//			attachmentDTO.setContentType(attachment.getContentType());
//			attachmentDTO.setData(attachment.getData());
//			attachmentDTO.setFilename(attachment.getFilename());
			dto.addAttachment(attachmentDTO);
		}

		/**
		 * Update email's tries and date sent in the database, irrespectively of the
		 * outcome of the sendig process.
		 */
		email.setTries((byte) (email.getTries() + 1));

		/** Try to send the email */
		try {
			mailQueueSender.send(dto);
			email.setDateSent(System.currentTimeMillis());

			/**
			 * If the email was sent successfully, we can update its status to Sent, so that
			 * the scheduler does not try to resend it.
			 */
			email.setStatus(EMAIL_STATUS.SENT.toString());
		} catch (MailingException ex) {
			LOGGER.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
			/** Set the reason for failure in the database */
			Throwable t = ex.getCause() != null ? ex.getCause() : ex;
			email.setServerResponse(t.getLocalizedMessage());
			email.setServerResponseDate(System.currentTimeMillis());
			/**
			 * If anything went wrong during delivery check if the maximum attempts have
			 * been reached and mark the email as Failed in that case.
			 */
			if (email.getTries() >= mailingProperties.getMaxTries()) {
				email.setStatus(EMAIL_STATUS.FAILED.toString());
			}
		}
		emailRepository.save(email);
		// em.merge(email);
	}



public void sendOne(String emailId) {
    send(emailRepository.fetchById(emailId));
    		//Email.find(em, emailId));
  }

  /**
   * Check for QUEUED emails and send them.
   */
  @Scheduled(initialDelay = 30000, fixedDelay = 5000)
  public void checkAndSendQueued() {
    if (mailingProperties.isPolling()) {
    	
    	Predicate predicate = qEmail.status.eq(EMAIL_STATUS.QUEUED.toString()).and(qEmail.tries.lt(mailingProperties.getMaxTries()));
    	
    	  List<Email> emails = 	emailRepository.findAll(predicate);
//    	
/*    	  public static List<Email> findQueued(EntityManager em, byte maxTries) {
    	    String jpql =
    	        "SELECT m FROM Email m " +
    	            "WHERE m.status = :status AND m.tries < :tries";

    	    return em.createQuery(jpql, Email.class)
    	        .setParameter("status", EMAIL_STATUS.QUEUED.toString())
    	        .setParameter("tries", maxTries)
    	        .getResultList();
    	  }
    	*/
//    	emailRepository.
    	  //emailRepository.findQueued(mailingProperties.getMaxTries());
    	  //Email.findQueued(em, mailingProperties.getMaxTries());
    	
      LOGGER.log(Level.FINEST, "Found {0} email(s) to be sent.", emails.size());

      for (Email email : emails) {
        send(email);
      }
    }
  }

}
