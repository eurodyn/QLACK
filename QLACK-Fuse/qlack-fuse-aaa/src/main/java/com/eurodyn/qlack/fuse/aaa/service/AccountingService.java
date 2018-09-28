package com.eurodyn.qlack.fuse.aaa.service;

import com.eurodyn.qlack.common.exceptions.QDoesNotExistException;
import com.eurodyn.qlack.fuse.aaa.dto.SessionAttributeDTO;
import com.eurodyn.qlack.fuse.aaa.dto.SessionDTO;
import com.eurodyn.qlack.fuse.aaa.mappers.SessionAttributeMapper;
import com.eurodyn.qlack.fuse.aaa.mappers.SessionMapper;
import com.eurodyn.qlack.fuse.aaa.model.QSession;
import com.eurodyn.qlack.fuse.aaa.model.QSessionAttribute;
import com.eurodyn.qlack.fuse.aaa.model.Session;
import com.eurodyn.qlack.fuse.aaa.model.SessionAttribute;
import com.eurodyn.qlack.fuse.aaa.repository.SessionAttributeRepository;
import com.eurodyn.qlack.fuse.aaa.repository.SessionRepository;
import com.eurodyn.qlack.fuse.aaa.repository.UserRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import java.text.MessageFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

/**
 * Provides accounting information for the user. For details regarding the functionality offered see
 * the respective interfaces.
 *
 * @author European Dynamics SA
 */
@Service
@Validated
@Transactional
public class AccountingService {

  // JUL reference.
  private static final Logger LOGGER = Logger.getLogger(AccountingService.class.getName());

  // QuertyDSL helpers.
  private static QSession qSession = QSession.session;
  private static QSessionAttribute qSessionAttribute = QSessionAttribute.sessionAttribute;

//  @PersistenceContext
//  private EntityManager em;

  // Service references
  private final SessionRepository sessionRepository;
  private final UserRepository userRepository;
  private final SessionAttributeRepository sessionAttributeRepository;
  private final SessionMapper sessionMapper;
  private final SessionAttributeMapper sessionAttributeMapper;

  @Autowired
  public AccountingService(SessionRepository sessionRepository,
      SessionAttributeRepository sessionAttributeRepository,
      SessionMapper SessionMapper, UserRepository userRepository,
      SessionAttributeMapper sessionAttributeMapper) {
    this.sessionRepository = sessionRepository;
    this.sessionAttributeRepository = sessionAttributeRepository;
    this.sessionMapper = SessionMapper;
    this.userRepository = userRepository;
    this.sessionAttributeMapper = sessionAttributeMapper;
  }

  public String createSession(SessionDTO sessionDTO) {
//    Session entity = ConverterUtil.sessionDTOToSession(session, em);
//    if (entity.getCreatedOn() == 0) {
//      entity.setCreatedOn(Instant.now().toEpochMilli());
//    }
//    em.persist(entity);
//    if (entity.getSessionAttributes() != null) {
//      for (SessionAttribute attribute : entity.getSessionAttributes()) {
//        em.persist(attribute);
//      }
//    }
//    return entity.getId();
    Session entity = sessionMapper.mapToEntity(sessionDTO);
    if (entity.getCreatedOn() == 0) {
      entity.setCreatedOn(Instant.now().toEpochMilli());
    }
    entity.setUser(userRepository.fetchById(sessionDTO.getUserId()));
    sessionRepository.save(entity);
    if (entity.getSessionAttributes() != null) {
      for (SessionAttribute attribute : entity.getSessionAttributes()) {
        attribute.setSession(entity);
        sessionAttributeRepository.save(attribute);
      }
    }

    return entity.getId();
  }

  public void terminateSession(String sessionID) {
    final Optional<Session> session = sessionRepository.findById(sessionID);
    if (session.isPresent()) {
      session.get().setTerminatedOn(Instant.now().toEpochMilli());
    } else {
      LOGGER
          .log(Level.WARNING,
              "Requested to terminate a session that does not exist, session ID: {0}",
              sessionID);
    }
  }

  public void terminateSessionByApplicationSessionId(String applicationSessionId) {
//    final Session session = new JPAQueryFactory(em)
//        .selectFrom(qSession)
//        .where(qSession.applicationSessionId.eq(applicationSessionId))
//        .fetchOne();
//    if (session != null) {
//      terminateSession(session.getId());
//    } else {
//      throw new QDoesNotExistException(MessageFormat
//          .format("Session with application session Id {0} could not be found to be terminated.",
//              applicationSessionId));
//    }
    Predicate predicate = qSession.applicationSessionId.eq(applicationSessionId);
    final Session session = sessionRepository.findOne(predicate)
        .orElseThrow(()-> new QDoesNotExistException(MessageFormat
            .format("Session with application session Id {0} could not be found to be terminated.",
                applicationSessionId)));

    terminateSession(session.getId());
  }

  public SessionDTO getSession(String sessionID) {

    return sessionMapper.mapToDTO(findSession(sessionID));
  }

  public Long getSessionDuration(String sessionID) {
    Session session = findSession(sessionID);
    if (session.getTerminatedOn() == null) {
      return null;
    }

    return session.getTerminatedOn() - session.getCreatedOn();
  }

  public Long getUserLastLogIn(String userID) {
//    Query q = em.createQuery(
//        "SELECT MAX(s.createdOn) FROM com.eurodyn.qlack.fuse.aaa.model.Session s WHERE s.user = :user");
//    q.setParameter("user", User.find(userID, em));
//    List<Long> queryResult = q.getResultList();
//    if (CollectionUtils.isEmpty(queryResult)) {
//      return null;
//    }
//    return queryResult.get(0);
    Predicate predicate = qSession.user.id.eq(userID);
    List<Session> queryResult = sessionRepository
        .findAll(predicate, Sort.by("createdOn").descending());
    if (queryResult.isEmpty()) {
      return null;
    }

    return queryResult.get(0).getCreatedOn();
  }

  public Long getUserLastLogOut(String userID) {
//    Query q = em.createQuery(
//        "SELECT MAX(s.terminatedOn) FROM com.eurodyn.qlack.fuse.aaa.model.Session s WHERE s.user = :user");
//    q.setParameter("user", User.find(userID, em));
//    List<Long> queryResult = q.getResultList();
//    if (CollectionUtils.isEmpty(queryResult)) {
//      return null;
//    }
//    return queryResult.get(0);
    Predicate predicate = qSession.user.id.eq(userID);
    List<Session> queryResult = sessionRepository
        .findAll(predicate, Sort.by("terminatedOn").descending());
    if (queryResult.isEmpty()) {
      return null;
    }

    return queryResult.get(0).getCreatedOn();
  }

  public Long getUserLastLogInDuration(String userID) {
//    Query q = em.createQuery("SELECT s FROM Session s WHERE s.user = :user "
//        + "AND s.terminatedOn = (SELECT MAX(s.terminatedOn) FROM com.eurodyn.qlack.fuse.aaa.model.Session s WHERE s.user = :user)");
//    q.setParameter("user", User.find(userID, em));
//    List<Session> queryResult = q.getResultList();
//    // Also checking the terminatedOn value of the retrieved result in case
//    // there is no terminated session and thus the query
//    // SELECT MAX(s.terminatedOn) FROM AaaSession s WHERE s.user = :user
//    // returns null.
//    if ((CollectionUtils.isEmpty(queryResult)) || (queryResult.get(0).getTerminatedOn() == null)) {
//      return null;
//    }
//    Session session = queryResult.get(0);
//    return session.getTerminatedOn() - session.getCreatedOn();
    Predicate predicate = qSession.user.id.eq(userID);
    List<Session> queryResult = sessionRepository
        .findAll(predicate, Sort.by("terminatedOn").descending());
    if (queryResult.isEmpty() || (queryResult.get(0).getTerminatedOn() == null)) {
      return null;
    }
    Session session = queryResult.get(0);

    return session.getTerminatedOn() - session.getCreatedOn();
  }

  public long getNoOfTimesUserLoggedIn(String userID) {
//    Query q = em.createQuery(
//        "SELECT COUNT(s) FROM com.eurodyn.qlack.fuse.aaa.model.Session s WHERE s.user = :user");
//    q.setParameter("user", User.find(userID, em));
//    return (Long) q.getSingleResult();
    Predicate predicate = qSession.user.id.eq(userID);

    return (long) sessionRepository.findAll(predicate).size();
  }

  public Set<String> filterOnlineUsers(Collection<String> userIDs) {
//    Query query = em.createQuery(
//        "SELECT DISTINCT s.user.id FROM com.eurodyn.qlack.fuse.aaa.model.Session s "
//            + "WHERE s.terminatedOn IS NULL "
//            + "AND s.user.id in (:userIDs)");
//    query.setParameter("userIDs", userIDs);
//    return new HashSet<String>(query.getResultList());
    Predicate predicate = qSession.terminatedOn.isNull().and(qSession.user.id.in(userIDs));

    return sessionRepository.findAll(predicate).stream()
        .map(session -> session.getUser().getId())
        .collect(Collectors.toSet());
  }


  public void updateAttribute(SessionAttributeDTO attribute,
      boolean createIfMissing) {
    Collection<SessionAttributeDTO> attributes = new ArrayList<>(1);
    attributes.add(attribute);
    updateAttributes(attributes, createIfMissing);
  }

  public void updateAttributes(Collection<SessionAttributeDTO> attributes,
      boolean createIfMissing) {
    for (SessionAttributeDTO attributeDTO : attributes) {
      SessionAttribute attribute = sessionAttributeRepository.findBySessionIdAndName(
          attributeDTO.getSessionId(), attributeDTO.getName());
      if ((attribute == null) && createIfMissing) {
        attribute = new SessionAttribute();
        attribute.setName(attributeDTO.getName());
        attribute.setSession(findSession(attributeDTO.getSessionId()));
      }
      attribute.setValue(attributeDTO.getValue());
//      em.merge(attribute);
      sessionAttributeRepository.save(attribute);
    }
  }

  public void deleteAttribute(String sessionID, String attributeName) {
    SessionAttribute attribute = sessionAttributeRepository
        .findBySessionIdAndName(sessionID, attributeName);
//    em.remove(attribute);
    sessionAttributeRepository.delete(attribute);
  }

  public SessionAttributeDTO getAttribute(String sessionID, String attributeName) {

    return sessionAttributeMapper.mapToDTO(
        sessionAttributeRepository.findBySessionIdAndName(sessionID, attributeName));
  }

  public Set<String> getSessionIDsForAttribute(Collection<String> sessionIDs,
      String attributeName, String attributeValue) {
//    String queryString = "SELECT s.id FROM com.eurodyn.qlack.fuse.aaa.model.Session s "
//        + "JOIN s.sessionAttributes sa "
//        + "WHERE sa.name = :attributeName "
//        + "AND sa.value = :attributeValue";
//    if ((sessionIDs != null) && (sessionIDs.size() > 0)) {
//      queryString = queryString.concat(" AND s.id IN (:sessions)");
//    }
//    Query q = em.createQuery(queryString);
//    q.setParameter("attributeName", attributeName);
//    q.setParameter("attributeValue", attributeValue);
//    if ((sessionIDs != null) && (sessionIDs.size() > 0)) {
//      q.setParameter("sessions", sessionIDs);
//    }
//    return new HashSet<>(q.getResultList());
    Predicate predicate = qSession.sessionAttributes.any().name.eq(attributeName)
        .and(qSession.sessionAttributes.any().value.eq(attributeValue));
    if (sessionIDs != null) {
      BooleanBuilder builder = new BooleanBuilder();
      for (String id : sessionIDs) {
        builder.or(qSession.id.eq(id));
      }
      predicate = ((BooleanExpression) predicate).and(builder);
    }

    return sessionRepository.findAll(predicate).stream()
        .map(session -> session.getUser().getId())
        .collect(Collectors.toSet());
  }

  public boolean isAttributeValueUnique(String userId, String attributeName,
      String attributeValue) {
//    long count = new JPAQueryFactory(em)
//        .selectFrom(qSessionAttribute)
//        .innerJoin(qSessionAttribute.session, qSession)
//        .where(qSession.user.id.eq(userId), qSessionAttribute.name.eq(attributeName),
//            qSessionAttribute.value.eq(attributeValue))
//        .fetchCount();
//
//    return count == 0;
    Predicate predicate = qSession.sessionAttributes.any().name.eq(attributeName)
        .and(qSession.sessionAttributes.any().value.eq(attributeName))
        .and(qSession.user.id.eq(userId));

    return sessionRepository.findAll(predicate).isEmpty();
  }

  public void deleteSessionsBeforeDate(Date date) {
    sessionRepository.deleteByCreatedOnBefore(date.getTime());
  }

  public void terminateSessionsBeforeDate(Date date) {
    final List<Session> sessions = sessionRepository.findByCreatedOnBeforeAndTerminatedOnNull(
        date.getTime());
    sessions.forEach(o -> terminateSession(o.getId()));
  }

  public Page<SessionDTO> getSessions(String userId, Pageable pageable) {
    return sessionMapper.fromSessions(sessionRepository.findByUserId(userId, pageable));
  }

  private Session findSession(String sessionId){

    return sessionRepository.fetchById(sessionId);
  }

}