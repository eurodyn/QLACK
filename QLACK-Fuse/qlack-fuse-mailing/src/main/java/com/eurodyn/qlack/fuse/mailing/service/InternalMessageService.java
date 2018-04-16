package com.eurodyn.qlack.fuse.mailing.service;


import com.eurodyn.qlack.fuse.mailing.dto.InternalAttachmentDTO;
import com.eurodyn.qlack.fuse.mailing.dto.InternalMessagesDTO;
import com.eurodyn.qlack.fuse.mailing.model.InternalAttachment;
import com.eurodyn.qlack.fuse.mailing.model.InternalMessages;
import com.eurodyn.qlack.fuse.mailing.util.ConverterUtil;
import com.eurodyn.qlack.fuse.mailing.util.MailConstants;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
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

  @PersistenceContext
  private EntityManager em;

  /**
   * Send a new internal message.
   *
   * @param dto - the internal message data
   * @return - an InternalMessagesDTO object
   */
  public InternalMessagesDTO sendInternalMail(InternalMessagesDTO dto) {

    InternalMessages internalMessages = ConverterUtil.internalMessageConvert(dto);

    // Status can be READ, UNREAD, REPLIED.
    internalMessages.setStatus("UNREAD");
    internalMessages.setDeleteType("N");

    em.persist(internalMessages);

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
      InternalAttachment attachment = ConverterUtil.internalAttachmentConvert(attachmentDto);
      attachment.setMessages(internalMessages);
      em.persist(attachment);
    }

    return ConverterUtil.internalMessageConvert(internalMessages);
  }

  /**
   * Get the Inbox.
   *
   * @param userId - the person that the message was sent to
   * @return a list of InternalMessagesDTO
   */
  public List<InternalMessagesDTO> getInternalInboxFolder(String userId) {
    List<InternalMessages> internalMessagesList = InternalMessages.findUserInbox(em, userId);

    List<InternalMessagesDTO> dtoList = ConverterUtil
        .internalMessageConvertList(internalMessagesList);

    return dtoList;
  }

  /**
   * Get the sent folder.
   *
   * @param userId - the person that sent the message
   * @return a list of messages
   */
  public List<InternalMessagesDTO> getInternalSentFolder(String userId) {
    List<InternalMessages> internalMessagesList = InternalMessages.findUserSent(em, userId);

    List<InternalMessagesDTO> dtoList = ConverterUtil
        .internalMessageConvertList(internalMessagesList);

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
    Long count = InternalMessages.countByUserAndStatus(em, userId, status);
    return count;
  }

  /**
   * Mark a message as Read
   *
   * @param messageId - the message Id
   */
  public void markMessageAsRead(String messageId) {
    InternalMessages internalMessages = em.find(InternalMessages.class, messageId);
    internalMessages.setStatus(MailConstants.MARK_READ);
  }

  /**
   * Mark a message as Replied.
   *
   * @param messageId - the message Id
   */
  public void markMessageAsReplied(String messageId) {
    InternalMessages internalMessages = em.find(InternalMessages.class, messageId);
    internalMessages.setStatus(MailConstants.MARK_REPLIED);
  }

  /**
   * Mark a message as Unread.
   *
   * @param messageId - the message Id
   */
  public void markMessageAsUnread(String messageId) {
    InternalMessages internalMessages = em.find(InternalMessages.class, messageId);
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
    InternalMessages internalMessages = em.find(InternalMessages.class, messageId);
    if (MailConstants.INBOX_FOLDER_TYPE.equals(folderType)) {
      if ("S".equals(internalMessages.getDeleteType())) {
        em.remove(em.merge(internalMessages));
      } else {
        internalMessages.setDeleteType("I");
      }
    }
    if (MailConstants.SENT_FOLDER_TYPE.equals(folderType)) {
      if ("I".equals(internalMessages.getDeleteType())) {
        em.remove(em.merge(internalMessages));
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
    InternalMessages internalMessages = em.find(InternalMessages.class, messageId);
    return ConverterUtil.internalMessageConvert(internalMessages);
  }

  /**
   * Get the attachments of a message.
   *
   * @param messageId - the message Id
   */
  public List<InternalAttachmentDTO> getInternalMessageAttachments(String messageId) {
    List<InternalAttachment> internalAttachments = InternalAttachment
        .findByMessagesId(em, messageId);

    List<InternalAttachmentDTO> internalAttachmentDtos = new ArrayList<>();
    for (InternalAttachment internalAttachment : internalAttachments) {
      internalAttachmentDtos.add(ConverterUtil.internalAttachmentConvert(internalAttachment));
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
    InternalAttachment internalAttachment = InternalAttachment.findById(em, attachmentId);
    return ConverterUtil.internalAttachmentConvert(internalAttachment);
  }

}
