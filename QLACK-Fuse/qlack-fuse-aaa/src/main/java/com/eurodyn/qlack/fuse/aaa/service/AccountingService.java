package com.eurodyn.qlack.fuse.aaa.service;

import com.eurodyn.qlack.common.exceptions.QDoesNotExistException;
import com.eurodyn.qlack.fuse.aaa.dto.SessionAttributeDTO;
import com.eurodyn.qlack.fuse.aaa.dto.SessionDTO;
import com.eurodyn.qlack.fuse.aaa.model.QSession;
import com.eurodyn.qlack.fuse.aaa.model.QSessionAttribute;
import com.eurodyn.qlack.fuse.aaa.model.Session;
import com.eurodyn.qlack.fuse.aaa.model.SessionAttribute;
import com.eurodyn.qlack.fuse.aaa.model.User;
import com.eurodyn.qlack.fuse.aaa.util.ConverterUtil;
import com.querydsl.jpa.impl.JPAQueryFactory;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.text.MessageFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Provides accounting information for the user.
 * For details regarding the functionality offered see the respective interfaces.
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
  @PersistenceContext
  private EntityManager em;

  public String createSession(SessionDTO session) {
    Session entity = ConverterUtil.sessionDTOToSession(session, em);
    if (entity.getCreatedOn() == 0) {
      entity.setCreatedOn(Instant.now().toEpochMilli());
    }
    em.persist(entity);
    if (entity.getSessionAttributes() != null) {
      for (SessionAttribute attribute : entity.getSessionAttributes()) {
        em.persist(attribute);
      }
    }
    return entity.getId();
  }


  public void terminateSession(String sessionID) {
    Session sessionEntity = Session.find(sessionID, em);
    if (sessionEntity != null) {
      sessionEntity.setTerminatedOn(Instant.now().toEpochMilli());
    } else {
      LOGGER
          .log(Level.WARNING,
              "Requested to terminate a session that does not exist, session ID: {0}",
              sessionID);
    }
  }

  public void terminateSessionByApplicationSessionId(String applicationSessionId) {
    final Session session = new JPAQueryFactory(em)
        .selectFrom(qSession)
        .where(qSession.applicationSessionId.eq(applicationSessionId))
        .fetchOne();
    if (session != null) {
      terminateSession(session.getId());
    } else {
      throw new QDoesNotExistException(MessageFormat
          .format("Session with application session Id {0} could not be found to be terminated.",
              applicationSessionId));
    }
  }


  public SessionDTO getSession(String sessionID) {
    return ConverterUtil.sessionToSessionDTO(Session.find(sessionID, em));
  }


  public Long getSessionDuration(String sessionID) {
    Session session = Session.find(sessionID, em);
    if (session.getTerminatedOn() == null) {
      return null;
    }
    return session.getTerminatedOn() - session.getCreatedOn();
  }

  public Long getUserLastLogIn(String userID) {
    Query q = em.createQuery("SELECT MAX(s.createdOn) FROM com.eurodyn.qlack.fuse.aaa.model.Session s WHERE s.user = :user");
    q.setParameter("user", User.find(userID, em));
    List<Long> queryResult = q.getResultList();
    if (CollectionUtils.isEmpty(queryResult)) {
      return null;
    }
    return queryResult.get(0);
  }

  public Long getUserLastLogOut(String userID) {
    Query q = em.createQuery("SELECT MAX(s.terminatedOn) FROM com.eurodyn.qlack.fuse.aaa.model.Session s WHERE s.user = :user");
    q.setParameter("user", User.find(userID, em));
    List<Long> queryResult = q.getResultList();
    if (CollectionUtils.isEmpty(queryResult)) {
      return null;
    }
    return queryResult.get(0);
  }

  public Long getUserLastLogInDuration(String userID) {
    Query q = em.createQuery("SELECT s FROM Session s WHERE s.user = :user "
        + "AND s.terminatedOn = (SELECT MAX(s.terminatedOn) FROM com.eurodyn.qlack.fuse.aaa.model.Session s WHERE s.user = :user)");
    q.setParameter("user", User.find(userID, em));
    List<Session> queryResult = q.getResultList();
    // Also checking the terminatedOn value of the retrieved result in case
    // there is no terminated session and thus the query
    // SELECT MAX(s.terminatedOn) FROM AaaSession s WHERE s.user = :user
    // returns null.
    if ((CollectionUtils.isEmpty(queryResult)) || (queryResult.get(0).getTerminatedOn() == null)) {
      return null;
    }
    Session session = queryResult.get(0);
    return session.getTerminatedOn() - session.getCreatedOn();
  }

  public long getNoOfTimesUserLoggedIn(String userID) {
    Query q = em.createQuery("SELECT COUNT(s) FROM com.eurodyn.qlack.fuse.aaa.model.Session s WHERE s.user = :user");
    q.setParameter("user", User.find(userID, em));
    return (Long) q.getSingleResult();
  }

  public Set<String> filterOnlineUsers(Collection<String> userIDs) {
    Query query = em.createQuery(
        "SELECT DISTINCT s.user.id FROM com.eurodyn.qlack.fuse.aaa.model.Session s "
            + "WHERE s.terminatedOn IS NULL "
            + "AND s.user.id in (:userIDs)");
    query.setParameter("userIDs", userIDs);
    return new HashSet<String>(query.getResultList());
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
      SessionAttribute attribute = Session.findAttribute(
          attributeDTO.getSessionId(), attributeDTO.getName(), em);
      if ((attribute == null) && createIfMissing) {
        attribute = new SessionAttribute();
        attribute.setName(attributeDTO.getName());
        attribute.setSession(Session.find(attributeDTO.getSessionId(), em));
      }
      attribute.setValue(attributeDTO.getValue());
      em.merge(attribute);
    }
  }

  public void deleteAttribute(String sessionID, String attributeName) {
    SessionAttribute attribute = Session.findAttribute(sessionID, attributeName, em);
    em.remove(attribute);
  }

  public SessionAttributeDTO getAttribute(String sessionID, String attributeName) {
    return ConverterUtil.sessionAttributeToSessionAttributeDTO(Session
        .findAttribute(sessionID, attributeName, em));
  }

  public Set<String> getSessionIDsForAttribute(Collection<String> sessionIDs,
      String attributeName, String attributeValue) {
    String queryString = "SELECT s.id FROM com.eurodyn.qlack.fuse.aaa.model.Session s "
        + "JOIN s.sessionAttributes sa "
        + "WHERE sa.name = :attributeName "
        + "AND sa.value = :attributeValue";
    if ((sessionIDs != null) && (sessionIDs.size() > 0)) {
      queryString = queryString.concat(" AND s.id IN (:sessions)");
    }
    Query q = em.createQuery(queryString);
    q.setParameter("attributeName", attributeName);
    q.setParameter("attributeValue", attributeValue);
    if ((sessionIDs != null) && (sessionIDs.size() > 0)) {
      q.setParameter("sessions", sessionIDs);
    }
    return new HashSet<>(q.getResultList());
  }

  public boolean isAttributeValueUnique(String userId, String attributeName,
      String attributeValue) {
    long count = new JPAQueryFactory(em)
        .selectFrom(qSessionAttribute)
        .innerJoin(qSessionAttribute.session, qSession)
        .where(qSession.user.id.eq(userId), qSessionAttribute.name.eq(attributeName),
            qSessionAttribute.value.eq(attributeValue))
        .fetchCount();

    return count == 0;
  }

  public long deleteOldSessions(long deleteBeforeDate) {
    return new JPAQueryFactory(em)
        .delete(qSession)
        .where(qSession.createdOn.lt(deleteBeforeDate))
        .execute();
  }
}