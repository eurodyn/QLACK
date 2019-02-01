package com.eurodyn.qlack.fuse.aaa.service;

import com.eurodyn.qlack.fuse.aaa.criteria.UserSearchCriteria;
import com.eurodyn.qlack.fuse.aaa.criteria.UserSearchCriteria.UserAttributeCriteria;
import com.eurodyn.qlack.fuse.aaa.criteria.UserSearchCriteria.UserAttributeCriteria.Type;
import com.eurodyn.qlack.fuse.aaa.dto.SessionDTO;
import com.eurodyn.qlack.fuse.aaa.dto.UserAttributeDTO;
import com.eurodyn.qlack.fuse.aaa.dto.UserDTO;
import com.eurodyn.qlack.fuse.aaa.mappers.SessionMapper;
import com.eurodyn.qlack.fuse.aaa.mappers.UserAttributeMapper;
import com.eurodyn.qlack.fuse.aaa.mappers.UserMapper;
import com.eurodyn.qlack.fuse.aaa.model.UserGroup;
import com.eurodyn.qlack.fuse.aaa.model.QSession;
import com.eurodyn.qlack.fuse.aaa.model.QUser;
import com.eurodyn.qlack.fuse.aaa.model.QUserAttribute;
import com.eurodyn.qlack.fuse.aaa.model.Session;
import com.eurodyn.qlack.fuse.aaa.model.User;
import com.eurodyn.qlack.fuse.aaa.model.UserAttribute;
import com.eurodyn.qlack.fuse.aaa.repository.UserGroupRepository;
import com.eurodyn.qlack.fuse.aaa.repository.SessionRepository;
import com.eurodyn.qlack.fuse.aaa.repository.UserAttributeRepository;
import com.eurodyn.qlack.fuse.aaa.repository.UserRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
@Validated
@Transactional
public class UserService {

  private static final Logger LOGGER = Logger.getLogger(UserService.class.getName());
  private static final int saltLength = 16;

  // Service REFs
  private AccountingService accountingService;
  private LdapUserUtil ldapUserUtil;
  // Repositories
  private final UserRepository userRepository;
  private final UserAttributeRepository userAttributeRepository;
  private final SessionRepository sessionRepository;
  private final UserGroupRepository userGroupRepository;
  // Mappers
  private final UserMapper userMapper;
  private final SessionMapper sessionMapper;
  private final UserAttributeMapper userAttributeMapper;

  //QueryDSL helpers
  private QUser qUser = QUser.user;
  private QSession qSession = QSession.session;

  public UserService(AccountingService accountingService, LdapUserUtil ldapUserUtil,
                     UserRepository userRepository, UserAttributeRepository userAttributeRepository,
                     SessionRepository sessionRepository, UserGroupRepository userGroupRepository, UserMapper userMapper,
                     SessionMapper sessionMapper, UserAttributeMapper userAttributeMapper) {
    this.accountingService = accountingService;
    this.ldapUserUtil = ldapUserUtil;
    this.userRepository = userRepository;
    this.userAttributeRepository = userAttributeRepository;
    this.sessionRepository = sessionRepository;
    this.userGroupRepository = userGroupRepository;
    this.userMapper = userMapper;
    this.sessionMapper = sessionMapper;
    this.userAttributeMapper = userAttributeMapper;
  }

  public String createUser(UserDTO dto) {
    User user = userMapper.mapToEntity(dto);
    // Generate salt and hash password
    user.setSalt(RandomStringUtils.randomAlphanumeric(saltLength));
    String password = user.getSalt() + dto.getPassword();
    user.setPassword(DigestUtils.md5Hex(password));
    userRepository.save(user);
    for(UserAttribute attribute: user.getUserAttributes()){
      attribute.setUser(user);
      userAttributeRepository.save(attribute);
    }

    return user.getId();
  }

  public void updateUser(UserDTO dto, boolean updatePassword, boolean createIfMissing) {
    User user = userRepository.fetchById(dto.getId());
    if (updatePassword) {

      user.setSalt(RandomStringUtils.randomAlphanumeric(saltLength));
      String password = user.getSalt() + dto.getPassword();
      user.setPassword(DigestUtils.md5Hex(password));
    }
    userMapper.mapToExistingEntity(dto, user);

    if (dto.getUserAttributes() != null) {
      for (UserAttributeDTO attribute : dto.getUserAttributes()) {
        updateAttribute(attribute, createIfMissing);
      }
    }
  }

  public void deleteUser(String userID) {
    User user = userRepository.fetchById(userID);
    userRepository.delete(user);
  }

  public UserDTO getUserById(String userID) {
    User user = userRepository.fetchById(userID);

    return userMapper.mapToDTO(user);
  }

  public Set<UserDTO> getUsersById(Collection<String> userIDs) {
    Predicate predicate = qUser.id.in(userIDs);

    return userRepository.findAll(predicate).stream()
        .map(userMapper::mapToDTO)
        .collect(Collectors.toSet());
  }

  public Map<String, UserDTO> getUsersByIdAsHash(Collection<String> userIDs) {
    Map<String, UserDTO> retVal = new HashMap<>();
    Predicate predicate = qUser.id.in(userIDs);

    return userRepository.findAll(predicate).stream()
        .map(userMapper::mapToDTO)
        .collect(Collectors.toMap(UserDTO::getId,dto -> dto));
  }

  public UserDTO getUserByName(String userName) {
    return userMapper.mapToDTO(userRepository.findByUsername(userName));
  }

  public void updateUserStatus(String userID, byte status) {
    User user = userRepository.fetchById(userID);
    user.setStatus(status);
  }

  public byte getUserStatus(String userID) {
    User user = userRepository.fetchById(userID);
    return user.getStatus();
  }

  public boolean isSuperadmin(String userID) {
    User user = userRepository.fetchById(userID);
    if (user != null) {
      return user.isSuperadmin();
    } else {
      return false;
    }
  }

  public boolean isExternal(String userID) {
    User user = userRepository.fetchById(userID);
    return user.getExternal() != null && user.getExternal();
  }

  public String canAuthenticate(String username, String password) {
    String retVal = null;

    /** Try to find this user in the database */
    User user = userRepository.findByUsername(username);

    /** If the user was found proceed trying to authenticate it. Otherwise, if LDAP integration is
     * enabled try to authenticate the user in LDAP. Note that if the user is successfully
     * authenticated with LDAP, a new user will also be created/duplicated in AAA as an external
     * user.
     */
    if (user != null && BooleanUtils.isFalse(user.getExternal())) {
      /** Generate password hash to compare with password stored in the DB. */
      String checkPassword = DigestUtils.md5Hex(user.getSalt() + password);
      if (checkPassword.equals(user.getPassword())) {
        retVal = user.getId();
      }
    } else {
      if (ldapUserUtil.isLdapEnable()) {
        retVal = ldapUserUtil.canAuthenticate(username, password);
      }
    }

    return retVal;
  }

  public UserDTO login(String userID, String applicationSessionID,
      boolean terminateOtherSessions) {
    User user = userRepository.fetchById(userID);

    // Check if other sessions of this user need to be terminated first.
    if (terminateOtherSessions) {
      if (user.getSessions() != null) {
        for (Session session : user.getSessions()) {
          if (session.getTerminatedOn() == null) {
            accountingService.terminateSession(session.getId());
          }
        }
      }
    }

    // Create a new session for the user.
    SessionDTO session = new SessionDTO();
    session.setUserId(user.getId());
    session.setApplicationSessionID(applicationSessionID);
    String sessionId = accountingService.createSession(session);

    // Create a DTO representation of the user and populate the session Id of the session that was
    // just created.
    final UserDTO userDTO = userMapper.mapToDTO(user);
    userDTO.setSessionId(sessionId);

    return userDTO;
  }

  public void logout(String userID, String applicationSessionID) {
    User user = userRepository.fetchById(userID);

    if (user.getSessions() != null) {
      for (Session session : user.getSessions()) {
        if (((applicationSessionID != null) && (session
            .getApplicationSessionId().equals(applicationSessionID)))
            || ((applicationSessionID == null) && (session
            .getApplicationSessionId() == null))) {
          accountingService.terminateSession(session.getId());
        }
      }
    }
  }

  public void logoutAll() {
    Predicate predicate = qSession.terminatedOn.isNull();
    List<Session> queryResult = sessionRepository.findAll(predicate);
    if (queryResult != null) {
      for (Session session : queryResult) {
        logout(session.getUser().getId(),
            session.getApplicationSessionId());
      }
    }
  }

  public List<SessionDTO> isUserAlreadyLoggedIn(String userID) {
    Predicate predicate = qSession.user.id.eq(userID).and(qSession.terminatedOn.isNull());
    List<SessionDTO> retVal = sessionMapper.mapToDTO(sessionRepository
        .findAll(predicate, Sort.by("createdOn").ascending()));

    return retVal.isEmpty() ? null : retVal;
  }

  public String updatePassword(String username, String newPassword) {
    User user = userRepository.findByUsername(username);
    user.setSalt(RandomStringUtils.randomAlphanumeric(saltLength));
    if (StringUtils.isBlank(newPassword)) {
      newPassword = RandomStringUtils.randomAlphanumeric(8);
    }
    user.setPassword(DigestUtils.md5Hex(user.getSalt() + newPassword));
    return newPassword;
  }

  public boolean updatePassword(String username, String oldPassword, String newPassword) {
    boolean passwordUpdated = false;
    if (StringUtils.isNotBlank(canAuthenticate(username, oldPassword))) {
      updatePassword(username, newPassword);
      passwordUpdated = true;
    }

    return passwordUpdated;
  }

  public boolean belongsToGroupByName(String userID, String groupName,
      boolean includeChildren) {
    User user = userRepository.fetchById(userID);
    UserGroup userGroup = userGroupRepository.findByName(groupName);
    boolean retVal = userGroup.getUsers().contains(user);

    if (!retVal && includeChildren) {
      for (UserGroup child : userGroup.getChildren()) {
        if (belongsToGroupByName(userID, child.getName(),
            includeChildren)) {
          return true;
        }
      }
    }

    return retVal;
  }

  public void updateAttributes(Collection<UserAttributeDTO> attributes,
      boolean createIfMissing) {
    for (UserAttributeDTO attributeDTO : attributes) {
      updateAttribute(attributeDTO, createIfMissing);
    }
  }

  public void updateAttribute(UserAttributeDTO attributeDTO,
      boolean createIfMissing) {
    String userId = attributeDTO.getUserId();
    String name = attributeDTO.getName();

    UserAttribute attribute = userAttributeRepository.findByUserIdAndName(userId, name);
    if (attribute != null) {
      mapAttribute(attribute, attributeDTO);
      userAttributeRepository.save(attribute);
    } else {
      if (createIfMissing) {
        attribute = new UserAttribute();
        mapAttribute(attribute, attributeDTO);
        userAttributeRepository.save(attribute);
      } else {
        return;
      }
    }
  }

  private void mapAttribute(UserAttribute attribute,
      UserAttributeDTO attributeDTO) {
    String userId = attributeDTO.getUserId();
    User user = userRepository.fetchById(userId);
    attribute.setUser(user);
    userAttributeMapper.mapToExistingEntity(attributeDTO, attribute);
  }

  public void deleteAttribute(String userID, String attributeName) {
    UserAttribute attribute = userAttributeRepository.findByUserIdAndName(userID, attributeName);
    if (attribute != null) {
      userAttributeRepository.delete(attribute);
    }
  }

  public UserAttributeDTO getAttribute(String userID, String attributeName) {
    UserAttribute attribute = userAttributeRepository.findByUserIdAndName(userID, attributeName);
    return userAttributeMapper.mapToDTO(attribute);
  }

  public Set<String> getUserIDsForAttribute(Collection<String> userIDs,
      String attributeName, String attributeValue) {
    Predicate predicate = qUser.userAttributes.any().name.eq(attributeName)
        .and(qUser.userAttributes.any().data.eq(attributeValue));
    if ((userIDs != null) && (!userIDs.isEmpty())) {
      predicate = ((BooleanExpression) predicate).and(qUser.id.in(userIDs));
    }

    return userRepository.findAll(predicate).stream()
        .map(User::getId)
        .collect(Collectors.toSet());
  }

  public Iterable<UserDTO> findUsers(UserSearchCriteria criteria){
    Predicate predicate = buildPredicate(criteria);
    if(criteria.getAttributeCriteria() != null){
      predicate = getAttributePredicate(criteria.getAttributeCriteria(), predicate);
    }
    if(criteria.getPageable() != null){

      return listUsersPaginated(predicate, criteria.getPageable());
    }else{

      return listUsers(predicate);
    }
  }

  //TODO fix attribute sorting
  private Predicate getAttributePredicate(UserAttributeCriteria criteria, Predicate predicate){
    if (criteria.getAttCriteria()!= null) {
      for(UserAttributeCriteria uac: criteria.getAttCriteria()){
        if (uac.getType() == Type.AND) {
//          predicate = ((BooleanExpression)predicate).and(qUser.userAttributes.any().eq())
        }
      }
    }

    return predicate;
  }

  private List<UserDTO> listUsers(Predicate predicate) {

    return userRepository.findAll(predicate).stream()
        .map(userMapper::mapToDTO)
        .collect(Collectors.toList());
  }

  private Predicate buildPredicate(UserSearchCriteria criteria){
    Predicate predicate = new BooleanBuilder();
    if(criteria.getIncludeGroupIds() != null){
      predicate = ((BooleanBuilder) predicate).and(qUser.userGroups.any().id.in(criteria.getIncludeGroupIds()));
    }
    if(criteria.getExcludeGroupIds() != null){
      predicate = ((BooleanBuilder) predicate).and(qUser.userGroups.any().id.notIn(criteria.getExcludeGroupIds()));
    }
    if(criteria.getIncludeIds() != null){
      predicate = ((BooleanBuilder) predicate).and(qUser.id.in(criteria.getIncludeIds()));
    }
    if(criteria.getExcludeIds() != null){
      predicate = ((BooleanBuilder) predicate).and(qUser.id.notIn(criteria.getExcludeIds()));
    }
    if(criteria.getIncludeStatuses() != null){
      predicate = ((BooleanBuilder) predicate).and(qUser.status.in(criteria.getIncludeStatuses()));
    }
    if(criteria.getExcludeStatuses() != null){
      predicate = ((BooleanBuilder) predicate).and(qUser.status.notIn(criteria.getExcludeStatuses()));
    }
    if(criteria.getUsername() != null){
      predicate = ((BooleanBuilder) predicate).and(qUser.username.eq(criteria.getUsername()));
    }
    if(criteria.getSuperadmin() != null){
      predicate = ((BooleanBuilder) predicate).and(qUser.superadmin.eq(criteria.getSuperadmin()));
    }

    return predicate;
  }

  private Page<UserDTO> listUsersPaginated(Predicate predicate, Pageable pageable){

    return userRepository.findAll(predicate, pageable).map(userMapper::mapToDTO);
  }

  public long findUserCount(UserSearchCriteria criteria) {
    Predicate predicate = buildPredicate(criteria);

    return userRepository.findAll(predicate).size();
  }

  public boolean isAttributeValueUnique(String attributeValue,
      String attributeName, String userID) {

    boolean isAttributeValueUnique = false;
    QUserAttribute quserAttribute = QUserAttribute.userAttribute;

    Predicate predicate = quserAttribute.name.eq(attributeName)
        .and(quserAttribute.data.eq(attributeName));
    // convert Set to List
    List<UserAttributeDTO> qResult = userAttributeMapper.mapToDTO(userAttributeRepository.findAll(predicate));
    ArrayList<UserAttributeDTO> list = new ArrayList<UserAttributeDTO>(qResult);
    //in case of no user exists with this user attribute value	or there is only the given user
    if ((list.size() == 1 && list.get(0).getUserId().equals(userID)) || (list.isEmpty())) {
      isAttributeValueUnique = true;
    }
    return isAttributeValueUnique;
  }

  public Page<UserDTO> findAll(Predicate predicate, Pageable pageable) {
    return userMapper.map(userRepository.findAll(predicate, pageable));
  }
}
