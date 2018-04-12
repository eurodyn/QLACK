package com.eurodyn.qlack.fuse.mailing.service;


import com.eurodyn.qlack.fuse.mailing.dto.QFMInternalAttachmentDTO;
import com.eurodyn.qlack.fuse.mailing.dto.QFMInternalMessagesDTO;
import com.eurodyn.qlack.fuse.mailing.model.QFMInternalAttachment;
import com.eurodyn.qlack.fuse.mailing.model.QFMInternalMessages;
import com.eurodyn.qlack.fuse.mailing.util.QFMConverterUtil;
import com.eurodyn.qlack.fuse.mailing.util.QFMMailConstants;
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
public class QFMInternalMessageService {

  @PersistenceContext
  private EntityManager em;

  /**
   * Send a new internal message.
   *
   * @param dto - the internal message data
   * @return - an QFMInternalMessagesDTO object
   */
  public QFMInternalMessagesDTO sendInternalMail(QFMInternalMessagesDTO dto) {

    QFMInternalMessages internalMessages = QFMConverterUtil.internalMessageConvert(dto);

    // Status can be READ, UNREAD, REPLIED.
    internalMessages.setStatus("UNREAD");
    internalMessages.setDeleteType("N");

    em.persist(internalMessages);

    List<QFMInternalAttachmentDTO> internalAttachments = dto.getAttachments();
    if (internalAttachments == null) {
      internalAttachments = new ArrayList<>();
    }

    String fwdAttachmentId = dto.getFwdAttachmentId();
    if (fwdAttachmentId != null) {
      QFMInternalAttachmentDTO fwdInternalAttachmentDto = getInternalAttachment(fwdAttachmentId);
      internalAttachments.add(fwdInternalAttachmentDto);
    }

    for (QFMInternalAttachmentDTO attachmentDto : internalAttachments) {
      QFMInternalAttachment attachment = QFMConverterUtil.internalAttachmentConvert(attachmentDto);
      attachment.setMessages(internalMessages);
      em.persist(attachment);
    }

    return QFMConverterUtil.internalMessageConvert(internalMessages);
  }

  /**
   * Get the Inbox.
   *
   * @param userId - the person that the message was sent to
   * @return a list of QFMInternalMessagesDTO
   */
  public List<QFMInternalMessagesDTO> getInternalInboxFolder(String userId) {
    List<QFMInternalMessages> internalMessagesList = QFMInternalMessages.findUserInbox(em, userId);

    List<QFMInternalMessagesDTO> dtoList = QFMConverterUtil
        .internalMessageConvertList(internalMessagesList);

    return dtoList;
  }

  /**
   * Get the sent folder.
   *
   * @param userId - the person that sent the message
   * @return a list of messages
   */
  public List<QFMInternalMessagesDTO> getInternalSentFolder(String userId) {
    List<QFMInternalMessages> internalMessagesList = QFMInternalMessages.findUserSent(em, userId);

    List<QFMInternalMessagesDTO> dtoList = QFMConverterUtil
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
    Long count = QFMInternalMessages.countByUserAndStatus(em, userId, status);
    return count;
  }

  /**
   * Mark a message as Read
   *
   * @param messageId - the message Id
   */
  public void markMessageAsRead(String messageId) {
    QFMInternalMessages internalMessages = em.find(QFMInternalMessages.class, messageId);
    internalMessages.setStatus(QFMMailConstants.MARK_READ);
  }

  /**
   * Mark a message as Replied.
   *
   * @param messageId - the message Id
   */
  public void markMessageAsReplied(String messageId) {
    QFMInternalMessages internalMessages = em.find(QFMInternalMessages.class, messageId);
    internalMessages.setStatus(QFMMailConstants.MARK_REPLIED);
  }

  /**
   * Mark a message as Unread.
   *
   * @param messageId - the message Id
   */
  public void markMessageAsUnread(String messageId) {
    QFMInternalMessages internalMessages = em.find(QFMInternalMessages.class, messageId);
    internalMessages.setStatus(QFMMailConstants.MARK_UNREAD);
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
    QFMInternalMessages internalMessages = em.find(QFMInternalMessages.class, messageId);
    if (QFMMailConstants.INBOX_FOLDER_TYPE.equals(folderType)) {
      if ("S".equals(internalMessages.getDeleteType())) {
        em.remove(em.merge(internalMessages));
      } else {
        internalMessages.setDeleteType("I");
      }
    }
    if (QFMMailConstants.SENT_FOLDER_TYPE.equals(folderType)) {
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
  public QFMInternalMessagesDTO getInternalMessage(String messageId) {
    QFMInternalMessages internalMessages = em.find(QFMInternalMessages.class, messageId);
    return QFMConverterUtil.internalMessageConvert(internalMessages);
  }

  /**
   * Get the attachments of a message.
   *
   * @param messageId - the message Id
   */
  public List<QFMInternalAttachmentDTO> getInternalMessageAttachments(String messageId) {
    List<QFMInternalAttachment> internalAttachments = QFMInternalAttachment
        .findByMessagesId(em, messageId);

    List<QFMInternalAttachmentDTO> internalAttachmentDtos = new ArrayList<>();
    for (QFMInternalAttachment internalAttachment : internalAttachments) {
      internalAttachmentDtos.add(QFMConverterUtil.internalAttachmentConvert(internalAttachment));
    }
    return internalAttachmentDtos;
  }

  /**
   * Get an attachment based on its Id.
   *
   * @param attachmentId - the attachment Id.
   * @return the attachment
   */
  public QFMInternalAttachmentDTO getInternalAttachment(String attachmentId) {
    QFMInternalAttachment internalAttachment = QFMInternalAttachment.findById(em, attachmentId);
    return QFMConverterUtil.internalAttachmentConvert(internalAttachment);
  }

}
