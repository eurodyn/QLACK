package com.eurodyn.qlack.fuse.aaa.service;

import com.eurodyn.qlack.fuse.aaa.dto.OpTemplateDTO;
import com.eurodyn.qlack.fuse.aaa.model.OpTemplate;
import com.eurodyn.qlack.fuse.aaa.model.OpTemplateHasOperation;
import com.eurodyn.qlack.fuse.aaa.model.Operation;
import com.eurodyn.qlack.fuse.aaa.model.Resource;
import com.eurodyn.qlack.fuse.aaa.util.ConverterUtil;
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
public class OpTemplateService {

  @PersistenceContext
  private EntityManager em;

  public String createTemplate(OpTemplateDTO templateDTO) {
    OpTemplate template = new OpTemplate();
    template.setName(templateDTO.getName());
    template.setDescription(templateDTO.getDescription());
    em.persist(template);
    return template.getId();
  }

  public void deleteTemplateByID(String templateID) {
    em.remove(OpTemplate.find(templateID, em));
  }

  public void deleteTemplateByName(String templateName) {
    em.remove(OpTemplate.findByName(templateName, em));
  }

  public OpTemplateDTO getTemplateByID(String templateID) {
    return ConverterUtil.opTemplateToOpTemplateDTO(OpTemplate.find(templateID, em));
  }

  public OpTemplateDTO getTemplateByName(String templateName) {
    return ConverterUtil.opTemplateToOpTemplateDTO(OpTemplate.findByName(templateName, em));
  }

  public void addOperation(String templateID, String operationName,
      boolean isDeny) {
    OpTemplateHasOperation tho = OpTemplateHasOperation
        .findByTemplateIDAndOperationName(templateID, operationName, em);
    if (tho != null) {
      tho.setDeny(isDeny);
    } else {
      OpTemplate template = OpTemplate.find(templateID, em);
      Operation operation = Operation.findByName(operationName, em);
      tho = new OpTemplateHasOperation();
      tho.setDeny(isDeny);
      template.addOpTemplateHasOperation(tho);
      operation.addOpTemplateHasOperation(tho);
      em.persist(tho);
    }
  }

  public void addOperation(String templateID, String operationName,
      String resourceID, boolean isDeny) {
    OpTemplateHasOperation tho = OpTemplateHasOperation
        .findByTemplateAndResourceIDAndOperationName(
            templateID, operationName, resourceID, em);
    if (tho != null) {
      tho.setDeny(isDeny);
    } else {
      OpTemplate template = OpTemplate.find(templateID, em);
      Operation operation = Operation.findByName(operationName, em);
      Resource resource = Resource.find(resourceID, em);
      tho = new OpTemplateHasOperation();
      tho.setDeny(isDeny);
      template.addOpTemplateHasOperation(tho);
      operation.addOpTemplateHasOperation(tho);
      resource.addOpTemplateHasOperation(tho);
      em.persist(tho);
    }
  }

  public void removeOperation(String templateID, String operationName) {
    OpTemplateHasOperation tho = OpTemplateHasOperation.findByTemplateIDAndOperationName(
        templateID, operationName, em);
    em.remove(tho);
  }

  public void removeOperation(String templateID, String operationName,
      String resourceID) {
    OpTemplateHasOperation tho = OpTemplateHasOperation
        .findByTemplateAndResourceIDAndOperationName(
            templateID, operationName, resourceID, em);
    em.remove(tho);
  }

  public Boolean getOperationAccess(String templateID, String operationName) {
    Boolean retVal = null;

    OpTemplateHasOperation tho = OpTemplateHasOperation.findByTemplateIDAndOperationName(
        templateID, operationName, em);
    if (tho != null) {
      retVal = tho.isDeny();
    }

    return retVal;
  }

  public Boolean getOperationAccess(String templateID, String operationName,
      String resourceID) {
    Boolean retVal = null;

    OpTemplateHasOperation tho = OpTemplateHasOperation
        .findByTemplateAndResourceIDAndOperationName(
            templateID, operationName, resourceID, em);
    if (tho != null) {
      retVal = tho.isDeny();
    }

    return retVal;
  }

  public boolean updateTemplate(OpTemplateDTO templateDTO) {
    boolean retVal = false;
    OpTemplate template = em.find(OpTemplate.class, templateDTO.getId());
    if (template != null) {
      template.setDescription(templateDTO.getDescription());
      template.setName(templateDTO.getName());
      retVal = true;
    }

    return retVal;
  }
}