package com.eurodyn.qlack.fuse.aaa.util;


import com.eurodyn.qlack.fuse.aaa.dto.QFAGroupDTO;
import com.eurodyn.qlack.fuse.aaa.dto.QFAGroupHasOperationDTO;
import com.eurodyn.qlack.fuse.aaa.dto.QFAOpTemplateDTO;
import com.eurodyn.qlack.fuse.aaa.dto.QFAOperationAccessDTO;
import com.eurodyn.qlack.fuse.aaa.dto.QFAOperationDTO;
import com.eurodyn.qlack.fuse.aaa.dto.QFAResourceDTO;
import com.eurodyn.qlack.fuse.aaa.dto.QFASessionAttributeDTO;
import com.eurodyn.qlack.fuse.aaa.dto.QFASessionDTO;
import com.eurodyn.qlack.fuse.aaa.dto.QFAUserAttributeDTO;
import com.eurodyn.qlack.fuse.aaa.dto.QFAUserDTO;
import com.eurodyn.qlack.fuse.aaa.model.QFAGroup;
import com.eurodyn.qlack.fuse.aaa.model.QFAGroupHasOperation;
import com.eurodyn.qlack.fuse.aaa.model.QFAOpTemplate;
import com.eurodyn.qlack.fuse.aaa.model.QFAOpTemplateHasOperation;
import com.eurodyn.qlack.fuse.aaa.model.QFAOperation;
import com.eurodyn.qlack.fuse.aaa.model.QFAResource;
import com.eurodyn.qlack.fuse.aaa.model.QFASession;
import com.eurodyn.qlack.fuse.aaa.model.QFASessionAttribute;
import com.eurodyn.qlack.fuse.aaa.model.QFAUser;
import com.eurodyn.qlack.fuse.aaa.model.QFAUserAttribute;
import javax.persistence.EntityManager;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Utility class to convert
 * 1.transfer object to entity
 * 2.entity to transfer object.
 *
 * @author European Dynamics SA
 */
//TODO Convert to MapStruct
public class QFAConverterUtil {

  /**
   * This method is used for converting QFAUser entity to QFAUserDTO transfer object.
   * Please note that the password field in the QFAUserDTO object
   * IS NOT set. This is because the password stored in the database
   * is hashed and therefore it does not make sense returning it to the AAA
   * client since this field does not contain any useful value.
   *
   * @param entity Entity Object.
   * @return QFAUserDTO, Transfer object.
   */
  public static QFAUserDTO userToUserDTO(QFAUser entity) {
    if (entity == null) {
      return null;
    }

    QFAUserDTO dto = new QFAUserDTO();
    dto.setId(entity.getId());
    dto.setUsername(entity.getUsername());
    dto.setStatus(entity.getStatus());
    dto.setSuperadmin(entity.isSuperadmin());
    dto.setExternal(entity.isExternal() != null && entity.isExternal());

    Set<QFAUserAttributeDTO> userAttributeDtos = userAttributesToUserAttributeDTOSet(
        entity.getUserAttributes());
    dto.setUserAttributes(userAttributeDtos);

    return dto;
  }

  public static Set<QFAUserAttributeDTO> userAttributesToUserAttributeDTOSet(
      Collection<QFAUserAttribute> entities) {
    if (entities == null) {
      return null;
    }

    Set<QFAUserAttributeDTO> dtos = new HashSet<QFAUserAttributeDTO>();
    for (QFAUserAttribute entity : entities) {
      QFAUserAttributeDTO dto = userAttributeToUserAttributeDTO(entity);
      dtos.add(dto);
    }
    return dtos;
  }

  public static QFAUserAttributeDTO userAttributeToUserAttributeDTO(QFAUserAttribute entity) {
    if (entity == null) {
      return null;
    }
    QFAUserAttributeDTO dto = new QFAUserAttributeDTO();
    dto.setId(entity.getId());
    dto.setName(entity.getName());
    dto.setData(entity.getData());
    dto.setBinData(entity.getBindata());
    dto.setContentType(entity.getContentType());
    dto.setUserId(entity.getUser().getId());
    return dto;
  }

  /**
   * Converts a QFAUserDTO transfer object to QFAUser entity.
   * This method does not convert the createdOn and lastModifiedOn attributes
   * since these are set by the UserService whenever needed and not by the
   * parameters passed by callers. Additionally, this method does not set
   * the user password since it needs to be hashed before being stored in the DB.
   *
   * @param dto Transfer object.
   * @return QFAUser Entity Object.
   */
  public static QFAUser userDTOToUser(QFAUserDTO dto) {
    if (dto == null) {
      return null;
    }

    QFAUser entity = new QFAUser();
    if (StringUtils.isNotBlank(dto.getId())) {
      entity.setId(dto.getId());
    }
    entity.setUsername(dto.getUsername());
    entity.setStatus(dto.getStatus());
    entity.setSuperadmin(dto.isSuperadmin());
    entity.setExternal(dto.isExternal());
    entity.setUserAttributes(userAttributeDTOsToUserAttributeList(
        dto.getUserAttributes(), entity));
    return entity;
  }


  public static List<QFAUserAttribute> userAttributeDTOsToUserAttributeList(
      Collection<QFAUserAttributeDTO> dtos, QFAUser user) {
    if (dtos == null) {
      return null;
    }

    List<QFAUserAttribute> entities = new ArrayList<>();
    for (QFAUserAttributeDTO dto : dtos) {
      QFAUserAttribute entity = new QFAUserAttribute();
      entity.setUser(user);
      entity.setName(dto.getName());
      entity.setData(dto.getData());
      entity.setBindata(dto.getBinData());
      entity.setContentType(dto.getContentType());
      entities.add(entity);
    }
    return entities;
  }


  public static QFASessionDTO sessionToSessionDTO(QFASession entity) {
    if (entity == null) {
      return null;
    }

    QFASessionDTO dto = new QFASessionDTO();
    dto.setId(entity.getId());
    dto.setUserId(entity.getUser().getId());
    dto.setCreatedOn(entity.getCreatedOn());
    dto.setTerminatedOn(entity.getTerminatedOn());
    dto.setAttributes(sessionAttributesToSessionAttributeDTOSet(
        entity.getSessionAttributes()));
    dto.setApplicationSessionID(entity.getApplicationSessionId());

    return dto;
  }

  public static QFASessionAttributeDTO sessionAttributeToSessionAttributeDTO(QFASessionAttribute entity) {
    if (entity == null) {
      return null;
    }

    QFASessionAttributeDTO dto = new QFASessionAttributeDTO();
    dto.setId(entity.getId());
    dto.setName(entity.getName());
    dto.setValue(entity.getValue());
    dto.setSessionId(entity.getSession().getId());
    return dto;
  }

  public static Set<QFASessionAttributeDTO> sessionAttributesToSessionAttributeDTOSet(
      Collection<QFASessionAttribute> entities) {
    if (entities == null) {
      return null;
    }
    Set<QFASessionAttributeDTO> dtos = new HashSet<QFASessionAttributeDTO>();
    for (QFASessionAttribute attribute : entities) {
      dtos.add(sessionAttributeToSessionAttributeDTO(attribute));
    }
    return dtos;
  }

  public static QFASession sessionDTOToSession(QFASessionDTO dto, EntityManager em) {
    if (dto == null) {
      return null;
    }

    QFASession entity = new QFASession();
    entity.setCreatedOn(dto.getCreatedOn());
    entity.setTerminatedOn(dto.getTerminatedOn());
    entity.setApplicationSessionId(dto.getApplicationSessionID());
    entity.setUser(QFAUser.find(dto.getUserId(), em));
    entity.setSessionAttributes(sessionAttributeDTOsToSessionAttributeList(
        dto.getAttributes(), entity));
    return entity;
  }

  public static List<QFASessionAttribute> sessionAttributeDTOsToSessionAttributeList(
      Collection<QFASessionAttributeDTO> dtos, QFASession session) {
    if (dtos == null) {
      return null;
    }

    List<QFASessionAttribute> entities = new ArrayList<>();
    for (QFASessionAttributeDTO dto : dtos) {
      QFASessionAttribute entity = new QFASessionAttribute();
      entity.setSession(session);
      entity.setName(dto.getName());
      entity.setValue(dto.getValue());
      entities.add(entity);
    }
    return entities;
  }

  public static QFAGroupDTO groupToGroupDTO(QFAGroup entity, boolean lazyRelatives) {
    return groupToGroupDTO(entity, lazyRelatives, lazyRelatives);
  }

  /*
   * Separate lazy parent from lazy children in order to allow specifying lazyChildren = true
   * when getting a node's parent since in any other case we will get a stack overflow.
   * For consistency reasons we also specify lazyParent = true when getting a node's children
   */
  private static QFAGroupDTO groupToGroupDTO(QFAGroup entity, boolean lazyParent, boolean lazyChildren) {
    if (entity == null) {
      return null;
    }

    QFAGroupDTO dto = new QFAGroupDTO();
    dto.setId(entity.getId());
    dto.setDescription(entity.getDescription());
    dto.setName(entity.getName());
    dto.setObjectID(entity.getObjectId());

    if (!lazyParent) {
      dto.setParent(groupToGroupDTO(entity.getParent(), false, true));
    }
    if (!lazyChildren) {
      dto.setChildren(new HashSet<QFAGroupDTO>());
      for (QFAGroup child : entity.getChildren()) {
        dto.getChildren().add(groupToGroupDTO(child, true, false));
      }
    }

    return dto;
  }

  public static List<QFAGroupDTO> groupToGroupDTOList(Collection<QFAGroup> groups,
      boolean lazyRelatives) {
    if (groups == null) {
      return null;
    }

    List<QFAGroupDTO> retVal = new ArrayList<>(groups.size());

    // Use a HashSet as index in order to provide efficient indexing of
    // processed groups in the case where we need to re-use them as
    // other groups' relatives (if lazyRelatives = false).
    Map<String, QFAGroupDTO> groupIndex = new HashMap<>();
    for (QFAGroup group : groups) {
      // If the group has already been processed as a parent/child of another
      // group then use the QFAGroupDTO instance already created; otherwise create
      // a new QFAGroupDTO instance for this group.
      QFAGroupDTO dto = groupIndex.get(group.getId());
      if (dto == null) {
        // Do not handle lazyRelatives in the groupToGroupDTO method since
        // we will handle them in this method in order to only create one
        // QFAGroupDTO instance for each group and use this instance wherever
        // the group is referenced (as parent, child, etc.).
        dto = groupToGroupDTO(group, true);
      }

      if (!lazyRelatives) {
        if (!groupIndex.containsKey(dto.getId())) {
          groupIndex.put(dto.getId(), dto);
        }

        // If the group has a parent check if it has already been processed in
        // order to use the already existing instance, otherwise process it
        // and add it to the groupIndex to be used later. Same for the group
        // children.
        if (group.getParent() != null) {
          QFAGroupDTO parent = groupIndex.get(group.getParent().getId());
          if (parent == null) {
            parent = groupToGroupDTO(group.getParent(), false);
            groupIndex.put(parent.getId(), parent);
          }
          dto.setParent(parent);
        }
        dto.setChildren(new HashSet<QFAGroupDTO>());
        for (QFAGroup child : group.getChildren()) {
          QFAGroupDTO childDTO = groupIndex.get(child.getId());
          if (childDTO == null) {
            childDTO = groupToGroupDTO(child, false);
            groupIndex.put(childDTO.getId(), childDTO);
          }
          dto.getChildren().add(childDTO);
        }
      }

      retVal.add(dto);
    }

    return retVal;
  }


  public static QFAOperationDTO operationToOperationDTO(QFAOperation entity) {
    if (entity == null) {
      return null;
    }

    QFAOperationDTO dto = new QFAOperationDTO();
    dto.setDescription(entity.getDescription());
    dto.setDynamic(entity.isDynamic());
    dto.setDynamicCode(entity.getDynamicCode());
    dto.setId(entity.getId());
    dto.setName(entity.getName());
    return dto;
  }

  public static List<QFAOperationDTO> operationToOperationDTOList(List<QFAOperation> entities) {
    if (entities == null) {
      return null;
    }

    List<QFAOperationDTO> dtos = new ArrayList<>(entities.size());
    for (QFAOperation entity : entities) {
      dtos.add(operationToOperationDTO(entity));
    }
    return dtos;
  }


  public static QFAOpTemplateDTO opTemplateToOpTemplateDTO(QFAOpTemplate entity) {
    if (entity == null) {
      return null;
    }

    QFAOpTemplateDTO dto = new QFAOpTemplateDTO();
    dto.setId(entity.getId());
    dto.setName(entity.getName());
    dto.setDescription(entity.getDescription());
    dto.setOperations(
        opTemplateHasOperationsToOperationAccessDTOSet(entity.getOpTemplateHasOperations()));
    return dto;
  }


  public static QFAOperationAccessDTO opTemplateHasOperationToOperationAccessDTO(
      QFAOpTemplateHasOperation entity) {
    if (entity == null) {
      return null;
    }

    QFAOperationAccessDTO dto = new QFAOperationAccessDTO();
    dto.setOperation(operationToOperationDTO(entity.getOperation()));
    dto.setResource(resourceToResourceDTO(entity.getResource()));
    dto.setDeny(entity.isDeny());
    return dto;
  }


  public static Set<QFAOperationAccessDTO> opTemplateHasOperationsToOperationAccessDTOSet(
      Collection<QFAOpTemplateHasOperation> entities) {
    if (entities == null) {
      return null;
    }

    Set<QFAOperationAccessDTO> dtos = new HashSet<>();
    for (QFAOpTemplateHasOperation entity : entities) {
      dtos.add(opTemplateHasOperationToOperationAccessDTO(entity));
    }
    return dtos;
  }


  public static QFAResourceDTO resourceToResourceDTO(QFAResource entity) {
    if (entity == null) {
      return null;
    }

    QFAResourceDTO dto = new QFAResourceDTO();
    dto.setId(entity.getId());
    dto.setName(entity.getName());
    dto.setDescription(entity.getDescription());
    dto.setObjectID(entity.getObjectId());
    return dto;
  }

  public static List<QFAGroupHasOperationDTO> groupHasOperationToGroupHasOperationDTO(
      List<QFAGroupHasOperation> entities) {
    if (entities == null) {
      return null;
    }

    List<QFAGroupHasOperationDTO> dtos = new ArrayList<>();
    for (QFAGroupHasOperation entity : entities) {
      dtos.add(groupHasOperationToGroupHasOperationDTO(entity));
    }
    return dtos;
  }

  public static QFAGroupHasOperationDTO groupHasOperationToGroupHasOperationDTO(
      QFAGroupHasOperation entity) {
    if (entity == null) {
      return null;
    }
    QFAGroupHasOperationDTO dto = new QFAGroupHasOperationDTO();
    dto.setId(entity.getId());
    dto.setGroupDTO(groupToGroupDTO(entity.getGroup(), false));
    dto.setResourceDTO(resourceToResourceDTO(entity.getResource()));
    dto.setOperationDTO(operationToOperationDTO(entity.getOperation()));
    return dto;
  }
}
