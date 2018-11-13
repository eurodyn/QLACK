package com.eurodyn.qlack.fuse.aaa.service;

import java.util.List;
import java.util.Set;

import com.eurodyn.qlack.fuse.aaa.dto.GroupHasOperationDTO;
import com.eurodyn.qlack.fuse.aaa.dto.OperationDTO;
import com.eurodyn.qlack.fuse.aaa.dto.ResourceDTO;

/**
 * @author European Dynamics SA
 */
public interface OperationService {


    void setPrioritisePositive(boolean prioritisePositive);

    String createOperation(OperationDTO operationDTO);

    void updateOperation(OperationDTO operationDTO);

    void deleteOperation(String operationID);

    List<OperationDTO> getAllOperations();

    OperationDTO getOperationByName(String operationName);

    void addOperationToUser(String userID, String operationName, boolean isDeny);

    void addOperationToUser(String userID, String operationName, String resourceID,
        boolean isDeny);

    void addOperationsToUserFromTemplateID(String userID, String templateID);

    void addOperationsToUserFromTemplateName(String userID, String templateName);

    void addOperationToGroup(String groupID, String operationName, boolean isDeny);

    void addOperationToGroup(String groupID, String operationName, String resourceID,
        boolean isDeny);

    void addOperationsToGroupFromTemplateID(String groupID, String templateID);

    void addOperationsToGroupFromTemplateName(String groupID, String templateName);

    void removeOperationFromUser(String userID, String operationName);

    void removeOperationFromUser(String userID, String operationName, String resourceID);

    void removeOperationFromGroup(String groupID, String operationName);

    void removeOperationFromGroup(String groupID, String operationName, String resourceID);

    Boolean isPermitted(String userId, String operationName);

    Boolean isPermitted(String userId, String operationName, String resourceObjectID);

    Boolean isPermittedForGroup(String groupID, String operationName);

    Boolean isPermittedForGroupByResource(String groupID, String operationName,
        String resourceName);

    Boolean isPermittedForGroup(String groupID, String operationName,
        String resourceObjectID);

    Set<String> getAllowedUsersForOperation(String operationName,
        boolean checkUserGroups);

    Set<String> getAllowedUsersForOperation(String operationName, String resourceObjectID,
        boolean checkUserGroups);

    Set<String> getBlockedUsersForOperation(String operationName,
        boolean checkUserGroups);

    Set<String> getBlockedUsersForOperation(String operationName, String resourceObjectID,
        boolean checkUserGroups);

    Set<String> getAllowedGroupsForOperation(String operationName, boolean checkAncestors);

    Set<String> getAllowedGroupsForOperation(String operationName,
        String resourceObjectID, boolean checkAncestors);

    Set<String> getBlockedGroupsForOperation(String operationName, boolean checkAncestors);

    Set<String> getBlockedGroupsForOperation(String operationName,
        String resourceObjectID, boolean checkAncestors);

    Set<String> getPermittedOperationsForUser(String userID, boolean checkUserGroups);

    Set<String> getPermittedOperationsForUser(String userID, String resourceObjectID,
        boolean checkUserGroups);

    Set<ResourceDTO> getResourceForOperation(String userID, String... operations);

    Set<ResourceDTO> getResourceForOperation(String userID,
        boolean getAllowed, String... operations);

    Set<ResourceDTO> getResourceForOperation(String userID,
        boolean getAllowed, boolean checkUserGroups, String... operations);

    OperationDTO getOperationByID(String operationID);

    List<String> getGroupIDsByOperationAndUser(String operationName, String userId);

    List<GroupHasOperationDTO> getGroupOperations(String groupName);

    List<GroupHasOperationDTO> getGroupOperations(List<String> groupNames);
}
