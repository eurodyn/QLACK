package com.eurodyn.qlack.fuse.mailing.util;


import com.eurodyn.qlack.fuse.mailing.dto.QFMContactDTO;
import com.eurodyn.qlack.fuse.mailing.dto.QFMDistributionListDTO;
import com.eurodyn.qlack.fuse.mailing.dto.QFMEmailDTO;
import com.eurodyn.qlack.fuse.mailing.dto.QFMInternalAttachmentDTO;
import com.eurodyn.qlack.fuse.mailing.dto.QFMInternalMessagesDTO;
import com.eurodyn.qlack.fuse.mailing.model.QFMContact;
import com.eurodyn.qlack.fuse.mailing.model.QFMDistributionList;
import com.eurodyn.qlack.fuse.mailing.model.QFMEmail;
import com.eurodyn.qlack.fuse.mailing.model.QFMInternalAttachment;
import com.eurodyn.qlack.fuse.mailing.model.QFMInternalMessages;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * This is utility class used for converting DTO's to entities and vice versa.
 *
 * @author European Dynamics SA.
 */
public class QFMConverterUtil {

  /**
   * Converts the entity QFMDistributionList to data transfer object QFMDistributionListDTO
   *
   * @param entity QFMDistributionList entity.
   * @return QFMDistributionListDTO Data transfer object, null if entity is null.
   */
  public static QFMDistributionListDTO dlistConvert(QFMDistributionList entity) {
    if (entity == null) {
      return null;
    }

    QFMDistributionListDTO dto = new QFMDistributionListDTO();
    dto.setId(entity.getId());
    dto.setName(entity.getName());
    dto.setDescription(entity.getDescription());
    dto.setCreatedBy(entity.getCreatedBy());
    dto.setCreatedOn(entity.getCreatedOn());

    return dto;
  }

  /**
   * Converts the entity data transfer object QFMDistributionListDTO to QFMDistributionList.
   *
   * @param dto Data transfer object
   * @return QFMDistributionList entity, null if DTO is null.
   */
  public static QFMDistributionList dlistConvert(QFMDistributionListDTO dto) {
    if (dto == null) {
      return null;
    }

    QFMDistributionList entity = new QFMDistributionList();
    entity.setName(dto.getName());
    entity.setDescription(dto.getDescription());

    return entity;
  }

  /**
   * Converts the entity data transfer object QFMContactDTO to QFMContact.
   */
  public static QFMContact contactConvert(QFMContactDTO dto) {
    QFMContact entity = new QFMContact();
    entity.setEmail(dto.getEmail());
    entity.setFirstName(dto.getFirstName());
    entity.setLastName(dto.getLastName());
    entity.setLocale(dto.getLocale());
    entity.setUserId(dto.getUserID());

    return entity;
  }

  /**
   * Creates a list of e-mails.
   *
   * @param emails String of e-mails separated by token .
   * @return list of e-mails.
   */
  public static List<String> createRecepientlist(String emails) {
    List<String> contacts = new ArrayList<>();

    StringTokenizer st = new StringTokenizer(emails, ",");
    while (st.hasMoreElements()) {
      String next = (String) st.nextElement();
      contacts.add(next);
    }

    return contacts.isEmpty() ? null : contacts;
  }

  /**
   * Create e-mails string separated by token
   *
   * @param emails List of e-mails.
   * @return String of token separated e-mails.
   */
  public static String createRecepientlist(List<String> emails) {

    StringBuilder emailAddress = new StringBuilder();
    if (emails != null && !emails.isEmpty()) {
      for (String email : emails) {
        if (emailAddress.length() > 0) {
          emailAddress.append(",");
        }
        emailAddress.append(email);
      }
    }

    return emailAddress.length() > 0 ? emailAddress.toString() : null;
  }

  /**
   * Converts QFMInternalMessagesDTO DTO to QFMInternalMessages without attachments.
   *
   * @param dto internal message data transfer object.
   * @return QFMInternalMessages entity.
   */
  public static QFMInternalMessages internalMessageConvert(QFMInternalMessagesDTO dto) {
    if (dto == null) {
      return null;
    }

    QFMInternalMessages entity = new QFMInternalMessages();

    entity.setSubject(dto.getSubject());
    entity.setMessage(dto.getMessage());
    entity.setMailFrom(dto.getFrom());
    entity.setMailTo(dto.getTo());
    entity.setDateSent(dto.getDateSent().getTime());
    entity.setDateReceived(dto.getDateReceived().getTime());

    return entity;
  }

  /**
   * Converts the QFMInternalAttachment entity to data transfer object.
   *
   * @param dto internal message data transfer object.
   * @return QFMInternalAttachment entity.
   */
  public static QFMInternalAttachment internalAttachmentConvert(QFMInternalAttachmentDTO dto) {
    if (dto == null) {
      return null;
    }

    QFMInternalAttachment entity = new QFMInternalAttachment();

    entity.setContentType(dto.getContentType());
    entity.setData(dto.getData());
    entity.setFilename(dto.getFilename());
    entity.setFormat(dto.getFormat());

    return entity;
  }

  /**
   * Converts QFMInternalMessages entity to QFMInternalMessagesDTO.
   *
   * @param entity QFMInternalMessages
   * @return QFMInternalMessagesDTO
   */
  public static QFMInternalMessagesDTO internalMessageConvert(QFMInternalMessages entity) {
    if (entity == null) {
      return null;
    }

    QFMInternalMessagesDTO dto = new QFMInternalMessagesDTO();
    dto.setId(entity.getId());
    dto.setSubject(entity.getSubject());
    dto.setMessage(entity.getMessage());
    dto.setFrom(entity.getMailFrom());
    dto.setTo(entity.getMailTo());
    dto.setDateSent(entity.getDateSent());
    dto.setDateReceived(entity.getDateReceived());
    dto.setStatus(entity.getStatus());
    dto.setDeleteType(entity.getDeleteType());

    Set<QFMInternalAttachment> entityAttachments = entity.getAttachments();

    List<QFMInternalAttachmentDTO> dtoAttachments = new ArrayList<>();
    if (entityAttachments != null && !entityAttachments.isEmpty()) {
      for (QFMInternalAttachment attachment : entityAttachments) {
        dtoAttachments.add(internalAttachmentConvert(attachment));
      }
    }
    dto.setAttachments(dtoAttachments);

    return dto;
  }

  /**
   * Converts the QFMInternalAttachment entity to DTO.
   *
   * @param entity Internal attachment.
   * @return QFMInternalAttachment entity.
   */
  public static QFMInternalAttachmentDTO internalAttachmentConvert(QFMInternalAttachment entity) {
    if (entity == null) {
      return null;
    }

    QFMInternalAttachmentDTO dto = new QFMInternalAttachmentDTO();

    dto.setId(entity.getId());
    dto.setMessagesId(entity.getMessages().getId());
    dto.setContentType(entity.getContentType());
    dto.setData(entity.getData());
    dto.setFilename(entity.getFilename());
    dto.setFormat(entity.getFormat());

    return dto;
  }

  /**
   * Converts list of QFMInternalMessages entities to list data transfer object.
   *
   * @param internalMessagesList list of QFMInternalMessages entities.
   * @return list of data transfer object.
   */
  public static List<QFMInternalMessagesDTO> internalMessageConvertList(
      List<QFMInternalMessages> internalMessagesList) {
    List<QFMInternalMessagesDTO> messagesDtoList = new ArrayList<>();

    for (QFMInternalMessages internalMessages : internalMessagesList) {
      QFMInternalMessagesDTO dto = internalMessageConvert(internalMessages);
      messagesDtoList.add(dto);
    }

    return messagesDtoList;
  }

  /**
   * Converts the QFMEmail entity to DTO.
   *
   * @param entity QFMEmail.
   * @return QFMEmailDTO
   */
  public static QFMEmailDTO emailConvert(QFMEmail entity) {
    if (entity == null) {
      return null;
    }

    QFMEmailDTO dto = new QFMEmailDTO();
    dto.setId(entity.getId());
    dto.setDateSent(entity.getDateSent());
    dto.setStatus(entity.getStatus());
    dto.setBody(entity.getBody());
    dto.setFrom(entity.getFromEmail());
    dto.setServerResponse(entity.getServerResponse());
    dto.setSubject(entity.getSubject());
    dto.setStatus(entity.getStatus());

    return dto;
  }

}
