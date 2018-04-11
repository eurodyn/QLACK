package com.eurodyn.qlack.fuse.aaa.service;

import com.eurodyn.qlack.fuse.aaa.dto.QFAGroupDTO;
import com.eurodyn.qlack.fuse.aaa.dto.QFAJSONConfig;
import com.eurodyn.qlack.fuse.aaa.dto.QFAOpTemplateDTO;
import com.eurodyn.qlack.fuse.aaa.dto.QFAOperationDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@Validated
@Transactional
public class QFAJSONConfigService {

  // Logger
  private static final Logger LOGGER = Logger.getLogger(QFAJSONConfigService.class.getName());

  // JSON parser.
  ObjectMapper mapper = new ObjectMapper();

  @PersistenceContext
  private EntityManager em;

  private QFAUserGroupService groupService;
  private QFAOpTemplateService templateService;
  private QFAOperationService operationService;

  @Autowired
  public QFAJSONConfigService(QFAUserGroupService groupService, QFAOpTemplateService templateService,
      QFAOperationService operationService) {
    this.groupService = groupService;
    this.templateService = templateService;
    this.operationService = operationService;
  }

  private void parseConfig(URL configFileURL) {
    LOGGER.log(Level.FINE, "Handling FUSE AAA configuration: {0}.", configFileURL.toExternalForm());

    // Parse the JSON file.
    QFAJSONConfig config = null;
    try {
      config = mapper.readValue(configFileURL, QFAJSONConfig.class);
    } catch (IOException e) {
      LOGGER.log(Level.SEVERE, MessageFormat.format(
          "Could not parse configuration file {0}.", configFileURL.toExternalForm()), e);
      return;
    }

    // Calculate an MD5 for this file to know if it has changed in order to
    // avoid unnecessary database access.
    String checksum = null;
    try {
      checksum = DigestUtils.md5Hex(mapper.writeValueAsString(config));
    } catch (JsonProcessingException e) {
      LOGGER.log(Level.SEVERE, MessageFormat.format(
          "Could not calculate MD5 for file {0}.", configFileURL.toExternalForm()), e);
      return;
    }

    // Create groups.
    for (QFAJSONConfig.Group g : config.getGroups()) {
      // If the group exists, update it, otherwise create it.
      LOGGER.log(Level.FINEST, "Processing group {0}", g.getName());
      QFAGroupDTO groupDTO = groupService.getGroupByName(g.getName(), true);
      boolean isNew = groupDTO == null;
      if (isNew) {
        groupDTO = new QFAGroupDTO();
      }
      groupDTO.setDescription(g.getDescription());
      groupDTO.setName(g.getName());
      if (StringUtils.isNotBlank(g.getParentGroupName())) {
        QFAGroupDTO parentGroup = groupService.getGroupByName(g.getParentGroupName(), true);
        groupDTO.setParent(new QFAGroupDTO(parentGroup.getId()));
      }
      if (isNew) {
        groupService.createGroup(groupDTO);
      } else {
        groupService.updateGroup(groupDTO);
      }
    }
//    em.flush();

    // Create templates.
    for (QFAJSONConfig.Template t : config.getTemplates()) {
      // If the template exists, update it, otherwise create it.
      LOGGER.log(Level.FINEST, "Processing template {0}", t.getName());
      QFAOpTemplateDTO templateDTO = templateService.getTemplateByName(t.getName());
      boolean isNew = templateDTO == null;
      if (isNew) {
        templateDTO = new QFAOpTemplateDTO();
      }
      templateDTO.setDescription(t.getDescription());
      templateDTO.setName(t.getName());
      if (isNew) {
        templateService.createTemplate(templateDTO);
      } else {
        templateService.updateTemplate(templateDTO);
      }
    }
//    em.flush();

    // Create Operations.
    for (QFAJSONConfig.Operation o : config.getOperations()) {
      // If the operation exists, update it, otherwise create it.
      LOGGER.log(Level.FINEST, "Processing operation {0}", o.getName());
      QFAOperationDTO opDTO = operationService.getOperationByName(o.getName());
      boolean isNew = opDTO == null;
      if (isNew) {
        opDTO = new QFAOperationDTO();
      }
      opDTO.setDescription(o.getDescription());
      opDTO.setName(o.getName());
      if (isNew) {
        operationService.createOperation(opDTO);
      } else {
        operationService.updateOperation(opDTO);
      }
    }
//    em.flush();

    // Create QFAGroup has Operations.
    for (QFAJSONConfig.GroupHasOperation gho : config.getGroupHasOperations()) {
      // If the operation exists, update it, otherwise create it.
      LOGGER.log(Level.FINEST, "Processing group has operation {0}-{1}",
          new String[]{gho.getGroupName(), gho.getOperationName()});
      QFAGroupDTO groupDTO = groupService.getGroupByName(gho.getGroupName(), true);
      if (!operationService.getAllowedGroupsForOperation(
          gho.getOperationName(), false).contains(groupDTO.getId())) {
        operationService.addOperationToGroup(
            groupDTO.getId(), gho.getOperationName(), gho.isDeny());
      }
    }
//    em.flush();

    // Create Template has Operations.
    for (QFAJSONConfig.TemplateHasOperation tho : config.getTemplateHasOperations()) {
      // If the operation exists, update it, otherwise create it.
      LOGGER.log(Level.FINEST, "Processing template has operation {0}-{1}",
          new String[]{tho.getTemplateName(), tho.getOperationName()});
      QFAOpTemplateDTO templateDTO = templateService.getTemplateByName(tho.getTemplateName());
      if (templateService.getOperationAccess(templateDTO.getId(), tho.getOperationName()) == null) {
        templateService.addOperation(templateDTO.getId(), tho.getOperationName(), tho.isDeny());
      }
    }
//    em.flush();
  }

  @PostConstruct
  public void init() {
    // Find AAA configurations.
    try {
      Enumeration<URL> entries = this.getClass().getClassLoader()
          .getResources("qlack-aaa-config.json");
      if (entries != null) {
        while (entries.hasMoreElements()) {
          parseConfig(entries.nextElement());
        }
      }
    } catch (IOException e) {
      LOGGER.log(Level.SEVERE, "Could not search QLACK Fuse AAA configuration files.", e);
    }
  }
}
