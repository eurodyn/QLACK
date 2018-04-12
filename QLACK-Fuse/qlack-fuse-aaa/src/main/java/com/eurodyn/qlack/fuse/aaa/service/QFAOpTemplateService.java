package com.eurodyn.qlack.fuse.aaa.service;

import com.eurodyn.qlack.fuse.aaa.dto.QFAOpTemplateDTO;
import com.eurodyn.qlack.fuse.aaa.model.QFAOpTemplate;
import com.eurodyn.qlack.fuse.aaa.model.QFAOpTemplateHasOperation;
import com.eurodyn.qlack.fuse.aaa.model.QFAOperation;
import com.eurodyn.qlack.fuse.aaa.model.QFAResource;
import com.eurodyn.qlack.fuse.aaa.util.QFAConverterUtil;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

/**
 * @author European Dynamics SA
 */
@Service
@Validated
@Transactional
public class QFAOpTemplateService {

  @PersistenceContext
  private EntityManager em;

  public String createTemplate(QFAOpTemplateDTO templateDTO) {
    QFAOpTemplate template = new QFAOpTemplate();
    template.setName(templateDTO.getName());
    template.setDescription(templateDTO.getDescription());
    em.persist(template);
    return template.getId();
  }

  public void deleteTemplateByID(String templateID) {
    em.remove(QFAOpTemplate.find(templateID, em));
  }

  public void deleteTemplateByName(String templateName) {
    em.remove(QFAOpTemplate.findByName(templateName, em));
  }

  public QFAOpTemplateDTO getTemplateByID(String templateID) {
    return QFAConverterUtil.opTemplateToOpTemplateDTO(QFAOpTemplate.find(templateID, em));
  }

  public QFAOpTemplateDTO getTemplateByName(String templateName) {
    return QFAConverterUtil.opTemplateToOpTemplateDTO(QFAOpTemplate.findByName(templateName, em));
  }

  public void addOperation(String templateID, String operationName,
      boolean isDeny) {
    QFAOpTemplateHasOperation tho = QFAOpTemplateHasOperation
        .findByTemplateIDAndOperationName(templateID, operationName, em);
    if (tho != null) {
      tho.setDeny(isDeny);
    } else {
      QFAOpTemplate template = QFAOpTemplate.find(templateID, em);
      QFAOperation operation = QFAOperation.findByName(operationName, em);
      tho = new QFAOpTemplateHasOperation();
      tho.setDeny(isDeny);
      template.addOpTemplateHasOperation(tho);
      operation.addOpTemplateHasOperation(tho);
      em.persist(tho);
    }
  }

  public void addOperation(String templateID, String operationName,
      String resourceID, boolean isDeny) {
    QFAOpTemplateHasOperation tho = QFAOpTemplateHasOperation
        .findByTemplateAndResourceIDAndOperationName(
            templateID, operationName, resourceID, em);
    if (tho != null) {
      tho.setDeny(isDeny);
    } else {
      QFAOpTemplate template = QFAOpTemplate.find(templateID, em);
      QFAOperation operation = QFAOperation.findByName(operationName, em);
      QFAResource resource = QFAResource.find(resourceID, em);
      tho = new QFAOpTemplateHasOperation();
      tho.setDeny(isDeny);
      template.addOpTemplateHasOperation(tho);
      operation.addOpTemplateHasOperation(tho);
      resource.addOpTemplateHasOperation(tho);
      em.persist(tho);
    }
  }

  public void removeOperation(String templateID, String operationName) {
    QFAOpTemplateHasOperation tho = QFAOpTemplateHasOperation.findByTemplateIDAndOperationName(
        templateID, operationName, em);
    em.remove(tho);
  }

  public void removeOperation(String templateID, String operationName,
      String resourceID) {
    QFAOpTemplateHasOperation tho = QFAOpTemplateHasOperation
        .findByTemplateAndResourceIDAndOperationName(
            templateID, operationName, resourceID, em);
    em.remove(tho);
  }

  public Boolean getOperationAccess(String templateID, String operationName) {
    Boolean retVal = null;

    QFAOpTemplateHasOperation tho = QFAOpTemplateHasOperation.findByTemplateIDAndOperationName(
        templateID, operationName, em);
    if (tho != null) {
      retVal = tho.isDeny();
    }

    return retVal;
  }

  public Boolean getOperationAccess(String templateID, String operationName,
      String resourceID) {
    Boolean retVal = null;

    QFAOpTemplateHasOperation tho = QFAOpTemplateHasOperation
        .findByTemplateAndResourceIDAndOperationName(
            templateID, operationName, resourceID, em);
    if (tho != null) {
      retVal = tho.isDeny();
    }

    return retVal;
  }

  public boolean updateTemplate(QFAOpTemplateDTO templateDTO) {
    boolean retVal = false;
    QFAOpTemplate template = em.find(QFAOpTemplate.class, templateDTO.getId());
    if (template != null) {
      template.setDescription(templateDTO.getDescription());
      template.setName(templateDTO.getName());
      retVal = true;
    }

    return retVal;
  }
}