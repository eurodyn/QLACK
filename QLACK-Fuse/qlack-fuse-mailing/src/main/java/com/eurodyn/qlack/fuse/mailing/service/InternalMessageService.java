package com.eurodyn.qlack.fuse.mailing.service;


import com.eurodyn.qlack.fuse.mailing.dto.InternalAttachmentDTO;
import com.eurodyn.qlack.fuse.mailing.dto.InternalMessagesDTO;
import com.eurodyn.qlack.fuse.mailing.mappers.AttachmentMapper;
import com.eurodyn.qlack.fuse.mailing.mappers.InternalAttachmentMapper;
import com.eurodyn.qlack.fuse.mailing.mappers.InternalMessagesMapper;
import com.eurodyn.qlack.fuse.mailing.model.InternalAttachment;
import com.eurodyn.qlack.fuse.mailing.model.InternalMessages;
import com.eurodyn.qlack.fuse.mailing.model.QInternalMessages;
import com.eurodyn.qlack.fuse.mailing.repository.InternalAttachmentRepository;
import com.eurodyn.qlack.fuse.mailing.repository.InternalMessagesRepository;
//import com.eurodyn.qlack.fuse.mailing.util.ConverterUtil;
import com.eurodyn.qlack.fuse.mailing.util.MailConstants;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Provide internal messages related services. For details regarding the functionality offered see
 * the respective interfaces.
 *
 * @author European Dynamics SA.
 */
@Service
@Validated
@Transactional
public class InternalMessageService {

	//  @PersistenceContext
//  private EntityManager em;

	private final InternalMessagesRepository internalMessagesRepository; 
	private final InternalAttachmentRepository internalAttachmentRepository;
	
	private InternalMessagesMapper internalMessagesMapper;
	private InternalAttachmentMapper internalAttachmentMapper;
	
	private static QInternalMessages qInternalMessages  = QInternalMessages.internalMessages;
	

	public InternalMessageService(InternalMessagesRepository internalMessagesRepository,
			InternalAttachmentRepository internalAttachmentRepository, InternalMessagesMapper internalMessagesMapper,
			InternalAttachmentMapper internalAttachmentMapper) {
		
		this.internalMessagesRepository = internalMessagesRepository;
		this.internalAttachmentRepository = internalAttachmentRepository;
		this.internalMessagesMapper = internalMessagesMapper;
		this.internalAttachmentMapper = internalAttachmentMapper;
	}
	
	
  /**
   * Send a new internal message.
   *
   * @param dto - the internal message data
   * @return - an InternalMessagesDTO object
   */
  public InternalMessagesDTO sendInternalMail(InternalMessagesDTO dto) {

    InternalMessages internalMessages = internalMessagesMapper.mapToEntity(dto); 
    		//ConverterUtil.internalMessageConvert(dto);

    // Status can be READ, UNREAD, REPLIED.
    internalMessages.setStatus("UNREAD");
    internalMessages.setDeleteType("N");

//    em.persist(internalMessages);
    internalMessagesRepository.save(internalMessages);
    
    
    List<InternalAttachmentDTO> internalAttachments = dto.getAttachments();
    if (internalAttachments == null) {
      internalAttachments = new ArrayList<>();
    }

    String fwdAttachmentId = dto.getFwdAttachmentId();
    if (fwdAttachmentId != null) {
      InternalAttachmentDTO fwdInternalAttachmentDto = getInternalAttachment(fwdAttachmentId);
      internalAttachments.add(fwdInternalAttachmentDto);
    }

    for (InternalAttachmentDTO attachmentDto : internalAttachments) {
      InternalAttachment attachment =  internalAttachmentMapper.mapToEntity(attachmentDto);
//    		  ConverterUtil.internalAttachmentConvert(attachmentDto);
      attachment.setMessages(internalMessages);
      internalAttachmentRepository.save(attachment);
      //em.persist(attachment);
    }

    return internalMessagesMapper.mapToDTO(internalMessages);
    		//ConverterUtil.internalMessageConvert(internalMessages);
  }



/**
   * Get the Inbox.
   *
   * @param userId - the person that the message was sent to
   * @return a list of InternalMessagesDTO
   */
  public List<InternalMessagesDTO> getInternalInboxFolder(String userId) {
	  
	  
	  Predicate predicate  = qInternalMessages.mailTo.eq(userId).and(qInternalMessages.deleteType.notEqualsIgnoreCase("I"));
	  
//	  findUserInbox
	  
/*	  public static List<InternalMessages> findUserInbox(EntityManager em, String userId) {
		    String jpql =
		        "SELECT m FROM InternalMessages m " +
		            "WHERE m.mailTo = :userId AND m.deleteType <> 'I'";

		    return em.createQuery(jpql, InternalMessages.class)
		        .setParameter("userId", userId)
		        .getResultList();*/
	  
    List<InternalMessages> internalMessagesList = internalMessagesRepository.findAll(predicate); 
    		//internalMessagesRepository.findbyUserid(userId); 
//    		InternalMessages.findUserInbox(em, userId);

    List<InternalMessagesDTO> dtoList = internalMessagesMapper.mapToDTO(internalMessagesList);
//    		ConverterUtil.internalMessageConvertList(internalMessagesList);

    return dtoList;
  }

  /**
   * Get the sent folder.
   *
   * @param userId - the person that sent the message
   * @return a list of messages
   */
  public List<InternalMessagesDTO> getInternalSentFolder(String userId) {
	  
	  
	  Predicate predicate  = qInternalMessages.mailTo.eq(userId).and(qInternalMessages.deleteType.notEqualsIgnoreCase("S"));

/*	  
	  public static List<InternalMessages> findUserSent(EntityManager em, String userId) {
		    String jpql =
		        "SELECT m FROM InternalMessages m " +
		            "WHERE m.mailFrom = :userId AND m.deleteType <> 'S'";

		    return em.createQuery(jpql, InternalMessages.class)
		        .setParameter("userId", userId)
		        .getResultList();
		  }
*/
	  
	  
    List<InternalMessages> internalMessagesList =  internalMessagesRepository.findAll(predicate); 
    		//internalMessagesRepository.findUserSent(userId); 
    		//InternalMessages.findUserSent(em, userId);

    List<InternalMessagesDTO> dtoList = internalMessagesMapper.mapToDTO(internalMessagesList); 
//    		ConverterUtil.internalMessageConvertList(internalMessagesList);

    return dtoList;
  }

  /**
   * Get the number of the messages
   *
   * @param userId - the person that the message was sent to
   * @param status - the status (read, unread, replied)
   * @return the No of messages.
   */
	public long getMailCount(String userId, String status) {

		Predicate predicate = new BooleanBuilder();

		if (userId != null) {
			// predicates.add("(m.mailTo = :userId AND m.deleteType <> 'I')");
			predicate = ((BooleanBuilder) predicate).and(qInternalMessages.mailTo.eq(userId))
					.and(qInternalMessages.deleteType.notEqualsIgnoreCase("I"));
		}
		if (status != null) {
			// predicates.add("(UPPER(m.status) = :status)");
			predicate = ((BooleanBuilder) predicate).and(qInternalMessages.status.toLowerCase().eq(status));
		}

		return Long.valueOf(internalMessagesRepository.findAll(predicate).size());

		//////////////////////////////////////////////////////////////////
//		String select = "SELECT count(m) FROM InternalMessages m";
//
//		List<String> predicates = new ArrayList<>(2);
//		if (userId != null) {
//			predicates.add("(m.mailTo = :userId AND m.deleteType <> 'I')");
//		}
//		if (status != null) {
//			predicates.add("(UPPER(m.status) = :status)");
//		}
//
//		// open-coded join()
//		StringBuilder sb = new StringBuilder(select);
//		Iterator<String> iter = predicates.iterator();
//
//		if (iter.hasNext()) {
//			sb.append(" WHERE ").append(iter.next());
//			while (iter.hasNext()) {
//				sb.append(" AND ").append(iter.next());
//			}
//		}
//		String jpql = sb.toString();
//
//		TypedQuery<Long> q = em.createQuery(jpql, Long.class);
//		if (userId != null) {
//			q.setParameter("mailTo", userId);
//		}
//		if (status != null) {
//			q.setParameter("status", status.toUpperCase());
//		}
//
//		return q.getSingleResult();
//
//		// InternalMessages.countByUserAndStatus(em, userId, status);
//		Long count = internalMessagesRepository.countByUserAndStatus(userId, status);
//		return count;
	}

  /**
   * Mark a message as Read
   *
   * @param messageId - the message Id
   */
  public void markMessageAsRead(String messageId) {
    InternalMessages internalMessages = internalMessagesRepository.fetchById(messageId);
//    		em.find(InternalMessages.class, messageId);
    internalMessages.setStatus(MailConstants.MARK_READ);
  }

  /**
   * Mark a message as Replied.
   *
   * @param messageId - the message Id
   */
  public void markMessageAsReplied(String messageId) {
    InternalMessages internalMessages = internalMessagesRepository.fetchById(messageId);
    		//em.find(InternalMessages.class, messageId);
    internalMessages.setStatus(MailConstants.MARK_REPLIED);
  }

  /**
   * Mark a message as Unread.
   *
   * @param messageId - the message Id
   */
  public void markMessageAsUnread(String messageId) {
    InternalMessages internalMessages = internalMessagesRepository.fetchById(messageId); 
    		//em.find(InternalMessages.class, messageId);
    internalMessages.setStatus(MailConstants.MARK_UNREAD);
  }

  /**
   * Delete a message.
   *
   * Depending on the folder type (inbox or sent) this method perform the following: <ul> <li> if
   * the folder that contains the message is the inbox and the sender has already deleted the
   * message, then the message is permanently removed from the system.</li> <li> if the folder that
   * contains the message is the inbox and the sender has not deleted the message, then the message
   * is marked as "deleted from the sender".</li> <li> if the folder that contains the message is
   * the sent folder and the receiver has already deleted the message, then the message is
   * permanently removed from the system.</li> <li> if the folder that contains the message is the
   * sent folder and the receiver has not deleted the message, then the message is marked as
   * "deleted from the receiver".</li> </ul>
   *
   * @param messageId - the message Id
   * @param folderType - the folder type (inbox or sent)
   */
  public void deleteMessage(String messageId, String folderType) {
    InternalMessages internalMessages = internalMessagesRepository.fetchById(messageId); 
    		//em.find(InternalMessages.class, messageId);
    if (MailConstants.INBOX_FOLDER_TYPE.equals(folderType)) {
      if ("S".equals(internalMessages.getDeleteType())) {
//        em.remove(em.merge(internalMessages));
    	  internalMessagesRepository.delete(internalMessages);
    	  
      } else {
        internalMessages.setDeleteType("I");
      }
    }
    if (MailConstants.SENT_FOLDER_TYPE.equals(folderType)) {
      if ("I".equals(internalMessages.getDeleteType())) {
//        em.remove(em.merge(internalMessages));
    	  internalMessagesRepository.delete(internalMessages);
      } else {
        internalMessages.setDeleteType("S");
      }
    }
  }

  /**
   * View the details of a message.
   *
   * @param messageId - the message Id
   * @return the message
   */
  public InternalMessagesDTO getInternalMessage(String messageId) {
    InternalMessages internalMessages = internalMessagesRepository.fetchById(messageId);
    		//em.find(InternalMessages.class, messageId);
    return internalMessagesMapper.mapToDTO(internalMessages); 
    		//ConverterUtil.internalMessageConvert(internalMessages);
  }

  /**
   * Get the attachments of a message.
   *
   * @param messageId - the message Id
   */
  public List<InternalAttachmentDTO> getInternalMessageAttachments(String messageId) {
    List<InternalAttachment> internalAttachments =  internalAttachmentRepository.findByMessagesId(messageId);
//    		InternalAttachment.findByMessagesId(em, messageId);

    List<InternalAttachmentDTO> internalAttachmentDtos = new ArrayList<>();
    for (InternalAttachment internalAttachment : internalAttachments) {
      internalAttachmentDtos.add(internalAttachmentMapper.mapToDTO(internalAttachment));
    		  //ConverterUtil.internalAttachmentConvert(internalAttachment));
    }
    return internalAttachmentDtos;
  }

  /**
   * Get an attachment based on its Id.
   *
   * @param attachmentId - the attachment Id.
   * @return the attachment
   */
  public InternalAttachmentDTO getInternalAttachment(String attachmentId) {
    InternalAttachment internalAttachment = internalAttachmentRepository.fetchById(attachmentId); 
    		//InternalAttachment.findById(em, attachmentId);
    return internalAttachmentMapper.mapToDTO(internalAttachment); 
    		//ConverterUtil.internalAttachmentConvert(internalAttachment);
  }

}
