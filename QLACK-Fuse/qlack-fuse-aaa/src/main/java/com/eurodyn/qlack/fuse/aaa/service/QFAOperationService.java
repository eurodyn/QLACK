package com.eurodyn.qlack.fuse.aaa.service;

import bsh.EvalError;
import bsh.Interpreter;
import com.eurodyn.qlack.fuse.aaa.dto.QFAGroupHasOperationDTO;
import com.eurodyn.qlack.fuse.aaa.dto.QFAOperationDTO;
import com.eurodyn.qlack.fuse.aaa.dto.QFAResourceDTO;
import com.eurodyn.qlack.fuse.aaa.exception.QFADynamicOperationException;
import com.eurodyn.qlack.fuse.aaa.model.QFAGroup;
import com.eurodyn.qlack.fuse.aaa.model.QFAGroupHasOperation;
import com.eurodyn.qlack.fuse.aaa.model.QFAOpTemplate;
import com.eurodyn.qlack.fuse.aaa.model.QFAOpTemplateHasOperation;
import com.eurodyn.qlack.fuse.aaa.model.QFAOperation;
import com.eurodyn.qlack.fuse.aaa.model.QFAResource;
import com.eurodyn.qlack.fuse.aaa.model.QFAUser;
import com.eurodyn.qlack.fuse.aaa.model.QFAUserHasOperation;
import com.eurodyn.qlack.fuse.aaa.util.QFAConverterUtil;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author European Dynamics SA
 */
@Service
@Validated
@Transactional
public class QFAOperationService {

  private static final Logger LOGGER = Logger.getLogger(QFAOperationService.class.getName());
  @PersistenceContext
  private EntityManager em;

  private boolean prioritisePositive;

  public void setPrioritisePositive(boolean prioritisePositive) {
    this.prioritisePositive = prioritisePositive;
  }

  public String createOperation(QFAOperationDTO operationDTO) {
    QFAOperation operation = new QFAOperation();
    operation.setDescription(operationDTO.getDescription());
    operation.setDynamicCode(operationDTO.getDynamicCode());
    operation.setDynamic(operationDTO.isDynamic());
    operation.setName(operationDTO.getName());
    em.persist(operation);

    return operation.getId();
  }

  public void updateOperation(QFAOperationDTO operationDTO) {
    QFAOperation operation = QFAOperation.find(operationDTO.getId(), em);
    operation.setName(operationDTO.getName());
    operation.setDescription(operationDTO.getDescription());
    operation.setDynamic(operationDTO.isDynamic());
    operation.setDynamicCode(operationDTO.getDynamicCode());
  }

  public void deleteOperation(String operationID) {
    em.remove(QFAOperation.find(operationID, em));
  }

  public List<QFAOperationDTO> getAllOperations() {
    return QFAConverterUtil.operationToOperationDTOList(QFAOperation.findAll(em));
  }

  public QFAOperationDTO getOperationByName(String operationName) {
    return QFAConverterUtil.operationToOperationDTO(QFAOperation.findByName(operationName, em));
  }

  public void addOperationToUser(String userID, String operationName, boolean isDeny) {
    QFAUserHasOperation uho = QFAUserHasOperation
        .findByUserIDAndOperationName(userID, operationName, em);
    if (uho != null) {
      uho.setDeny(isDeny);
    } else {
      QFAUser user = QFAUser.find(userID, em);
      QFAOperation operation = QFAOperation.findByName(operationName, em);
      uho = new QFAUserHasOperation();
      uho.setDeny(isDeny);
      user.addUserHasOperation(uho);
      operation.addUserHasOperation(uho);
      em.persist(uho);
    }
  }

  public void addOperationToUser(String userID, String operationName, String resourceID,
      boolean isDeny) {
    QFAUserHasOperation uho = QFAUserHasOperation.findByUserAndResourceIDAndOperationName(
        userID, operationName, resourceID, em);
    if (uho != null) {
      uho.setDeny(isDeny);
    } else {
      QFAUser user = QFAUser.find(userID, em);
      QFAOperation operation = QFAOperation.findByName(operationName, em);
      QFAResource resource = QFAResource.find(resourceID, em);
      uho = new QFAUserHasOperation();
      uho.setDeny(isDeny);
      user.addUserHasOperation(uho);
      operation.addUserHasOperation(uho);
      resource.addUserHasOperation(uho);
      em.persist(uho);
    }
  }

  public void addOperationsToUserFromTemplateID(String userID, String templateID) {
    QFAOpTemplate template = QFAOpTemplate.find(templateID, em);
    addOperationsToUserFromTemplate(userID, template);
  }

  public void addOperationsToUserFromTemplateName(String userID, String templateName) {
    QFAOpTemplate template = QFAOpTemplate.findByName(templateName, em);
    addOperationsToUserFromTemplate(userID, template);
  }

  private void addOperationsToUserFromTemplate(String userID, QFAOpTemplate template) {
    for (QFAOpTemplateHasOperation tho : template.getOpTemplateHasOperations()) {
      if (tho.getResource() == null) {
        addOperationToUser(userID, tho.getOperation().getName(), tho.isDeny());
      } else {
        addOperationToUser(userID, tho.getOperation().getName(), tho.getResource().getId(),
            tho.isDeny());
      }
    }
  }

  public void addOperationToGroup(String groupID, String operationName, boolean isDeny) {
    QFAGroupHasOperation gho = QFAGroupHasOperation
        .findByGroupIDAndOperationName(groupID, operationName, em);
    if (gho != null) {
      gho.setDeny(isDeny);
    } else {
      QFAGroup group = QFAGroup.find(groupID, em);
      QFAOperation operation = QFAOperation.findByName(operationName, em);
      gho = new QFAGroupHasOperation();
      gho.setDeny(isDeny);
      group.addGroupHasOperation(gho);
      operation.addGroupHasOperation(gho);
      em.persist(gho);
    }
  }

  public void addOperationToGroup(String groupID, String operationName, String resourceID,
      boolean isDeny) {
    QFAGroupHasOperation gho = QFAGroupHasOperation.findByGroupAndResourceIDAndOperationName(
        groupID, operationName, resourceID, em);
    if (gho != null) {
      gho.setDeny(isDeny);
    } else {
      QFAGroup group = QFAGroup.find(groupID, em);
      QFAOperation operation = QFAOperation.findByName(operationName, em);
      QFAResource resource = QFAResource.find(resourceID, em);
      gho = new QFAGroupHasOperation();
      gho.setDeny(isDeny);
      group.addGroupHasOperation(gho);
      operation.addGroupHasOperation(gho);
      resource.addGroupHasOperation(gho);
      em.persist(gho);
    }
  }

  public void addOperationsToGroupFromTemplateID(String groupID, String templateID) {
    QFAOpTemplate template = QFAOpTemplate.find(templateID, em);
    addOperationsToGroupFromTemplate(groupID, template);
  }

  public void addOperationsToGroupFromTemplateName(String groupID, String templateName) {
    QFAOpTemplate template = QFAOpTemplate.findByName(templateName, em);
    addOperationsToGroupFromTemplate(groupID, template);
  }

  private void addOperationsToGroupFromTemplate(String groupID, QFAOpTemplate template) {
    for (QFAOpTemplateHasOperation tho : template.getOpTemplateHasOperations()) {
      if (tho.getResource() == null) {
        addOperationToGroup(groupID, tho.getOperation().getName(), tho.isDeny());
      } else {
        addOperationToGroup(groupID, tho.getOperation().getName(), tho.getResource().getId(),
            tho.isDeny());
      }
    }
  }

  public void removeOperationFromUser(String userID, String operationName) {
    QFAUserHasOperation uho = QFAUserHasOperation
        .findByUserIDAndOperationName(userID, operationName, em);
    if (uho != null) {
      em.remove(uho);
    }
  }

  public void removeOperationFromUser(String userID, String operationName, String resourceID) {
    QFAUserHasOperation uho = QFAUserHasOperation
        .findByUserAndResourceIDAndOperationName(userID, operationName, resourceID, em);
    if (uho != null) {
      em.remove(uho);
    }
  }

  public void removeOperationFromGroup(String groupID, String operationName) {
    QFAGroupHasOperation gho = QFAGroupHasOperation
        .findByGroupIDAndOperationName(groupID, operationName, em);
    if (gho != null) {
      em.remove(gho);
    }
  }

  public void removeOperationFromGroup(String groupID, String operationName, String resourceID) {
    QFAGroupHasOperation gho = QFAGroupHasOperation
        .findByGroupAndResourceIDAndOperationName(groupID, operationName, resourceID, em);
    if (gho != null) {
      em.remove(gho);
    }
  }

  public Boolean isPermitted(String userId, String operationName) {
    return isPermitted(userId, operationName, null);
  }

  public Boolean isPermitted(String userId, String operationName, String resourceObjectID) {
    LOGGER.log(
        Level.FINEST,
        "Checking permissions for user ''{0}'', operation ''{1}'' and resource object ID ''{2}''.",
        new String[]{userId, operationName, resourceObjectID});
    QFAUser user = QFAUser.find(userId, em);

    // If the user is a superadmin then the operation is permitted
    // by definition
    if (user.isSuperadmin()) {
      return true;
    }

    QFAOperation operation = QFAOperation.findByName(operationName, em);
    String resourceId =
        (resourceObjectID == null) ? null : QFAResource.findByObjectID(resourceObjectID, em).getId();

    Boolean retVal = null;
    QFAUserHasOperation uho = (resourceId == null)
        ? QFAUserHasOperation.findByUserIDAndOperationName(userId, operationName, em)
        : QFAUserHasOperation
            .findByUserAndResourceIDAndOperationName(userId, operationName, resourceId, em);

    // Check the user's permission on the operation
    if (uho != null) {
      // First check whether this is a dynamic operation.
      if (operation.isDynamic()) {
        retVal = evaluateDynamicOperation(operation, userId, null,
            resourceObjectID);
      } else {
        retVal = !uho.isDeny();
      }
    }
    // If no user permission on the operation exists check the permissions for the user groups.
    else {
      List<QFAGroup> userGroups = user.getGroups();
      for (QFAGroup group : userGroups) {
        Boolean groupPermission;
        groupPermission = isPermittedForGroup(group.getId(), operationName, resourceObjectID);
        if (groupPermission != null) {
          // Assign the permission we got for the group to the return value only if
          // a. We haven't found another permission for this user so far
          // b. The groupPermission is true and we are prioritising positive permissions or
          // the groupPermission is false and we are prioritising negative permissions.
          if ((retVal == null) || (groupPermission == prioritisePositive)) {
            retVal = groupPermission;
          }

        }
      }
    }

    return retVal;
  }

  public Boolean isPermittedForGroup(String groupID, String operationName) {
    return isPermittedForGroup(groupID, operationName, null);
  }

  public Boolean isPermittedForGroupByResource(String groupID, String operationName,
      String resourceName) {
    LOGGER.log(Level.FINEST,
        "Checking permissions for group {0}, operation {1} and resource with object ID {2}.",
        new String[]{groupID, operationName, resourceName});

    QFAGroup group = QFAGroup.find(groupID, em);
    QFAOperation operation = QFAOperation.findByName(operationName, em);

    Boolean retVal = null;
    QFAGroupHasOperation gho = QFAGroupHasOperation
        .findByGroupIDAndOperationNameAndResourceName(groupID, operationName, resourceName, em);
    if (gho != null) {
      retVal = !gho.isDeny();
    } else if (group.getParent() != null) {
      // If this group is not assigned the operation check the group's
      // parents until a result is found or until no other parent exists.
      retVal = isPermittedForGroup(group.getParent().getId(), operationName, resourceName);
    }

    return retVal;
  }

  public Boolean isPermittedForGroup(String groupID, String operationName,
      String resourceObjectID) {
    LOGGER.log(Level.FINEST,
        "Checking permissions for group {0}, operation {1} and resource with object ID {2}.",
        new String[]{groupID, operationName, resourceObjectID});

    QFAGroup group = QFAGroup.find(groupID, em);
    QFAOperation operation = QFAOperation.findByName(operationName, em);
    String resourceId =
        (resourceObjectID == null) ? null : QFAResource.findByObjectID(resourceObjectID, em).getId();

    Boolean retVal = null;
    QFAGroupHasOperation gho = (resourceId == null)
        ? QFAGroupHasOperation.findByGroupIDAndOperationName(groupID, operationName, em)
        : QFAGroupHasOperation
            .findByGroupAndResourceIDAndOperationName(groupID, operationName, resourceId, em);
    if (gho != null) {
      // First check whether this is a dynamic operation.
      if (operation.isDynamic()) {
        retVal = evaluateDynamicOperation(operation, null, groupID,
            resourceObjectID);
      } else {
        retVal = !gho.isDeny();
      }
    } else if (group.getParent() != null) {
      // If this group is not assigned the operation check the group's
      // parents until a result is found or until no other parent exists.
      retVal = isPermittedForGroup(group.getParent().getId(), operationName, resourceObjectID);
    }

    return retVal;
  }

  private Set<String> getUsersForOperation(String operationName,
      String resourceObjectID, boolean checkUserGroups, boolean getAllowed) {
    Set<String> allUsers = QFAUser.getNormalUserIds(em);
    // Superadmin users are allowed the operation by default
    Set<String> returnedUsers = new HashSet<>();
    if (getAllowed) {
      returnedUsers = QFAUser.getSuperadminUserIds(em);
    } else {
      for (String superadminId : QFAUser.getSuperadminUserIds(em)) {
        allUsers.remove(superadminId);
      }
    }

    String resourceId = null;
    if (resourceObjectID != null) {
      resourceId = QFAResource.findByObjectID(resourceObjectID, em).getId();
    }

    // First check the permissions of users themselves
    List<QFAUserHasOperation> uhoList;
    if (resourceId == null) {
      uhoList = QFAUserHasOperation.findByOperationName(operationName, em);
    } else {
      uhoList = QFAUserHasOperation.findByResourceIDAndOperationName(operationName, resourceId, em);
    }
    for (QFAUserHasOperation uho : uhoList) {
      allUsers.remove(uho.getUser().getId());

      // Check if operation is dynamic and if yes evaluate the operation
      if (uho.getOperation().isDynamic()) {
        Boolean dynamicResult = evaluateDynamicOperation(uho.getOperation(),
            uho.getUser().getId(), null, resourceObjectID);
        if ((dynamicResult != null) && (dynamicResult.booleanValue() == getAllowed)) {
          returnedUsers.add(uho.getUser().getId());
        }
      } else if (!uho.isDeny() == getAllowed) {
        returnedUsers.add(uho.getUser().getId());
      }
    }

    // Then iterate over the remaining users to check group permissions
    if (checkUserGroups) {
      // Using Iterator to iterate over allUsers in order to avoid
      // ConcurrentModificationException caused by user removal in the for loop
      Iterator<String> userIt = allUsers.iterator();
      while (userIt.hasNext()) {
        String userId = userIt.next();
        List<QFAGroup> userGroups = QFAUser.find(userId, em).getGroups();
        Boolean userPermission = null;
        for (QFAGroup group : userGroups) {
          Boolean groupPermission;
          if (resourceObjectID == null) {
            groupPermission = isPermittedForGroup(group.getId(), operationName);
          } else {
            groupPermission = isPermittedForGroup(group.getId(), operationName, resourceObjectID);
          }
          // We have the following cases depending on the group permission:
          // a. If it was positive and we are prioritising positive permissions the user
          // is allowed and we end the check for this user. The user will be added to
          // the returned users if getAllowed == true.
          // b. If it was negative and we are prioritising negative permissions the user
          // is not allowed and we end the check for this user. The user will be added to
          // the returned users if getAllowed == false.
          // c. In all other cases we wait until the rest of the user groups are checked
          // before we make a final decision. For this reason we assign the groupPermission
          // to the userPermission variable to be checked after group check is finished.
          if (groupPermission != null) {
            userIt.remove();
            if (groupPermission.booleanValue() == prioritisePositive) {
              if (groupPermission.booleanValue() == getAllowed) {
                returnedUsers.add(userId);
              }
              userPermission = null;
              break;
            } else {
              userPermission = groupPermission;
            }
          }
        }
        if ((userPermission != null) && (userPermission.booleanValue() == getAllowed)) {
          returnedUsers.add(userId);
        }
      }
    }

    return returnedUsers;
  }

  public Set<String> getAllowedUsersForOperation(String operationName,
      boolean checkUserGroups) {
    return getUsersForOperation(operationName, null, checkUserGroups, true);
  }

  public Set<String> getAllowedUsersForOperation(String operationName, String resourceObjectID,
      boolean checkUserGroups) {
    return getUsersForOperation(operationName, resourceObjectID, checkUserGroups, true);
  }

  public Set<String> getBlockedUsersForOperation(String operationName,
      boolean checkUserGroups) {
    return getUsersForOperation(operationName, null, checkUserGroups, false);
  }

  public Set<String> getBlockedUsersForOperation(String operationName, String resourceObjectID,
      boolean checkUserGroups) {
    return getUsersForOperation(operationName, resourceObjectID, checkUserGroups, false);
  }

  private Set<String> getGroupsForOperation(String operationName, String resourceObjectID,
      boolean checkAncestors, boolean getAllowed) {
    Set<String> allGroups = QFAGroup.getAllGroupIds(em);
    Set<String> returnedGroups = new HashSet<>();

    String resourceId = null;
    if (resourceObjectID != null) {
      resourceId = QFAResource.findByObjectID(resourceObjectID, em).getId();
    }
    List<QFAGroupHasOperation> ghoList;
    if (resourceId == null) {
      ghoList = QFAGroupHasOperation.findByOperationName(operationName, em);
    } else {
      ghoList = QFAGroupHasOperation.findByResourceIDAndOperationName(
          operationName, resourceId, em);
    }
    for (QFAGroupHasOperation gho : ghoList) {
      allGroups.remove(gho.getGroup().getId());

      // Check if operation is dynamic and if yes evaluate the operation
      if (gho.getOperation().isDynamic()) {
        Boolean dynamicResult = evaluateDynamicOperation(gho.getOperation(),
            null, gho.getGroup().getId(), null);
        if ((dynamicResult != null) && (dynamicResult.booleanValue() == getAllowed)) {
          returnedGroups.add(gho.getGroup().getId());
        }
      } else if (!gho.isDeny() == getAllowed) {
        returnedGroups.add(gho.getGroup().getId());
      }
    }

    // Check the ancestors of the remaining groups if so requested
    if (checkAncestors) {
      for (String groupId : allGroups) {
        Boolean groupPermission;
        if (resourceObjectID == null) {
          groupPermission = isPermittedForGroup(groupId, operationName);
        } else {
          groupPermission = isPermittedForGroup(groupId, operationName, resourceObjectID);
        }
        if ((groupPermission != null) && (groupPermission.booleanValue() == getAllowed)) {
          returnedGroups.add(groupId);
        }
      }
    }

    return returnedGroups;
  }

  public Set<String> getAllowedGroupsForOperation(String operationName, boolean checkAncestors) {
    return getGroupsForOperation(operationName, null, checkAncestors, true);
  }

  public Set<String> getAllowedGroupsForOperation(String operationName,
      String resourceObjectID, boolean checkAncestors) {
    return getGroupsForOperation(operationName, resourceObjectID, checkAncestors, true);
  }

  public Set<String> getBlockedGroupsForOperation(String operationName, boolean checkAncestors) {
    return getGroupsForOperation(operationName, null, checkAncestors, false);
  }

  public Set<String> getBlockedGroupsForOperation(String operationName,
      String resourceObjectID, boolean checkAncestors) {
    return getGroupsForOperation(operationName, resourceObjectID, checkAncestors, false);
  }

  private Boolean evaluateDynamicOperation(QFAOperation operation,
      String userID, String groupID, String resourceObjectID) {
    LOGGER.log(Level.FINEST, "Evaluating dynamic operation ''{0}''.",
        operation.getName());

    Boolean retVal;
    String algorithm = operation.getDynamicCode();
    // Create a BeanShell interpreter for this operation.
    Interpreter i = new Interpreter();
    // Pass parameters to the algorithm.
    try {
      i.set("userID", userID);
      i.set("groupID", groupID);
      i.set("resourceObjectID", resourceObjectID);
      i.set("entitymanager", em);
      i.eval(algorithm);
      retVal = ((Boolean) i.get("retVal")).booleanValue();
    } catch (EvalError ex) {
      // Catching the EvalError in order to convert it to
      // a RuntimeException which will also rollback the transaction.
      throw new QFADynamicOperationException(
          "Error evaluating dynamic operation '"
              + operation.getName() + "'.");
    }

    return retVal;
  }

  public Set<String> getPermittedOperationsForUser(String userID, boolean checkUserGroups) {
    QFAUser user = QFAUser.find(userID, em);
    return getOperationsForUser(user, null, checkUserGroups);
  }

  public Set<String> getPermittedOperationsForUser(String userID, String resourceObjectID,
      boolean checkUserGroups) {
    QFAUser user = QFAUser.find(userID, em);
    QFAResource resource = QFAResource.findByObjectID(resourceObjectID, em);
    return getOperationsForUser(user, resource, checkUserGroups);
  }

  private Set<String> getOperationsForUser(QFAUser user, QFAResource resource,
      boolean checkUserGroups) {
    Set<String> allowedOperations = new HashSet<>();
    Set<String> deniedOperations = new HashSet<>();

    // If the user is a superadmin then they are allowed all operations
    if (user.isSuperadmin()) {
      for (QFAOperation operation : QFAOperation.findAll(em)) {
        allowedOperations.add(operation.getName());
      }
    } else {
      // Check operations attributed to the user
      for (QFAUserHasOperation uho : user.getUserHasOperations()) {
        if (uho.getResource() == resource) {
          if ((uho.getOperation().isDynamic() && evaluateDynamicOperation(
              uho.getOperation(), user.getId(), null, resource.getObjectId()))
              || (!uho.getOperation().isDynamic() && !uho.isDeny())) {
            allowedOperations.add(uho.getOperation().getName());
          } else if ((uho.getOperation().isDynamic() && !evaluateDynamicOperation(
              uho.getOperation(), user.getId(), null, resource.getObjectId()))
              || (!uho.getOperation().isDynamic() && uho.isDeny())) {
            deniedOperations.add(uho.getOperation().getId());
          }
        }
      }
      if (checkUserGroups) {
        // Then check operations the user may have via their groups
        Set<String> allowedGroupOperations = new HashSet<>();
        Set<String> deniedGroupOperations = new HashSet<>();
        // First get all the operations allowed or denied through the user groups
        for (QFAGroup group : user.getGroups()) {
          while (group != null) {
            allowedGroupOperations.addAll(getOperationsForGroup(group, resource, true));
            deniedGroupOperations.addAll(getOperationsForGroup(group, resource, false));
            group = group.getParent();
          }
        }
        // And then check for each allowed operation if it is explicitly denied
        // to the user or if it is denied through another group (only if prioritisePositive == false)
        for (String groupOperation : allowedGroupOperations) {
          if (!deniedOperations.contains(groupOperation)
              && (prioritisePositive || (!deniedGroupOperations.contains(groupOperation)))) {
            allowedOperations.add(groupOperation);
          }
        }
      }
    }

    return allowedOperations;
  }

  private Set<String> getOperationsForGroup(QFAGroup group, QFAResource resource, boolean allowed) {
    Set<String> retVal = new HashSet<>();
    for (QFAGroupHasOperation gho : group.getGroupHasOperations()) {
      if (gho.getResource() == resource) {
        String resourceObjectID = (resource != null) ? resource.getObjectId() : null;
        if ((gho.getOperation().isDynamic() &&
            (evaluateDynamicOperation(gho.getOperation(), null, group.getId(), resourceObjectID)
                == allowed))
            || (!gho.getOperation().isDynamic() && (!gho.isDeny() == allowed))) {
          retVal.add(gho.getOperation().getName());
        }
      }
    }
    return retVal;
  }

  public Set<QFAResourceDTO> getResourceForOperation(String userID, String operationName,
      boolean getAllowed) {
    return getResourceForOperation(userID, operationName, getAllowed, false);
  }

  public Set<QFAResourceDTO> getResourceForOperation(String userID, String operationName,
      boolean getAllowed, boolean checkUserGroups) {
    Set<QFAResourceDTO> resourceDTOList = new HashSet<>();
    QFAUser user = QFAUser.find(userID, em);
    for (QFAUserHasOperation uho : user.getUserHasOperations()) {
      if (uho.isDeny() != getAllowed && uho.getOperation().getName().equals(operationName)) {
        resourceDTOList
            .add(QFAConverterUtil
                .resourceToResourceDTO(QFAResource.find(uho.getResource().getId(), em)));
      }
    }
    /* also the resources of the groups the user belongs to should be retrieved */
    if (checkUserGroups) {
      for (QFAGroup group : user.getGroups()) {
        for (QFAGroupHasOperation gho : group.getGroupHasOperations()) {
          if (gho.isDeny() != getAllowed && gho.getOperation().getName().equals(operationName)) {
            resourceDTOList.add(
                QFAConverterUtil
                    .resourceToResourceDTO(QFAResource.find(gho.getResource().getId(), em)));
          }
        }
      }
    }
    return resourceDTOList;
  }

  public QFAOperationDTO getOperationByID(String operationID) {
    QFAOperation o = QFAOperation.find(operationID, em);
    if (o != null) {
      return QFAConverterUtil.operationToOperationDTO(o);
    } else {
      return null;
    }
  }

  public List<String> getGroupIDsByOperationAndUser(String operationName, String userId) {
    return null;
  }

  public List<QFAGroupHasOperationDTO> getGroupOperations(String groupName) {
    List<QFAGroupHasOperation> entities = QFAGroupHasOperation.findByGroupName(groupName, em);
    return QFAConverterUtil.groupHasOperationToGroupHasOperationDTO(entities);
  }

  public List<QFAGroupHasOperationDTO> getGroupOperations(List<String> groupNames) {
    List<QFAGroupHasOperation> entities = QFAGroupHasOperation.findByGroupName(groupNames, em);
    return QFAConverterUtil.groupHasOperationToGroupHasOperationDTO(entities);
  }
}