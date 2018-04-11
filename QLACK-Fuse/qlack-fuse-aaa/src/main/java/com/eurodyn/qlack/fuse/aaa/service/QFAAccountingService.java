package com.eurodyn.qlack.fuse.aaa.service;

import com.eurodyn.qlack.common.exceptions.QDoesNotExistException;
import com.eurodyn.qlack.fuse.aaa.dto.QFASessionAttributeDTO;
import com.eurodyn.qlack.fuse.aaa.dto.QFASessionDTO;
import com.eurodyn.qlack.fuse.aaa.model.QFASession;
import com.eurodyn.qlack.fuse.aaa.model.QFASessionAttribute;
import com.eurodyn.qlack.fuse.aaa.model.QFAUser;
import com.eurodyn.qlack.fuse.aaa.model.QQFASession;
import com.eurodyn.qlack.fuse.aaa.model.QQFASessionAttribute;
import com.eurodyn.qlack.fuse.aaa.util.QFAConverterUtil;
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
public class QFAAccountingService {

  // JUL reference.
  private static final Logger LOGGER = Logger.getLogger(QFAAccountingService.class.getName());

  @PersistenceContext
  private EntityManager em;

  // QuertyDSL helpers.
  private static QQFASession qSession = QQFASession.qFASession;
  private static QQFASessionAttribute qSessionAttribute = QQFASessionAttribute.qFASessionAttribute;

  public String createSession(QFASessionDTO session) {
    QFASession entity = QFAConverterUtil.sessionDTOToSession(session, em);
    if (entity.getCreatedOn() == 0) {
      entity.setCreatedOn(Instant.now().toEpochMilli());
    }
    em.persist(entity);
    if (entity.getSessionAttributes() != null) {
      for (QFASessionAttribute attribute : entity.getSessionAttributes()) {
        em.persist(attribute);
      }
    }
    return entity.getId();
  }


  public void terminateSession(String sessionID) {
    QFASession sessionEntity = QFASession.find(sessionID, em);
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
    final QFASession session = new JPAQueryFactory(em)
        .selectFrom(qSession)
        .where(qSession.applicationSessionId.eq(applicationSessionId))
        .fetchOne();
    if (session != null) {
      terminateSession(session.getId());
    } else {
      throw new QDoesNotExistException(MessageFormat
          .format("QFASession with application session Id {0} could not be found to be terminated.",
              applicationSessionId));
    }
  }


  public QFASessionDTO getSession(String sessionID) {
    return QFAConverterUtil.sessionToSessionDTO(QFASession.find(sessionID, em));
  }


  public Long getSessionDuration(String sessionID) {
    QFASession session = QFASession.find(sessionID, em);
    if (session.getTerminatedOn() == null) {
      return null;
    }
    return session.getTerminatedOn() - session.getCreatedOn();
  }

  public Long getUserLastLogIn(String userID) {
    Query q = em.createQuery("SELECT MAX(s.createdOn) FROM QFASession s WHERE s.user = :user");
    q.setParameter("user", QFAUser.find(userID, em));
    List<Long> queryResult = q.getResultList();
    if (CollectionUtils.isEmpty(queryResult)) {
      return null;
    }
    return queryResult.get(0);
  }

  public Long getUserLastLogOut(String userID) {
    Query q = em.createQuery("SELECT MAX(s.terminatedOn) FROM QFASession s WHERE s.user = :user");
    q.setParameter("user", QFAUser.find(userID, em));
    List<Long> queryResult = q.getResultList();
    if (CollectionUtils.isEmpty(queryResult)) {
      return null;
    }
    return queryResult.get(0);
  }

  public Long getUserLastLogInDuration(String userID) {
    Query q = em.createQuery("SELECT s FROM QFASession s WHERE s.user = :user "
        + "AND s.terminatedOn = (SELECT MAX(s.terminatedOn) FROM QFASession s WHERE s.user = :user)");
    q.setParameter("user", QFAUser.find(userID, em));
    List<QFASession> queryResult = q.getResultList();
    // Also checking the terminatedOn value of the retrieved result in case
    // there is no terminated session and thus the query
    // SELECT MAX(s.terminatedOn) FROM AaaSession s WHERE s.user = :user
    // returns null.
    if ((CollectionUtils.isEmpty(queryResult)) || (queryResult.get(0).getTerminatedOn() == null)) {
      return null;
    }
    QFASession session = queryResult.get(0);
    return session.getTerminatedOn() - session.getCreatedOn();
  }

  public long getNoOfTimesUserLoggedIn(String userID) {
    Query q = em.createQuery("SELECT COUNT(s) FROM QFASession s WHERE s.user = :user");
    q.setParameter("user", QFAUser.find(userID, em));
    return (Long) q.getSingleResult();
  }

  public Set<String> filterOnlineUsers(Collection<String> userIDs) {
    Query query = em.createQuery(
        "SELECT DISTINCT s.user.id FROM QFASession s "
            + "WHERE s.terminatedOn IS NULL "
            + "AND s.user.id in (:userIDs)");
    query.setParameter("userIDs", userIDs);
    return new HashSet<String>(query.getResultList());
  }


  public void updateAttribute(QFASessionAttributeDTO attribute,
      boolean createIfMissing) {
    Collection<QFASessionAttributeDTO> attributes = new ArrayList<>(1);
    attributes.add(attribute);
    updateAttributes(attributes, createIfMissing);
  }

  public void updateAttributes(Collection<QFASessionAttributeDTO> attributes,
      boolean createIfMissing) {
    for (QFASessionAttributeDTO attributeDTO : attributes) {
      QFASessionAttribute attribute = QFASession.findAttribute(
          attributeDTO.getSessionId(), attributeDTO.getName(), em);
      if ((attribute == null) && createIfMissing) {
        attribute = new QFASessionAttribute();
        attribute.setName(attributeDTO.getName());
        attribute.setSession(QFASession.find(attributeDTO.getSessionId(), em));
      }
      attribute.setValue(attributeDTO.getValue());
      em.merge(attribute);
    }
  }

  public void deleteAttribute(String sessionID, String attributeName) {
    QFASessionAttribute attribute = QFASession.findAttribute(sessionID, attributeName, em);
    em.remove(attribute);
  }

  public QFASessionAttributeDTO getAttribute(String sessionID, String attributeName) {
    return QFAConverterUtil.sessionAttributeToSessionAttributeDTO(QFASession
        .findAttribute(sessionID, attributeName, em));
  }

  public Set<String> getSessionIDsForAttribute(Collection<String> sessionIDs,
      String attributeName, String attributeValue) {
    String queryString = "SELECT s.id FROM QFASession s "
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