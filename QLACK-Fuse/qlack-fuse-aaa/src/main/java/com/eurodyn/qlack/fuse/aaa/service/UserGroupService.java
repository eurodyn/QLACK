package com.eurodyn.qlack.fuse.aaa.service;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.eurodyn.qlack.fuse.aaa.dto.GroupDTO;
import com.eurodyn.qlack.fuse.aaa.exception.InvalidGroupHierarchyException;

/**
 * @author European Dynamics SA
 */
public interface UserGroupService {

    String createGroup(GroupDTO groupDTO);

    void updateGroup(GroupDTO groupDTO);

    void deleteGroup(String groupID);

    void moveGroup(String groupID, String newParentId)
        throws InvalidGroupHierarchyException;

    GroupDTO getGroupByID(String groupID, boolean lazyRelatives);

    List<GroupDTO> getGroupsByID(Collection<String> groupIds, boolean lazyRelatives);

    GroupDTO getGroupByName(String groupName, boolean lazyRelatives);

    List<GroupDTO> getGroupByNames(List<String> groupNames, boolean lazyRelatives);

    GroupDTO getGroupByObjectId(String objectId, boolean lazyRelatives);

    List<GroupDTO> listGroups();

    List<GroupDTO> listGroupsAsTree();

    GroupDTO getGroupParent(String groupID);

    List<GroupDTO> getGroupChildren(String groupID);

    void addUser(String userID, String groupId);

    void addUsers(Collection<String> userIDs, String groupID);

    void addUserByGroupName(String userId, String groupName);

    void addUsersByGroupName(Collection<String> userIDs, String groupName);

    void removeUser(String userID, String groupID);

    void removeUsers(Collection<String> userIDs, String groupID);

    Set<String> getGroupUsersIds(String groupID, boolean includeChildren);

    Set<String> getUserGroupsIds(String userID);
}
