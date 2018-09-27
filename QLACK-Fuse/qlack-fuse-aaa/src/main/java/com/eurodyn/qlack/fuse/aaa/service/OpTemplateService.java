package com.eurodyn.qlack.fuse.aaa.service;

import com.eurodyn.qlack.fuse.aaa.dto.OpTemplateDTO;
import com.eurodyn.qlack.fuse.aaa.model.OpTemplate;
import com.eurodyn.qlack.fuse.aaa.model.OpTemplateHasOperation;
import com.eurodyn.qlack.fuse.aaa.model.Operation;
import com.eurodyn.qlack.fuse.aaa.model.Resource;
import com.eurodyn.qlack.fuse.aaa.repository.OpTemplateHasOperationRepository;
import com.eurodyn.qlack.fuse.aaa.repository.OpTemplateRepository;
import com.eurodyn.qlack.fuse.aaa.repository.OperationRepository;
import com.eurodyn.qlack.fuse.aaa.repository.ResourceRepository;
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

//  @PersistenceContext
//  private EntityManager em;

  private final OpTemplateRepository opTemplateRepository;

  private final OpTemplateHasOperationRepository opTemplateHasOperationRepository;

  private final OperationRepository operationRepository;

  private final ResourceRepository resourceRepository;

  public OpTemplateService(
      OpTemplateRepository opTemplateRepository,
      OpTemplateHasOperationRepository opTemplateHasOperationRepository,
      OperationRepository operationRepository,
      ResourceRepository resourceRepository) {
    this.opTemplateRepository = opTemplateRepository;
    this.opTemplateHasOperationRepository = opTemplateHasOperationRepository;
    this.operationRepository = operationRepository;
    this.resourceRepository = resourceRepository;
  }

  public String createTemplate(OpTemplateDTO templateDTO) {
    OpTemplate template = new OpTemplate();
    template.setName(templateDTO.getName());
    template.setDescription(templateDTO.getDescription());
//    em.persist(template);
    opTemplateRepository.save(template);
    return template.getId();
  }

  public void deleteTemplateByID(String templateID) {
//    em.remove(OpTemplate.find(templateID, em));

  }

  public void deleteTemplateByName(String templateName) {
//    em.remove(OpTemplate.findByName(templateName, em));

  }

  public OpTemplateDTO getTemplateByID(String templateID) {
//    return ConverterUtil.opTemplateToOpTemplateDTO(OpTemplate.find(templateID, em));
    return ConverterUtil.opTemplateToOpTemplateDTO(opTemplateRepository.fetchById(templateID));
  }

  public OpTemplateDTO getTemplateByName(String templateName) {
//    return ConverterUtil.opTemplateToOpTemplateDTO(OpTemplate.findByName(templateName, em));
    return ConverterUtil.opTemplateToOpTemplateDTO(opTemplateRepository.findByName(templateName));
  }

  public void addOperation(String templateID, String operationName,
      boolean isDeny) {
//    OpTemplateHasOperation tho = OpTemplateHasOperation
//        .findByTemplateIDAndOperationName(templateID, operationName, em);
    OpTemplateHasOperation tho = opTemplateHasOperationRepository
        .findByTemplateIdAndOperationName(templateID, operationName);
    if (tho != null) {
      tho.setDeny(isDeny);
    } else {
//      OpTemplate template = OpTemplate.find(templateID, em);
      OpTemplate template = opTemplateRepository.fetchById(templateID);
//      Operation operation = Operation.findByName(operationName, em);
      Operation operation = operationRepository.findByName(operationName);
      tho = new OpTemplateHasOperation();
      tho.setDeny(isDeny);
      template.addOpTemplateHasOperation(tho);
      operation.addOpTemplateHasOperation(tho);
//      em.persist(tho);
      opTemplateHasOperationRepository.save(tho);
    }
  }

  public void addOperation(String templateID, String operationName,
      String resourceID, boolean isDeny) {
//    OpTemplateHasOperation tho = OpTemplateHasOperation
//        .findByTemplateAndResourceIDAndOperationName(
//            templateID, operationName, resourceID, em);
    OpTemplateHasOperation tho = opTemplateHasOperationRepository
        .findByTemplateIdAndResourceIdAndOperationName(
            templateID, resourceID, operationName);
    if (tho != null) {
      tho.setDeny(isDeny);
    } else {
//      OpTemplate template = OpTemplate.find(templateID, em);
      OpTemplate template = opTemplateRepository.fetchById(templateID);
//      Operation operation = Operation.findByName(operationName, em);
      Operation operation = operationRepository.findByName(operationName);
//      Resource resource = Resource.find(resourceID, em);
      Resource resource = resourceRepository.fetchById(resourceID);
      tho = new OpTemplateHasOperation();
      tho.setDeny(isDeny);
      template.addOpTemplateHasOperation(tho);
      operation.addOpTemplateHasOperation(tho);
      resource.addOpTemplateHasOperation(tho);
//      em.persist(tho);
      opTemplateHasOperationRepository.save(tho);
    }
  }

  public void removeOperation(String templateID, String operationName) {
//    OpTemplateHasOperation tho = OpTemplateHasOperation.findByTemplateIDAndOperationName(
//        templateID, operationName, em);
    OpTemplateHasOperation tho = opTemplateHasOperationRepository.findByTemplateIdAndOperationName(
        templateID, operationName);
//    em.remove(tho);
    opTemplateHasOperationRepository.delete(tho);
  }

  public void removeOperation(String templateID, String operationName,
      String resourceID) {
//    OpTemplateHasOperation tho = OpTemplateHasOperation
//        .findByTemplateAndResourceIDAndOperationName(
//            templateID, operationName, resourceID, em);
    OpTemplateHasOperation tho = opTemplateHasOperationRepository
        .findByTemplateIdAndResourceIdAndOperationName(templateID, operationName, resourceID);
//    em.remove(tho);
    opTemplateHasOperationRepository.delete(tho);
  }

  public Boolean getOperationAccess(String templateID, String operationName) {
    Boolean retVal = null;

//    OpTemplateHasOperation tho = OpTemplateHasOperation.findByTemplateIDAndOperationName(
//        templateID, operationName, em);
    OpTemplateHasOperation tho = opTemplateHasOperationRepository.findByTemplateIdAndOperationName(
        templateID, operationName);
    if (tho != null) {
      retVal = tho.isDeny();
    }

    return retVal;
  }

  public Boolean getOperationAccess(String templateID, String operationName,
      String resourceID) {
    Boolean retVal = null;

//    OpTemplateHasOperation tho = OpTemplateHasOperation
//        .findByTemplateAndResourceIDAndOperationName(
//            templateID, operationName, resourceID, em);
    OpTemplateHasOperation tho = opTemplateHasOperationRepository
        .findByTemplateIdAndResourceIdAndOperationName(
            templateID, resourceID, operationName);
    if (tho != null) {
      retVal = tho.isDeny();
    }

    return retVal;
  }

  public boolean updateTemplate(OpTemplateDTO templateDTO) {
    boolean retVal = false;
//    OpTemplate template = em.find(OpTemplate.class, templateDTO.getId());
    OpTemplate template = opTemplateRepository.fetchById(templateDTO.getId());
    if (template != null) {
      template.setDescription(templateDTO.getDescription());
      template.setName(templateDTO.getName());
      retVal = true;
    }

    return retVal;
  }
}