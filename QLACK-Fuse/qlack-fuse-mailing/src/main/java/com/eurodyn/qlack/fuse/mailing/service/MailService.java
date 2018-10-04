package com.eurodyn.qlack.fuse.mailing.service;

import com.eurodyn.qlack.fuse.mailing.dto.AttachmentDTO;
import com.eurodyn.qlack.fuse.mailing.dto.EmailDTO;
import com.eurodyn.qlack.fuse.mailing.mappers.AttachmentMapper;
import com.eurodyn.qlack.fuse.mailing.mappers.EmailMapper;
import com.eurodyn.qlack.fuse.mailing.model.Attachment;
import com.eurodyn.qlack.fuse.mailing.model.Email;
import com.eurodyn.qlack.fuse.mailing.monitor.MailQueueMonitor;
import com.eurodyn.qlack.fuse.mailing.repository.AttachmentRepository;
import com.eurodyn.qlack.fuse.mailing.repository.EmailRepository;
//import com.eurodyn.qlack.fuse.mailing.util.ConverterUtil;
import com.eurodyn.qlack.fuse.mailing.util.MailConstants.EMAIL_STATUS;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.validation.Valid;
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

  private final MailQueueMonitor mailQueueMonitor;
  private final EmailRepository emailRepository;
  private final AttachmentRepository attachmentRepository;
  
  private EmailMapper emailMapper;
  private AttachmentMapper attachmentMapper;

//  @PersistenceContext
//  private EntityManager em;

	@Autowired
	public MailService(MailQueueMonitor mailQueueMonitor, EmailMapper emailMapper, EmailRepository emailRepository,
			AttachmentMapper attachmentMapper, AttachmentRepository attachmentRepository) {
		this.mailQueueMonitor = mailQueueMonitor;
		this.emailMapper = emailMapper;
		this.emailRepository = emailRepository;
		this.attachmentRepository = attachmentRepository;
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
  public String queueEmail(@Valid EmailDTO emailDto) {
    Email email = new Email();
    
    email = emailMapper.mapToEntity(emailDto);
//    email.setSubject(emailDto.getSubject());
//    email.setBody(emailDto.getBody());
//    email.setFromEmail(emailDto.getFrom());
//    email.setToEmails(ConverterUtil.createRecepientlist(emailDto.getToContact()));
//    email.setCcEmails(ConverterUtil.createRecepientlist(emailDto.getCcContact()));
//    email.setBccEmails(ConverterUtil.createRecepientlist(emailDto.getBccContact()));
//    email.setReplyToEmails(ConverterUtil.createRecepientlist(emailDto.getReplyToContact()));
//    email.setEmailType(emailDto.getEmailType().toString());

    email.setTries((byte) 0);
    email.setStatus(EMAIL_STATUS.QUEUED.toString());
    email.setAddedOnDate(System.currentTimeMillis());

    System.out.println("SAVING "+email.getFromEmail());
    
    emailRepository.save(email);
//    em.persist(email);

    // Process attachements.
    if (emailDto.getAttachments() != null && !emailDto.getAttachments().isEmpty()) {
      Set<Attachment> attachments = new HashSet<Attachment>();
      for (AttachmentDTO attachmentDto : emailDto.getAttachments()) {
        Attachment attachment = new Attachment();
        attachment.setEmail(email);
        
        attachment = attachmentMapper.mapToEntity(attachmentDto);

//        attachment.setFilename(attachmentDto.getFilename());
//        attachment.setContentType(attachmentDto.getContentType());
//        attachment.setData(attachmentDto.getData());
        // VERIFY
        attachment.setAttachmentSize(Long.valueOf(attachmentDto.getData().length));

        attachments.add(attachment);

        attachmentRepository.save(attachment);
//        em.persist(attachment);
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
    List<Email> emails = emailRepository.findByAddedOnDateAndStatus(date, status); 
    		//Email.findByDateAndStatus(em, date, status);
    
    for (Email email : emails) {
//      em.remove(email);
    	emailRepository.delete(email);
    }
  }

  /**
   * Delete an email from the queue.
   *
   * @param emailId - the email id.
   */
  public void deleteFromQueue(String emailId) {
	  // Email email = emailRepository.fetchById(emailId); 
	  //Email.find(em, emailId);
	  //	  em.remove(email);
	  emailRepository.deleteById(emailId);
  }

	public void updateStatus(String emailId, EMAIL_STATUS status) {
		Email email = emailRepository.fetchById(emailId);
		// Email.find(em, emailId);
		email.setStatus(status.toString());
	}

  public EmailDTO getMail(String emailId) {
    Email email = emailRepository.fetchById(emailId); 
    		//Email.find(em, emailId);
    if (email == null) {
      return null;
    }
    return emailMapper.mapToDTO(email);
    		//ConverterUtil.emailConvert(email);
  }

	public List<EmailDTO> getByStatus(EMAIL_STATUS status) {
		return // Email.findByDateAndStatus(em, null, status)
		emailRepository.findByAddedOnDateAndStatus(null, status).stream().map(o -> 
		//ConverterUtil.emailConvert(o)
		emailMapper.mapToDTO(o)
		)
				.collect(Collectors.toList());
	}

  public void sendOne(String emailId) {
    mailQueueMonitor.sendOne(emailId);
  }

}
