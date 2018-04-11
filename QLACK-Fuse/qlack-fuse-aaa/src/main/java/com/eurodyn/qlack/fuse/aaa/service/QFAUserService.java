package com.eurodyn.qlack.fuse.aaa.service;

import com.eurodyn.qlack.fuse.aaa.criteria.QFAUserSearchCriteria;
import com.eurodyn.qlack.fuse.aaa.criteria.QFAUserSearchCriteria.UserAttributeCriteria;
import com.eurodyn.qlack.fuse.aaa.dto.QFASessionDTO;
import com.eurodyn.qlack.fuse.aaa.dto.QFAUserAttributeDTO;
import com.eurodyn.qlack.fuse.aaa.dto.QFAUserDTO;
import com.eurodyn.qlack.fuse.aaa.model.QFAGroup;
import com.eurodyn.qlack.fuse.aaa.model.QFASession;
import com.eurodyn.qlack.fuse.aaa.model.QFAUser;
import com.eurodyn.qlack.fuse.aaa.model.QFAUserAttribute;
import com.eurodyn.qlack.fuse.aaa.model.QQFAUserAttribute;
import com.eurodyn.qlack.fuse.aaa.util.QFAConverterUtil;
import com.eurodyn.qlack.fuse.aaa.util.QFAUserSearchHelper;
import com.querydsl.jpa.impl.JPAQueryFactory;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

@Service
@Validated
@Transactional
public class QFAUserService {

  private static final Logger LOGGER = Logger.getLogger(QFAUserService.class.getName());
  private static final int saltLength = 16;

  @PersistenceContext
  private EntityManager em;

  // Service REFs
  private QFAAccountingService accountingService;
  private QFALdapUserUtil ldapUserUtil;

  @Autowired
  public QFAUserService(QFAAccountingService accountingService, QFALdapUserUtil ldapUserUtil) {
    this.accountingService = accountingService;
    this.ldapUserUtil = ldapUserUtil;
  }

  public String createUser(QFAUserDTO dto) {
    QFAUser user = QFAConverterUtil.userDTOToUser(dto);

    // Generate salt and hash password
    user.setSalt(RandomStringUtils.randomAlphanumeric(saltLength));
    String password = user.getSalt() + dto.getPassword();
    user.setPassword(DigestUtils.md5Hex(password));

    em.persist(user);
    if (user.getUserAttributes() != null) {
      for (QFAUserAttribute attribute : user.getUserAttributes()) {
        em.persist(attribute);
      }
    }

    return user.getId();
  }

  public void updateUser(QFAUserDTO dto, boolean updatePassword) {
    QFAUser user = QFAUser.find(dto.getId(), em);
    user.setUsername(dto.getUsername());
    if (updatePassword) {
      user.setSalt(RandomStringUtils.randomAlphanumeric(saltLength));
      String password = user.getSalt() + dto.getPassword();
      user.setPassword(DigestUtils.md5Hex(password));
    }
    user.setStatus(dto.getStatus());
    user.setSuperadmin(dto.isSuperadmin());
    user.setExternal(dto.isExternal());

    if (dto.getUserAttributes() != null) {
      for (QFAUserAttributeDTO attribute : dto.getUserAttributes()) {
        updateAttribute(attribute, true);
      }
    }

    // flush persistence context so that we can load the user with the
    // updated attributes
    em.flush();
  }

  public void deleteUser(String userID) {
    QFAUser user = QFAUser.find(userID, em);
    em.remove(user);
  }

  public QFAUserDTO getUserById(String userID) {
    QFAUser user = QFAUser.find(userID, em);
    return QFAConverterUtil.userToUserDTO(user);
  }

  public Set<QFAUserDTO> getUsersById(Collection<String> userIDs) {
    Set<QFAUserDTO> retVal = new HashSet<>();
    Query query = em.createQuery("SELECT u FROM QFAUser u WHERE "
        + " u.id in (:userIds)");
    query.setParameter("userIds", userIDs);
    List<QFAUser> queryResult = query.getResultList();
    for (QFAUser user : queryResult) {
      retVal.add(QFAConverterUtil.userToUserDTO(user));
    }
    return retVal;
  }

  public Map<String, QFAUserDTO> getUsersByIdAsHash(Collection<String> userIDs) {
    Map<String, QFAUserDTO> retVal = new HashMap<>();
    Query query = em.createQuery("SELECT u FROM QFAUser u WHERE "
        + " u.id in (:userIds)");
    query.setParameter("userIds", userIDs);
    List<QFAUser> queryResult = query.getResultList();
    for (QFAUser user : queryResult) {
      retVal.put(user.getId(), QFAConverterUtil.userToUserDTO(user));
    }
    return retVal;
  }

  public QFAUserDTO getUserByName(String userName) {
    return QFAConverterUtil.userToUserDTO(QFAUser.findByUsername(userName, em));
  }

  public void updateUserStatus(String userID, byte status) {
    QFAUser user = QFAUser.find(userID, em);
    user.setStatus(status);
  }

  public byte getUserStatus(String userID) {
    QFAUser user = QFAUser.find(userID, em);
    return user.getStatus();
  }

  public boolean isSuperadmin(String userID) {
    QFAUser user = QFAUser.find(userID, em);
    if (user != null) {
      return user.isSuperadmin();
    } else {
      return false;
    }
  }

  public boolean isExternal(String userID) {
    QFAUser user = QFAUser.find(userID, em);
    return user.isExternal() != null && user.isExternal();
  }

  public String canAuthenticate(String username, String password) {
    String retVal = null;

    /** Try to find this user in the database */
    QFAUser user = QFAUser.findByUsername(username, em);

    /** If the user was found proceed trying to authenticate it. Otherwise, if LDAP integration is
     * enabled try to authenticate the user in LDAP. Note that if the user is successfully
     * authenticated with LDAP, a new user will also be created/duplicated in AAA as an external
     * user.
     */
    if (user != null && BooleanUtils.isFalse(user.isExternal())) {
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

  public QFAUserDTO login(String userID, String applicationSessionID,
      boolean terminateOtherSessions) {
    QFAUser user = QFAUser.find(userID, em);

    // Check if other sessions of this user need to be terminated first.
    if (terminateOtherSessions) {
      if (user.getSessions() != null) {
        for (QFASession session : user.getSessions()) {
          accountingService.terminateSession(session.getId());
        }
      }
    }

    // Create a new session for the user.
    QFASessionDTO session = new QFASessionDTO();
    session.setUserId(user.getId());
    session.setApplicationSessionID(applicationSessionID);
    String sessionId = accountingService.createSession(session);

    // Create a DTO representation of the user and populate the session Id of the session that was
    // just created.
    final QFAUserDTO userDTO = QFAConverterUtil.userToUserDTO(user);
    userDTO.setSessionId(sessionId);

    return userDTO;
  }

  public void logout(String userID, String applicationSessionID) {
    QFAUser user = QFAUser.find(userID, em);

    if (user.getSessions() != null) {
      for (QFASession session : user.getSessions()) {
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
    Query query = em
        .createQuery("SELECT s FROM Session s WHERE s.terminatedOn IS NULL");
    List<QFASession> queryResult = query.getResultList();
    if (queryResult != null) {
      for (QFASession session : queryResult) {
        logout(session.getUser().getId(),
            session.getApplicationSessionId());
      }
    }
  }

  public List<QFASessionDTO> isUserAlreadyLoggedIn(String userID) {
    QFAUser user = QFAUser.find(userID, em);
    Query q = em.createQuery("SELECT s FROM QFASession s WHERE "
        + "s.user = :user " + "and s.terminatedOn IS NULL "
        + "ORDER BY s.createdOn ASC");
    q.setParameter("user", user);

    List<QFASession> queryResult = q.getResultList();
    List<QFASessionDTO> retVal = new ArrayList<>(queryResult.size());
    for (QFASession session : queryResult) {
      retVal.add(QFAConverterUtil.sessionToSessionDTO(session));
    }

    return retVal.isEmpty() ? null : retVal;
  }

  public String updatePassword(String username, String newPassword) {
    QFAUser user = QFAUser.findByUsername(username, em);
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
    QFAUser user = QFAUser.find(userID, em);
    QFAGroup group = QFAGroup.findByName(groupName, em);
    boolean retVal = group.getUsers().contains(user);

    if (!retVal && includeChildren) {
      for (QFAGroup child : group.getChildren()) {
        if (belongsToGroupByName(userID, child.getName(),
            includeChildren)) {
          return true;
        }
      }
    }

    return retVal;
  }

  public void updateAttributes(Collection<QFAUserAttributeDTO> attributes,
      boolean createIfMissing) {
    for (QFAUserAttributeDTO attributeDTO : attributes) {
      updateAttribute(attributeDTO, createIfMissing);
    }
  }

  public void updateAttribute(QFAUserAttributeDTO attributeDTO,
      boolean createIfMissing) {
    String userId = attributeDTO.getUserId();
    String name = attributeDTO.getName();

    QFAUserAttribute attribute = QFAUser.findAttribute(userId, name, em);
    if (attribute != null) {
      mapAttribute(attribute, attributeDTO);
      em.merge(attribute);
    } else {
      if (createIfMissing) {
        attribute = new QFAUserAttribute();
        mapAttribute(attribute, attributeDTO);
        em.persist(attribute);
      } else {
        return;
      }
    }
  }

  private void mapAttribute(QFAUserAttribute attribute,
      QFAUserAttributeDTO attributeDTO) {
    String userId = attributeDTO.getUserId();
    String name = attributeDTO.getName();

    QFAUser user = QFAUser.find(userId, em);
    attribute.setUser(user);
    attribute.setName(name);

    attribute.setData(attributeDTO.getData());
    attribute.setBindata(attributeDTO.getBinData());
    attribute.setContentType(attributeDTO.getContentType());
  }

  public void deleteAttribute(String userID, String attributeName) {
    QFAUserAttribute attribute = QFAUser.findAttribute(userID, attributeName, em);
    em.remove(attribute);
  }

  public QFAUserAttributeDTO getAttribute(String userID, String attributeName) {
    QFAUserAttribute attribute = QFAUser.findAttribute(userID, attributeName, em);
    return QFAConverterUtil.userAttributeToUserAttributeDTO(attribute);
  }

  public Set<String> getUserIDsForAttribute(Collection<String> userIDs,
      String attributeName, String attributeValue) {
    String queryString = "SELECT u.id FROM QFAUser u "
        + "JOIN u.userAttributes ua "
        + "WHERE ua.name = :attributeName "
        + "AND ua.data = :attributeValue";
    if ((userIDs != null) && (userIDs.size() > 0)) {
      queryString = queryString.concat(" AND u.id IN (:users)");
    }
    Query q = em.createQuery(queryString);
    q.setParameter("attributeName", attributeName);
    q.setParameter("attributeValue", attributeValue);
    if ((userIDs != null) && (userIDs.size() > 0)) {
      q.setParameter("users", userIDs);
    }
    return new HashSet<>(q.getResultList());
  }

  // TODO check performance - jpa criteria do not support unions, a native
  // query may be needed instead if performance is not good.
  public List<QFAUserDTO> findUsers(QFAUserSearchCriteria criteria) {
    List<QFAUserDTO> result = new ArrayList<>();

    // QFAUserSearchHelper used in order to allow including the sort
    // criterion in the select
    // clause which is required by certain DBs such as PostgreSQL and H2.
    // Using a separate
    // class for the CriteriaQuery allows us to to a cq.multiselect when
    // applying sorting
    // criteria (see below) since this requires the object being returned by
    // the query to have
    // a two-argument constructor accepting the values passed in the
    // multiselect.
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<QFAUserSearchHelper> cq = cb.createQuery(
        QFAUserSearchHelper.class).distinct(true);
    Root<QFAUser> root = cq.from(QFAUser.class);

    if (criteria != null) {
      cq = applySearchCriteria(cb, cq, root, criteria);

      // Apply sorting. The sort criterion should be included in the select
      // clause
      // in order to support certain DBs such as PostgreSQL and H2
      Expression orderExpr = null;
      if (criteria.getSortColumn() != null) {
        cq.multiselect(root, root.get(criteria.getSortColumn()));
        orderExpr = root.get(criteria.getSortColumn());
      } else if (criteria.getSortAttribute() != null) {
        Join<QFAUser, QFAUserAttribute> join = root.joinList("userAttributes",
            JoinType.LEFT);
        cq.multiselect(root, join.get("data"));
        Predicate pr = cb.equal(join.get("name"),
            criteria.getSortAttribute());
        cq = addPredicate(cq, cb, pr);
        orderExpr = join.get("data");
      }
      Order order = null;
      if (criteria.isAscending()) {
        order = cb.asc(orderExpr);
      } else {
        order = cb.desc(orderExpr);
      }
      cq = cq.orderBy(order);
    }

    TypedQuery<QFAUserSearchHelper> query = em.createQuery(cq);

    if (criteria != null) {
      // Apply pagination
      if (criteria.getPaging() != null
          && criteria.getPaging().getCurrentPage() > -1) {
        query.setFirstResult((criteria.getPaging().getCurrentPage() - 1)
            * criteria.getPaging().getPageSize());
        query.setMaxResults(criteria.getPaging().getPageSize());
      }
    }

    for (QFAUserSearchHelper helper : query.getResultList()) {
      result.add(helper.getUserDTO());
    }
    return result;
  }

  public long findUserCount(QFAUserSearchCriteria criteria) {
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<Long> cq = cb.createQuery(Long.class);
    Root<QFAUser> root = cq.from(QFAUser.class);
    cq = cq.select(cb.count(root));
    cq = applySearchCriteria(cb, cq, root, criteria);
    TypedQuery<Long> query = em.createQuery(cq);
    return query.getSingleResult();
  }

  private <T> CriteriaQuery<T> applySearchCriteria(CriteriaBuilder cb,
      CriteriaQuery<T> query, Root<QFAUser> root, QFAUserSearchCriteria criteria) {
    CriteriaQuery<T> cq = query;

    // Include/exclude user IDs
    if (criteria.getIncludeIds() != null) {
      Predicate pr = root.get("id").in(criteria.getIncludeIds());
      cq = addPredicate(cq, cb, pr);
    }
    if (criteria.getExcludeIds() != null) {
      Predicate pr = cb.not(root.get("id").in(criteria.getExcludeIds()));
      cq = addPredicate(cq, cb, pr);
    }
    // Include/exclude user group IDs
    if (criteria.getIncludeGroupIds() != null) {
      Predicate pr = cb.equal(root.join("groups").get("id"),
          criteria.getIncludeGroupIds());
      cq = addPredicate(cq, cb, pr);
    }
    if (criteria.getExcludeGroupIds() != null) {
      Predicate pr = cb.notEqual(root.join("groups").get("id"),
          criteria.getIncludeGroupIds());
      ;
      cq = addPredicate(cq, cb, pr);
    }
    // Filter by status
    if (criteria.getIncludeStatuses() != null) {
      Predicate pr = root.get("status").in(criteria.getIncludeStatuses());
      cq = addPredicate(cq, cb, pr);
    }
    if (criteria.getExcludeStatuses() != null) {
      Predicate pr = cb.not(root.get("status").in(
          criteria.getExcludeStatuses()));
      cq = addPredicate(cq, cb, pr);
    }
    // Filter by username
    if (criteria.getUsername() != null) {
      Predicate pr = cb.like(root.<String>get("username"),
          criteria.getUsername());
      cq = addPredicate(cq, cb, pr);
    }
    // Filter by attributes
    if (criteria.getAttributeCriteria() != null) {
      cq = addPredicate(
          cq,
          cb,
          getAttributePredicate(cb, cq, root,
              criteria.getAttributeCriteria()));
    }
    // Filter by superadmin flag
    if (criteria.getSuperadmin() != null) {
      Predicate pr = cb.equal(root.get("superadmin"),
          criteria.getSuperadmin());
      cq = addPredicate(cq, cb, pr);
    }
    return cq;
  }

  private <T> Predicate getAttributePredicate(CriteriaBuilder cb,
      CriteriaQuery<T> cq, Root<QFAUser> root,
      UserAttributeCriteria attCriteria) {
    Predicate attributePredicate = null;

    // Please note that the way UserAttributeCriteria are constructed by the
    // UserSearchCriteriaBuilder
    // they are guranteed to only have not null attributes OR not null
    // attCriteria (not both).
    // The code below however uses two separate if statements instead of an
    // if/else in order
    // to be able to handle a future modification of the
    // UserAttributeCriteria which will allow
    // both properties to be not null.
    if (attCriteria.getAttributes() != null) {
      // For each attribute construct a predicate of the type:
      // user.id IN (SELECT user FROM aaa_user_attribute WHERE name = ...
      // and data = ...
      // and join it with AND/OR (depending on the type of the attribute
      // criteria) with
      // other such predicates constructed in previous loops.
      for (QFAUserAttributeDTO attribute : attCriteria.getAttributes()) {
        Subquery<String> sq = cq.subquery(String.class);
        Root<QFAUserAttribute> sqRoot = sq.from(QFAUserAttribute.class);
        sq = sq.select(sqRoot.get("user").<String>get("id")); // field
        // to
        // map
        // with
        // main-query

        // Construct the WHERE clause of the subquery
        if (StringUtils.isNotBlank(attribute.getName())) {
          addPredicate(sq, cb,
              cb.equal(sqRoot.get("name"), attribute.getName()));
        }
        if (StringUtils.isNotBlank(attribute.getData())) {
          // a like instead of equality will be performed on the attribute 
          // values if the useLike variable is set to true
          if (attCriteria.isUseLike()) {
            addPredicate(sq, cb,
                cb.like(sqRoot.get("data"), '%' + attribute.getData() + '%'));
          } else {
            addPredicate(sq, cb,
                cb.equal(sqRoot.get("data"), attribute.getData()));
          }
        }
        if ((attribute.getBinData() != null)
            && (attribute.getBinData().length > 0)) {
          addPredicate(
              sq,
              cb,
              cb.equal(sqRoot.get("bindata"),
                  attribute.getBinData()));
        }
        if (StringUtils.isNotBlank(attribute.getContentType())) {
          addPredicate(
              sq,
              cb,
              cb.equal(sqRoot.get("contentType"),
                  attribute.getContentType()));
        }

        Predicate pr = root.get("id").in(sq);
        if (attributePredicate == null) {
          attributePredicate = pr;
        } else {
          switch (attCriteria.getType()) {
            case AND:
              attributePredicate = cb.and(attributePredicate, pr);
              break;
            case OR:
              attributePredicate = cb.or(attributePredicate, pr);
              break;
          }
        }
      }
    }
    if (attCriteria.getAttCriteria() != null) {
      // For each attribute criterion get the predicate corresponding to
      // it recursively
      // and then join it with AND/OR (depending on the type of the
      // attribute criteria) with
      // other such predicates retrieved in previous loops.
      for (UserAttributeCriteria nestedCriteria : attCriteria
          .getAttCriteria()) {
        Predicate nestedPredicate = getAttributePredicate(cb, cq, root,
            nestedCriteria);
        if (attributePredicate == null) {
          attributePredicate = nestedPredicate;
        } else {
          switch (attCriteria.getType()) {
            case AND:
              attributePredicate = cb.and(attributePredicate,
                  nestedPredicate);
              break;
            case OR:
              attributePredicate = cb.or(attributePredicate,
                  nestedPredicate);
              break;
          }
        }
      }
    }

    return attributePredicate;
  }

  private <T> CriteriaQuery<T> addPredicate(CriteriaQuery<T> query,
      CriteriaBuilder cb, Predicate pr) {
    CriteriaQuery<T> cq = query;
    if (cq.getRestriction() != null) {
      cq = cq.where(cb.and(cq.getRestriction(), pr));
    } else {
      cq = cq.where(pr);
    }
    return cq;
  }

  private <T> Subquery<T> addPredicate(Subquery<T> query, CriteriaBuilder cb,
      Predicate pr) {
    Subquery<T> sq = query;
    if (sq.getRestriction() != null) {
      sq = sq.where(cb.and(sq.getRestriction(), pr));
    } else {
      sq = sq.where(pr);
    }
    return sq;
  }

  public boolean isAttributeValueUnique(String attributeValue,
      String attributeName, String userID) {

    boolean isAttributeValueUnique = false;
    QQFAUserAttribute quserAttribute = QQFAUserAttribute.qFAUserAttribute;

    List<QFAUserAttribute> userAttributes = new JPAQueryFactory(em)
        .selectFrom(quserAttribute)
        .where(quserAttribute.name.eq(attributeName)
            .and(quserAttribute.data.eq(attributeValue)))
        .fetch();
    // convert Set to List
    Set<QFAUserAttributeDTO> set = QFAConverterUtil
        .userAttributesToUserAttributeDTOSet(userAttributes);
    ArrayList<QFAUserAttributeDTO> list = new ArrayList<QFAUserAttributeDTO>(set);
    //in case of no user exists with this user attribute value	or there is only the given user
    if ((list.size() == 1 && list.get(0).getUserId().equals(userID)) || (list.size() == 0)) {
      isAttributeValueUnique = true;
    }
    return isAttributeValueUnique;
  }
}
