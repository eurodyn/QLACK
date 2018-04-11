package com.eurodyn.qlack.fuse.aaa.service;

import com.eurodyn.qlack.fuse.aaa.dto.QFAGroupDTO;
import com.eurodyn.qlack.fuse.aaa.exception.QFAInvalidGroupHierarchyException;
import com.eurodyn.qlack.fuse.aaa.model.QFAGroup;
import com.eurodyn.qlack.fuse.aaa.model.QFAUser;
import com.eurodyn.qlack.fuse.aaa.model.QQFAGroup;
import com.eurodyn.qlack.fuse.aaa.util.QFAConverterUtil;
import com.querydsl.jpa.impl.JPAQueryFactory;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author European Dynamics SA
 */
@Service
@Validated
@Transactional
public class QFAUserGroupService {

  // QueryDSL helpers.
  private static QQFAGroup qGroup = QQFAGroup.qFAGroup;
  @PersistenceContext
  private EntityManager em;

  public String createGroup(QFAGroupDTO groupDTO) {
    QFAGroup group = new QFAGroup();
    if (groupDTO.getId() != null) {
      group.setId(groupDTO.getId());
    }
    group.setName(groupDTO.getName());
    group.setDescription(groupDTO.getDescription());
    group.setObjectId(groupDTO.getObjectID());
    if (groupDTO.getParent() != null) {
      group.setParent(QFAGroup.find(groupDTO.getParent().getId(), em));
    }
    em.persist(group);

    return group.getId();
  }

  public void updateGroup(QFAGroupDTO groupDTO) {
    QFAGroup group = QFAGroup.find(groupDTO.getId(), em);
    group.setName(groupDTO.getName());
    group.setDescription(groupDTO.getDescription());
    group.setObjectId(groupDTO.getObjectID());
  }

  public void deleteGroup(String groupID) {
    em.remove(QFAGroup.find(groupID, em));
  }

  public void moveGroup(String groupID, String newParentId)
      throws QFAInvalidGroupHierarchyException {
    QFAGroup group = QFAGroup.find(groupID, em);
    QFAGroup newParent = QFAGroup.find(newParentId, em);

    // Check the moving the group under the new parent will not
    // create a cyclic dependency.
    QFAGroup checkedGroup = newParent;
    while (checkedGroup != null) {
      if (checkedGroup.getId().equals(group.getId())) {
        throw new QFAInvalidGroupHierarchyException("Cannot move group with ID " + groupID
            + " under group with ID " + newParentId
            + " since this will create a cyclic dependency between groups.");
      }
      checkedGroup = checkedGroup.getParent();
    }

    group.setParent(newParent);
  }

  public QFAGroupDTO getGroupByID(String groupID, boolean lazyRelatives) {
    return QFAConverterUtil.groupToGroupDTO(QFAGroup.find(groupID, em), lazyRelatives);
  }

  public List<QFAGroupDTO> getGroupsByID(Collection<String> groupIds, boolean lazyRelatives) {
    Query query = em
        .createQuery("SELECT g FROM Group g WHERE g.id in (:groupIds) ORDER BY g.name ASC");
    query.setParameter("groupIds", groupIds);
    return QFAConverterUtil.groupToGroupDTOList(query.getResultList(), lazyRelatives);
  }

  public QFAGroupDTO getGroupByName(String groupName, boolean lazyRelatives) {
    return QFAConverterUtil.groupToGroupDTO(
        QFAGroup.findByName(groupName, em), lazyRelatives);
  }

  public List<QFAGroupDTO> getGroupByNames(List<String> groupNames, boolean lazyRelatives) {
    final List<QFAGroup> groups = new JPAQueryFactory(em)
        .selectFrom(qGroup)
        .where(qGroup.name.in(groupNames)).fetch();

    return QFAConverterUtil.groupToGroupDTOList(groups, lazyRelatives);
  }

  public QFAGroupDTO getGroupByObjectId(String objectId, boolean lazyRelatives) {
    return QFAConverterUtil.groupToGroupDTO(
        QFAGroup.findByObjectId(objectId, em), lazyRelatives);
  }

  public List<QFAGroupDTO> listGroups() {
    Query query = em.createQuery("SELECT g FROM QFAGroup g ORDER BY g.name ASC");
    return QFAConverterUtil.groupToGroupDTOList(
        query.getResultList(), true);
  }

  public List<QFAGroupDTO> listGroupsAsTree() {
    Query query = em
        .createQuery("SELECT g FROM QFAGroup g WHERE g.parent IS NULL ORDER BY g.name ASC");
    return QFAConverterUtil.groupToGroupDTOList(
        query.getResultList(), false);
  }

  public QFAGroupDTO getGroupParent(String groupID) {
    QFAGroup group = QFAGroup.find(groupID, em);
    return QFAConverterUtil.groupToGroupDTO(group.getParent(), true);
  }

  public List<QFAGroupDTO> getGroupChildren(String groupID) {
    Query query = null;
    if (groupID == null) {
      query = em.createQuery("SELECT g FROM QFAGroup g WHERE g.parent IS NULL ORDER BY g.name ASC");
    } else {
      query = em
          .createQuery("SELECT g FROM QFAGroup g WHERE g.parent.id = :parentId ORDER BY g.name ASC");
      query.setParameter("parentId", groupID);
    }
    return QFAConverterUtil.groupToGroupDTOList(
        query.getResultList(), true);

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
  private Set<String> getGroupHierarchyUsersIds(QFAGroup group, boolean includeAncestors,
      boolean includeDescendants) {
    Set<String> retVal = new HashSet<>(group.getUsers().size());
    for (QFAUser user : group.getUsers()) {
      retVal.add(user.getId());
    }

    // If children group users should be included iterate over them
    // (and their children recursively) and add their users to
    // the return value. Same for the group parents.
    if (includeDescendants) {
      for (QFAGroup child : group.getChildren()) {
        retVal.addAll(getGroupHierarchyUsersIds(child, false, true));
      }
    }
    if ((includeAncestors) && (group.getParent() != null)) {
      retVal.addAll(getGroupHierarchyUsersIds(group.getParent(), true, false));
    }

    return retVal;
  }

  private void addUsers(Collection<String> userIDs, QFAGroup group) {
    for (String userID : userIDs) {
      QFAUser user = QFAUser.find(userID, em);
      // TODO ask how else we do it
      if (group.getUsers() == null) {
        group.setUsers(new ArrayList<QFAUser>());
      }
      group.getUsers().add(user);
      if (user.getGroups() == null) {
        user.setGroups(new ArrayList<QFAGroup>());
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
    addUsers(userIDs, QFAGroup.find(groupID, em));

  }

  public void addUserByGroupName(String userId, String groupName) {
    List<String> userIds = new ArrayList<>(1);
    userIds.add(userId);
    addUsersByGroupName(userIds, groupName);
  }

  public void addUsersByGroupName(Collection<String> userIDs, String groupName) {
    addUsers(userIDs, QFAGroup.findByName(groupName, em));
  }

  public void removeUser(String userID, String groupID) {
    List<String> userIds = new ArrayList<>(1);
    userIds.add(userID);
    removeUsers(userIds, groupID);
  }

  public void removeUsers(Collection<String> userIDs, String groupID) {
    QFAGroup group = QFAGroup.find(groupID, em);
    for (String userID : userIDs) {
      QFAUser user = QFAUser.find(userID, em);
      group.getUsers().remove(user);
    }
  }

  public Set<String> getGroupUsersIds(String groupID, boolean includeChildren) {
    QFAGroup group = QFAGroup.find(groupID, em);
    return getGroupHierarchyUsersIds(group, false, includeChildren);
  }

  public Set<String> getUserGroupsIds(String userID) {
    QFAUser user = QFAUser.find(userID, em);
    Set<String> retVal = new HashSet<>();
    if (user.getGroups() != null) {
      for (QFAGroup group : user.getGroups()) {
        retVal.add(group.getId());
      }
    }
    return retVal;
  }
}
