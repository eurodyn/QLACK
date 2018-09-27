package com.eurodyn.qlack.fuse.aaa.service;

import com.eurodyn.qlack.fuse.aaa.dto.GroupDTO;
import com.eurodyn.qlack.fuse.aaa.exception.InvalidGroupHierarchyException;
import com.eurodyn.qlack.fuse.aaa.mappers.GroupMapper;
import com.eurodyn.qlack.fuse.aaa.model.Group;
import com.eurodyn.qlack.fuse.aaa.model.QGroup;
import com.eurodyn.qlack.fuse.aaa.model.User;
import com.eurodyn.qlack.fuse.aaa.repository.GroupRepository;
import com.eurodyn.qlack.fuse.aaa.repository.UserRepository;
import com.querydsl.core.types.Predicate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

/**
 * @author European Dynamics SA
 */
@Service
@Validated
@Transactional
public class UserGroupService {

  // QueryDSL helpers.
  private static QGroup qGroup = QGroup.group;
//  @PersistenceContext
//  private EntityManager em;

  private final GroupRepository groupRepository;

  private final UserRepository userRepository;

  private final GroupMapper groupMapper;

  public UserGroupService(GroupRepository groupRepository,
      UserRepository userRepository, GroupMapper groupMapper) {
    this.groupRepository = groupRepository;
    this.userRepository = userRepository;
    this.groupMapper = groupMapper;
  }

  public String createGroup(GroupDTO groupDTO) {
//    Group group = new Group();
//    if (groupDTO.getId() != null) {
//      group.setId(groupDTO.getId());
//    }
//    group.setName(groupDTO.getName());
//    group.setDescription(groupDTO.getDescription());
//    group.setObjectId(groupDTO.getObjectID());
    Group group = groupMapper.mapToEntity(groupDTO);
    if (groupDTO.getParentId() != null) {
//      group.setParent(Group.find(groupDTO.getParent().getId(), em));
      group.setParent(groupRepository.fetchById(groupDTO.getParentId()));
    }
//    em.persist(group);
    groupRepository.save(group);

    return group.getId();
  }

  public void updateGroup(GroupDTO groupDTO) {
//    Group group = Group.find(groupDTO.getId(), em);
    Group group = groupRepository.fetchById(groupDTO.getId());
    group.setName(groupDTO.getName());
    group.setDescription(groupDTO.getDescription());
    group.setObjectId(groupDTO.getObjectID());
  }

  public void deleteGroup(String groupID) {
//    em.remove(Group.find(groupID, em));
    groupRepository.delete(groupRepository.fetchById(groupID));
  }

  public void moveGroup(String groupID, String newParentId)
      throws InvalidGroupHierarchyException {
//    Group group = Group.find(groupID, em);
    Group group = groupRepository.fetchById(groupID);
//    Group newParent = Group.find(newParentId, em);
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
//    return ConverterUtil.groupToGroupDTO(Group.find(groupID, em), lazyRelatives);
    return groupMapper.mapToDTO(groupRepository.fetchById(groupID));
  }

  public List<GroupDTO> getGroupsByID(Collection<String> groupIds, boolean lazyRelatives) {
//    Query query = em
//        .createQuery("SELECT g FROM com.eurodyn.qlack.fuse.aaa.model.Group g WHERE g.id in (:groupIds) ORDER BY g.name ASC");
//    query.setParameter("groupIds", groupIds);
//    return ConverterUtil.groupToGroupDTOList(query.getResultList(), lazyRelatives);
    Predicate predicate = qGroup.id.in(groupIds);

    return groupMapper.mapToDTO(
        groupRepository.findAll(predicate, Sort.by("name").ascending()));

  }

  public GroupDTO getGroupByName(String groupName, boolean lazyRelatives) {
//    return ConverterUtil.groupToGroupDTO(
//        Group.findByName(groupName, em), lazyRelatives);
    return groupMapper.mapToDTO(
        groupRepository.findByName(groupName));
  }

  public List<GroupDTO> getGroupByNames(List<String> groupNames, boolean lazyRelatives) {
//    final List<Group> groups = new JPAQueryFactory(em)
//        .selectFrom(qGroup)
//        .where(qGroup.name.in(groupNames)).fetch();
    Predicate predicate = qGroup.name.in(groupNames);

    return groupMapper.mapToDTO(groupRepository.findAll(predicate));
  }

  public GroupDTO getGroupByObjectId(String objectId, boolean lazyRelatives) {
//    return ConverterUtil.groupToGroupDTO(
//        Group.findByObjectId(objectId, em), lazyRelatives);
    return groupMapper.mapToDTO(
        groupRepository.findByObjectId(objectId));
  }

  public List<GroupDTO> listGroups() {
//    Query query = em.createQuery("SELECT g FROM com.eurodyn.qlack.fuse.aaa.model.Group g ORDER BY g.name ASC");
//    return ConverterUtil.groupToGroupDTOList(
//        query.getResultList(), true);
    return groupMapper.mapToDTO(
        groupRepository.findAll(Sort.by("name").ascending()));
  }

  public List<GroupDTO> listGroupsAsTree() {
//    Query query = em
//        .createQuery("SELECT g FROM com.eurodyn.qlack.fuse.aaa.model.Group g WHERE g.parent IS NULL ORDER BY g.name ASC");
//    return ConverterUtil.groupToGroupDTOList(
//        query.getResultList(), false);
    Predicate predicate = qGroup.parent.isNull();

    return groupMapper.mapToDTO(groupRepository.findAll(
        predicate, Sort.by("name").ascending()));
  }

  public GroupDTO getGroupParent(String groupID) {
//    Group group = Group.find(groupID, em);
    Group group = groupRepository.fetchById(groupID);
    return groupMapper.mapToDTO(group.getParent());
  }

  public List<GroupDTO> getGroupChildren(String groupID) {
//    Query query = null;
    Predicate predicate;
    if (groupID == null) {
//      query = em.createQuery("SELECT g FROM com.eurodyn.qlack.fuse.aaa.model.Group g WHERE g.parent IS NULL ORDER BY g.name ASC");
      predicate = qGroup.parent.isNull();
    } else {
//      query = em
//          .createQuery(
//              "SELECT g FROM com.eurodyn.qlack.fuse.aaa.model.Group g WHERE g.parent.id = :parentId ORDER BY g.name ASC");
//      query.setParameter("parentId", groupID);
      predicate = qGroup.parent.id.eq(groupID);
    }
//    return ConverterUtil.groupToGroupDTOList(
//        query.getResultList(), true);
    return groupMapper.mapToDTO(groupRepository.findAll(
        predicate, Sort.by("name").ascending()));

  }

  /**
   * Returns the users belonging to a given group and (optionally) its hierarchy
   *
   * @param group The group the users of which to retrieve
   * @param includeAncestors true if users belonging to ancestors of this group
   * (the group's parent and its parent's parent, etc.) should be retrieved
   * @param includeDescendants true if users belonging to descendants of this
   * group (the group's children and its children's children, etc.) should
   * be retrieved
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
//      User user = User.find(userID, em);
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
//    addUsers(userIDs, Group.find(groupID, em));
    addUsers(userIDs, groupRepository.fetchById(groupID));
  }

  public void addUserByGroupName(String userId, String groupName) {
    List<String> userIds = new ArrayList<>(1);
    userIds.add(userId);
    addUsersByGroupName(userIds, groupName);
  }

  public void addUsersByGroupName(Collection<String> userIDs, String groupName) {
//    addUsers(userIDs, Group.findByName(groupName, em));
    addUsers(userIDs, groupRepository.findByName(groupName));
  }

  public void removeUser(String userID, String groupID) {
    List<String> userIds = new ArrayList<>(1);
    userIds.add(userID);
    removeUsers(userIds, groupID);
  }

  public void removeUsers(Collection<String> userIDs, String groupID) {
//    Group group = Group.find(groupID, em);
    Group group = groupRepository.fetchById(groupID);
    for (String userID : userIDs) {
//      User user = User.find(userID, em);
      User user = userRepository.fetchById(userID);
      group.getUsers().remove(user);
    }
  }

  public Set<String> getGroupUsersIds(String groupID, boolean includeChildren) {
//    Group group = Group.find(groupID, em);
    Group group = groupRepository.fetchById(groupID);
    return getGroupHierarchyUsersIds(group, false, includeChildren);
  }

  public Set<String> getUserGroupsIds(String userID) {
//    User user = User.find(userID, em);
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
