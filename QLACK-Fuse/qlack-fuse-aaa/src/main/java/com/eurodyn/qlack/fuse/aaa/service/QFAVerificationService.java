package com.eurodyn.qlack.fuse.aaa.service;

import com.eurodyn.qlack.fuse.aaa.model.QFAUser;
import com.eurodyn.qlack.fuse.aaa.model.QFAVerificationToken;
import com.eurodyn.qlack.fuse.aaa.model.QQFAVerificationToken;
import com.querydsl.jpa.impl.JPAQueryFactory;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.Instant;
import java.util.UUID;

@Service
@Validated
@Transactional
public class QFAVerificationService {

  @PersistenceContext
  private EntityManager em;

  public String createVerificationToken(String userId, long expiresOn) {
    return createVerificationToken(userId, expiresOn, null);
  }

  public String createVerificationToken(String userId, long expiresOn, String data) {
    QFAVerificationToken vt = new QFAVerificationToken();
    vt.setUser(QFAUser.find(userId, em));
    vt.setCreatedOn(Instant.now().toEpochMilli());
    if (data != null) {
      vt.setData(data);
    }
    vt.setExpiresOn(expiresOn);
    vt.setId(UUID.randomUUID().toString());
    em.persist(vt);

    return vt.getId();
  }

  public String verifyToken(String tokenID) {
    String userId = null;
    QFAVerificationToken vt = em.find(QFAVerificationToken.class, tokenID);
    if (vt != null && vt.getExpiresOn() >= Instant.now().toEpochMilli()) {
      userId = vt.getUser().getId();
    }

    return userId;
  }

  public void deleteToken(String tokenID) {
    QFAVerificationToken vt = em.find(QFAVerificationToken.class, tokenID);
    if (vt != null) {
      em.remove(vt);
    }

    // Each time a token is deleted perform some housekeeping to also delete any other expired
    // tokens.
    QQFAVerificationToken qvt = QQFAVerificationToken.qFAVerificationToken;
    new JPAQueryFactory(em).delete(qvt)
        .where(qvt.expiresOn.lt(Instant.now().toEpochMilli())).execute();
  }

  public String getTokenPayload(String tokenID) {
    QFAVerificationToken vt = em.find(QFAVerificationToken.class, tokenID);
    if (vt != null) {
      return vt.getData();
    } else {
      return null;
    }
  }

  public String getTokenUser(String tokenID) {
    String userId = null;

    QFAVerificationToken vt = em.find(QFAVerificationToken.class, tokenID);
    if (vt != null) {
      userId = vt.getUser().getId();
    }

    return userId;
  }

}
