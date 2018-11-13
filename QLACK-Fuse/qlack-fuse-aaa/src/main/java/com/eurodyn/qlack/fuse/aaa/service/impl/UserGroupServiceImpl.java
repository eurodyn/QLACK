package com.eurodyn.qlack.fuse.aaa.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.eurodyn.qlack.fuse.aaa.dto.GroupDTO;
import com.eurodyn.qlack.fuse.aaa.exception.InvalidGroupHierarchyException;
import com.eurodyn.qlack.fuse.aaa.mappers.GroupMapper;
import com.eurodyn.qlack.fuse.aaa.model.Group;
import com.eurodyn.qlack.fuse.aaa.model.QGroup;
import com.eurodyn.qlack.fuse.aaa.model.User;
import com.eurodyn.qlack.fuse.aaa.repository.GroupRepository;
import com.eurodyn.qlack.fuse.aaa.repository.UserRepository;
import com.eurodyn.qlack.fuse.aaa.service.UserGroupService;
import com.querydsl.core.types.Predicate;

/**
 * @author European Dynamics SA
 */
@Service
@Validated
@Transactional
public class UserGroupServiceImpl implements UserGroupService {

    // QueryDSL helpers.
    private static QGroup qGroup = QGroup.group;
    // Repositories
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    // Mappers
    private final GroupMapper groupMapper;

    public UserGroupServiceImpl(GroupRepository groupRepository,
        UserRepository userRepository, GroupMapper groupMapper) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
        this.groupMapper = groupMapper;
    }

    public String createGroup(GroupDTO groupDTO) {
        Group group = groupMapper.mapToEntity(groupDTO);
        if (groupDTO.getParentId() != null) {
            group.setParent(groupRepository.fetchById(groupDTO.getParentId()));
        }
        groupRepository.save(group);

        return group.getId();
    }

    public void updateGroup(GroupDTO groupDTO) {
        Group group = groupRepository.fetchById(groupDTO.getId());
        groupMapper.mapToExistingEntity(groupDTO, group);
    }

    public void deleteGroup(String groupID) {
        groupRepository.delete(groupRepository.fetchById(groupID));
    }

    public void moveGroup(String groupID, String newParentId)
        throws InvalidGroupHierarchyException {
        Group group = groupRepository.fetchById(groupID);
        Group newParent = groupRepository.fetchById(newParentId);

        // Check the moving the group under the new parent will not
        // create a cyclic dependency.
        Group checkedGroup = newParent;
        while (checkedGroup != null) {
            if (checkedGroup.getId().equals(group.getId())) {
                throw new InvalidGroupHierarchyException("Cannot move group with ID " + groupID
                    + " under group with ID " + newParentId
                    + " since this will create a cyclic dependency between groups.");
            }
            checkedGroup = checkedGroup.getParent();
        }

        group.setParent(newParent);
    }

    public GroupDTO getGroupByID(String groupID, boolean lazyRelatives) {
        return groupMapper.mapToDTO(groupRepository.fetchById(groupID));
    }

    public List<GroupDTO> getGroupsByID(Collection<String> groupIds, boolean lazyRelatives) {
        Predicate predicate = qGroup.id.in(groupIds);

        return groupMapper.mapToDTO(
            groupRepository.findAll(predicate, Sort.by("name").ascending()));

    }

    public GroupDTO getGroupByName(String groupName, boolean lazyRelatives) {
        return groupMapper.mapToDTO(
            groupRepository.findByName(groupName));
    }

    public List<GroupDTO> getGroupByNames(List<String> groupNames, boolean lazyRelatives) {
        Predicate predicate = qGroup.name.in(groupNames);

        return groupMapper.mapToDTO(groupRepository.findAll(predicate));
    }

    public GroupDTO getGroupByObjectId(String objectId, boolean lazyRelatives) {
        return groupMapper.mapToDTO(
            groupRepository.findByObjectId(objectId));
    }

    public List<GroupDTO> listGroups() {
        return groupMapper.mapToDTO(
            groupRepository.findAll(Sort.by("name").ascending()));
    }

    public List<GroupDTO> listGroupsAsTree() {
        Predicate predicate = qGroup.parent.isNull();

        return groupMapper.mapToDTO(groupRepository.findAll(
            predicate, Sort.by("name").ascending()));
    }

    public GroupDTO getGroupParent(String groupID) {
        Group group = groupRepository.fetchById(groupID);
        return groupMapper.mapToDTO(group.getParent());
    }

    public List<GroupDTO> getGroupChildren(String groupID) {
        Predicate predicate;
        if (groupID == null) {
            predicate = qGroup.parent.isNull();
        } else {
            predicate = qGroup.parent.id.eq(groupID);
        }
        return groupMapper.mapToDTO(groupRepository.findAll(
            predicate, Sort.by("name").ascending()));

    }

    /**
     * Returns the users belonging to a given group and (optionally) its hierarchy
     *
     * @param group The group the users of which to retrieve
     * @param includeAncestors true if users belonging to ancestors of this group (the group's parent and its parent's parent, etc.) should
     * be retrieved
     * @param includeDescendants true if users belonging to descendants of this group (the group's children and its children's children,
     * etc.) should be retrieved
     * @return The IDs of the users belonging to the specified group hierarchy.
     */
    private Set<String> getGroupHierarchyUsersIds(Group group, boolean includeAncestors,
        boolean includeDescendants) {
        Set<String> retVal = new HashSet<>(group.getUsers().size());
        for (User user : group.getUsers()) {
            retVal.add(user.getId());
        }

        // If children group users should be included iterate over them
        // (and their children recursively) and add their users to
        // the return value. Same for the group parents.
        if (includeDescendants) {
            for (Group child : group.getChildren()) {
                retVal.addAll(getGroupHierarchyUsersIds(child, false, true));
            }
        }
        if ((includeAncestors) && (group.getParent() != null)) {
            retVal.addAll(getGroupHierarchyUsersIds(group.getParent(), true, false));
        }

        return retVal;
    }

    private void addUsers(Collection<String> userIDs, Group group) {
        for (String userID : userIDs) {
            User user = userRepository.fetchById(userID);
            // TODO ask how else we do it
            if (group.getUsers() == null) {
                group.setUsers(new ArrayList<User>());
            }
            group.getUsers().add(user);
            if (user.getGroups() == null) {
                user.setGroups(new ArrayList<Group>());
            }
            user.getGroups().add(group);
        }
    }

    public void addUser(String userID, String groupId) {
        List<String> userIds = new ArrayList<>(1);
        userIds.add(userID);
        addUsers(userIds, groupId);
    }

    public void addUsers(Collection<String> userIDs, String groupID) {
        addUsers(userIDs, groupRepository.fetchById(groupID));
    }

    public void addUserByGroupName(String userId, String groupName) {
        List<String> userIds = new ArrayList<>(1);
        userIds.add(userId);
        addUsersByGroupName(userIds, groupName);
    }

    public void addUsersByGroupName(Collection<String> userIDs, String groupName) {
        addUsers(userIDs, groupRepository.findByName(groupName));
    }

    public void removeUser(String userID, String groupID) {
        List<String> userIds = new ArrayList<>(1);
        userIds.add(userID);
        removeUsers(userIds, groupID);
    }

    public void removeUsers(Collection<String> userIDs, String groupID) {
        Group group = groupRepository.fetchById(groupID);
        for (String userID : userIDs) {
            User user = userRepository.fetchById(userID);
            group.getUsers().remove(user);
        }
    }

    public Set<String> getGroupUsersIds(String groupID, boolean includeChildren) {
        Group group = groupRepository.fetchById(groupID);
        return getGroupHierarchyUsersIds(group, false, includeChildren);
    }

    public Set<String> getUserGroupsIds(String userID) {
        User user = userRepository.fetchById(userID);
        Set<String> retVal = new HashSet<>();
        if (user.getGroups() != null) {
            for (Group group : user.getGroups()) {
                retVal.add(group.getId());
            }
        }
        return retVal;
    }
}
