package com.eurodyn.qlack.fuse.audit.repository;

import com.eurodyn.qlack.fuse.audit.model.QAudit;
import com.querydsl.jpa.impl.JPAQueryFactory;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;

public class AuditRepositoryImpl implements AuditRepositoryExt {
  @PersistenceContext
  private EntityManager em;

  @Override
  public List<String> findDistinctEventsByReferenceId(String referenceId) {
    QAudit audit = QAudit.audit;
    return new JPAQueryFactory(em)
        .select(audit.event)
        .from(audit)
        .where(audit.referenceId.eq(referenceId))
        .orderBy(audit.event.asc())
        .distinct().fetch();
  }
}
