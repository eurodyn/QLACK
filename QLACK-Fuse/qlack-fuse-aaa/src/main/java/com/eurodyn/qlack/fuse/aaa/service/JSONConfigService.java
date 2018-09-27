package com.eurodyn.qlack.fuse.aaa.service;

import com.eurodyn.qlack.fuse.aaa.dto.GroupDTO;
import com.eurodyn.qlack.fuse.aaa.dto.JSONConfig;
import com.eurodyn.qlack.fuse.aaa.dto.OpTemplateDTO;
import com.eurodyn.qlack.fuse.aaa.dto.OperationDTO;
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
public class JSONConfigService {

  // Logger
  private static final Logger LOGGER = Logger.getLogger(JSONConfigService.class.getName());

  // JSON parser.
  ObjectMapper mapper = new ObjectMapper();

//  @PersistenceContext
//  private EntityManager em;

  private UserGroupService groupService;
  private OpTemplateService templateService;
  private OperationService operationService;

  @Autowired
  public JSONConfigService(UserGroupService groupService,
      OpTemplateService templateService,
      OperationService operationService) {
    this.groupService = groupService;
    this.templateService = templateService;
    this.operationService = operationService;
  }

  private void parseConfig(URL configFileURL) {
    LOGGER.log(Level.FINE, "Handling FUSE AAA configuration: {0}.", configFileURL.toExternalForm());

    // Parse the JSON file.
    JSONConfig config = null;
    try {
      config = mapper.readValue(configFileURL, JSONConfig.class);
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
    for (JSONConfig.Group g : config.getGroups()) {
      // If the group exists, update it, otherwise create it.
      LOGGER.log(Level.FINEST, "Processing group {0}", g.getName());
      GroupDTO groupDTO = groupService.getGroupByName(g.getName(), true);
      boolean isNew = groupDTO == null;
      if (isNew) {
        groupDTO = new GroupDTO();
      }
      groupDTO.setDescription(g.getDescription());
      groupDTO.setName(g.getName());
      if (StringUtils.isNotBlank(g.getParentGroupName())) {
        GroupDTO parentGroup = groupService.getGroupByName(g.getParentGroupName(), true);
        groupDTO.setParent(new GroupDTO(parentGroup.getId()));
      }
      if (isNew) {
        groupService.createGroup(groupDTO);
      } else {
        groupService.updateGroup(groupDTO);
      }
    }
//    em.flush();

    // Create templates.
    for (JSONConfig.Template t : config.getTemplates()) {
      // If the template exists, update it, otherwise create it.
      LOGGER.log(Level.FINEST, "Processing template {0}", t.getName());
      OpTemplateDTO templateDTO = templateService.getTemplateByName(t.getName());
      boolean isNew = templateDTO == null;
      if (isNew) {
        templateDTO = new OpTemplateDTO();
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
    for (JSONConfig.Operation o : config.getOperations()) {
      // If the operation exists, update it, otherwise create it.
      LOGGER.log(Level.FINEST, "Processing operation {0}", o.getName());
      OperationDTO opDTO = operationService.getOperationByName(o.getName());
      boolean isNew = opDTO == null;
      if (isNew) {
        opDTO = new OperationDTO();
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

    // Create Group has Operations.
    for (JSONConfig.GroupHasOperation gho : config.getGroupHasOperations()) {
      // If the operation exists, update it, otherwise create it.
      LOGGER.log(Level.FINEST, "Processing group has operation {0}-{1}",
          new String[]{gho.getGroupName(), gho.getOperationName()});
      GroupDTO groupDTO = groupService.getGroupByName(gho.getGroupName(), true);
      if (!operationService.getAllowedGroupsForOperation(
          gho.getOperationName(), false).contains(groupDTO.getId())) {
        operationService.addOperationToGroup(
            groupDTO.getId(), gho.getOperationName(), gho.isDeny());
      }
    }
//    em.flush();

    // Create Template has Operations.
    for (JSONConfig.TemplateHasOperation tho : config.getTemplateHasOperations()) {
      // If the operation exists, update it, otherwise create it.
      LOGGER.log(Level.FINEST, "Processing template has operation {0}-{1}",
          new String[]{tho.getTemplateName(), tho.getOperationName()});
      OpTemplateDTO templateDTO = templateService.getTemplateByName(tho.getTemplateName());
      if (templateService.getOperationAccess(templateDTO.getId(), tho.getOperationName()) == null) {
        templateService.addOperation(templateDTO.getId(), tho.getOperationName(), tho.isDeny());
      }
    }
//    em.flush();
  }

  @PostConstruct
  public void init() {
    initWithFile("qlack-aaa-config.json");
  }

  public void initWithFile(String configFile) {
    // Find AAA configurations.
    try {
      Enumeration<URL> entries = this.getClass().getClassLoader()
          .getResources(configFile);
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
