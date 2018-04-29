package com.eurodyn.qlack.fuse.audit.service;

import com.eurodyn.qlack.common.search.PagingParams;
import com.eurodyn.qlack.fuse.audit.dto.AuditLogDTO;
import com.eurodyn.qlack.fuse.audit.dto.SearchDTO;
import com.eurodyn.qlack.fuse.audit.dto.SortDTO;
import com.eurodyn.qlack.fuse.audit.enums.AuditLogColumns;
import com.eurodyn.qlack.fuse.audit.enums.SearchOperator;
import com.eurodyn.qlack.fuse.audit.enums.SortOperator;
import com.eurodyn.qlack.fuse.audit.model.Audit;
import com.eurodyn.qlack.fuse.audit.model.AuditLevel;
import com.eurodyn.qlack.fuse.audit.util.AuditProperties;
import com.eurodyn.qlack.fuse.audit.util.ConverterUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@Transactional
@Validated
public class AuditService {

  private static final Logger LOGGER = Logger
      .getLogger(AuditService.class.getSimpleName());

  private static final ObjectMapper mapper = new ObjectMapper();

  @PersistenceContext
  private EntityManager em;

  private AuditProperties auditProperties;

  @Autowired
  public AuditService(AuditProperties auditProperties) {
    this.auditProperties = auditProperties;
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

  private <T> CriteriaQuery<T> applySearchCriteria(CriteriaBuilder cb,
      CriteriaQuery<T> query, Root<Audit> root,
      List<SearchDTO> searchList, Date startDate, Date endDate) {
    CriteriaQuery<T> cq = query;

    if (searchList != null) {
      for (SearchDTO searchDTO : searchList) {
        if (SearchOperator.EQUAL == searchDTO.getOperator()) {
          if ("levelId".equals(searchDTO.getColumn().name())) {
            Predicate pr = root.get("levelId").get("name").in(searchDTO.getValue());
            cq = addPredicate(cq, cb, pr);
          } else {
            Predicate pr = root.get(searchDTO.getColumn().name()).in(
                searchDTO.getValue());
            cq = addPredicate(cq, cb, pr);
          }
        } else if (SearchOperator.LIKE == searchDTO.getOperator()) {
          if ("levelId".equals(searchDTO.getColumn().name())) {
            Expression expression = root.get(searchDTO.getColumn()
                .name()).get("name");
            Predicate pr = cb.like(expression, "%"
                + searchDTO.getValue().get(0) + "%");

            cq = addPredicate(cq, cb, pr);
          } else {
            Expression expression = root.get(searchDTO.getColumn()
                .name());
            Predicate pr = cb.like(expression, "%"
                + searchDTO.getValue().get(0) + "%");

            cq = addPredicate(cq, cb, pr);
          }
        }
      }
    }

    if (startDate != null) {

      Calendar cal = Calendar.getInstance();
      cal.setTime(startDate);
      cal.set(Calendar.HOUR_OF_DAY, 0);
      cal.set(Calendar.MINUTE, 0);
      cal.set(Calendar.SECOND, 59);
      startDate = cal.getTime();
      Expression expression = root.get("createdOn");
      Predicate pr = cb.greaterThanOrEqualTo(expression,
          startDate.getTime());

      cq = addPredicate(cq, cb, pr);

    }
    if (endDate != null) {
      Calendar cal = Calendar.getInstance();
      cal.setTime(endDate);
      cal.set(Calendar.HOUR_OF_DAY, 23);
      cal.set(Calendar.MINUTE, 59);
      cal.set(Calendar.SECOND, 59);
      endDate = cal.getTime();
      Expression expression = root.get("createdOn");
      Predicate pr = cb.lessThanOrEqualTo(expression, endDate.getTime());

      cq = addPredicate(cq, cb, pr);
    }

    return cq;
  }

  private <T> CriteriaQuery<T> applySortCriteria(CriteriaBuilder cb,
      CriteriaQuery<T> query, Root<Audit> root, List<SortDTO> sortList) {
    CriteriaQuery<T> cq = query;
    List<Order> orders = new ArrayList<>();

    if (sortList != null) {
      for (SortDTO sortDTO : sortList) {
        if (SortOperator.ASC == sortDTO.getOperator()) {
          if (AuditLogColumns.traceData.equals(sortDTO.getColumn())) {
            Expression<?> exp = root.get("traceId").get(sortDTO.getColumn().name());
            exp = cb.function("TO_CHAR", String.class, exp);
            orders.add(cb.asc(exp));
          } else {
            orders.add(cb.asc(root.get(sortDTO.getColumn().name())));
          }

        } else {
          if (AuditLogColumns.traceData.equals(sortDTO.getColumn())) {
            Expression<?> exp = root.get("traceId").get(sortDTO.getColumn().name());
            exp = cb.function("TO_CHAR", String.class, exp);
            orders.add(cb.desc(exp));
          } else {
            orders.add(cb.desc(root.get(sortDTO.getColumn().name())));
          }
        }
      }
      cq = cq.orderBy(orders);
    }

    return cq;
  }

  private <T> CriteriaQuery<T> applySearchCriteria(CriteriaBuilder cb,
      CriteriaQuery<T> query, Root<Audit> root,
      List<String> referenceIds, List<String> levelNames,
      List<String> groupNames, Date startDate, Date endDate) {
    CriteriaQuery<T> cq = query;

    if (referenceIds != null) {
      Predicate pr = root.get("referenceId").in(referenceIds);
      cq = addPredicate(cq, cb, pr);
    }
    if (levelNames != null) {
      Predicate pr = root.get("levelId").get("name").in(levelNames);
      cq = addPredicate(cq, cb, pr);
    }
    if (groupNames != null) {
      Predicate pr = root.get("groupName").in(groupNames);
      cq = addPredicate(cq, cb, pr);
    }

    if (startDate != null) {
      Expression expression = root.get("createdOn");
      cq.where(cb.greaterThanOrEqualTo(expression, startDate.getTime()));
      if (endDate != null) {
        cq.where(cb.and(cq.getRestriction(), cb.lessThanOrEqualTo(expression, endDate.getTime())));
      }
    }
    if (endDate != null) {
      Expression expression = root.get("createdOn");
      cq.where(cb.lessThanOrEqualTo(expression, endDate.getTime()));
      if (startDate != null) {
        cq.where(
            cb.and(cq.getRestriction(), cb.greaterThanOrEqualTo(expression, startDate.getTime())));
      }
    }

    return cq;
  }

  public void audit(String level, String event, String groupName,
      String description, String sessionID, Object traceData) {
    String traceDataStr = "";
    if (traceData != null) {
      try {
        traceDataStr = mapper.writeValueAsString(traceData);
      } catch (Exception e) {
        traceDataStr = e.getLocalizedMessage();
      }
    }
    audit(level, event, groupName, description,
        sessionID, traceDataStr);
  }

  public String audit(String level, String event, String groupName, String description,
      String sessionID, Object traceData, String referenceId) {
    AuditLogDTO dto = new AuditLogDTO();
    dto.setLevel(level);
    dto.setEvent(event);
    dto.setGroupName(groupName);
    dto.setShortDescription(description);
    dto.setPrinSessionId(sessionID);
    dto.setReferenceId(referenceId);
    if (auditProperties.isTraceData()) {
      String traceDataStr = "";
      if (traceData != null) {
        try {
          traceDataStr = mapper.writeValueAsString(traceData);
        } catch (Exception e) {
          traceDataStr = e.getLocalizedMessage();
        }
      }
      dto.setTraceData(traceDataStr);
    }
    return audit(dto);
  }

  public void audit(String level, String event, String groupName,
      String description, String sessionID, String traceData) {
    AuditLogDTO dto = new AuditLogDTO();
    dto.setLevel(level);
    dto.setEvent(event);
    dto.setGroupName(groupName);
    dto.setShortDescription(description);
    dto.setPrinSessionId(sessionID);
    if (auditProperties.isTraceData()) {
      dto.setTraceData(traceData);
    }
    audit(dto);
  }

  public String audit(AuditLogDTO audit) {
    LOGGER.log(Level.FINER, "Adding audit ''{0}''.", audit);
    if (audit.getCreatedOn() == null) {
      audit.setCreatedOn(new Date());
    }
    Audit alAudit = ConverterUtil.convertToAuditLogModel(audit);
    alAudit.setLevelId(AuditLevel.findByName(em, audit.getLevel()));
    if (null != alAudit.getTraceId()) {
      em.persist(alAudit.getTraceId());
    }
    em.persist(alAudit);
    return alAudit.getId();
  }

  public List<String> audits(List<AuditLogDTO> auditList, String correlationId) {
    LOGGER.log(Level.FINER, "Adding audits ''{0}''.", auditList);

    List<String> uuids = new ArrayList<>();

    for (AuditLogDTO audit : auditList) {
      if (audit.getCreatedOn() == null) {
        audit.setCreatedOn(new Date());
      }
      Audit alAudit = ConverterUtil.convertToAuditLogModel(audit);
      alAudit.setLevelId(AuditLevel.findByName(em, audit.getLevel()));
      alAudit.setCorrelationId(correlationId);
      if (null != alAudit.getTraceId()) {
        em.persist(alAudit.getTraceId());
      }
      em.persist(alAudit);
      uuids.add(alAudit.getId());
    }

    return uuids;
  }

  public void deleteAudit(String id) {
    LOGGER.log(Level.FINER, "Deleting audit ''{0}''.", id);
    em.remove(em.find(Audit.class, id));
  }

  public void truncateAudits() {
    LOGGER.log(Level.FINER, "Clearing all audit log data.");
    Query query = em.createQuery("DELETE FROM Audit a");
    query.executeUpdate();
  }

  public void truncateAudits(Date createdOn) {
    LOGGER.log(Level.FINER, "Clearing audit log data before {0}",
        createdOn.toString());
    Query query = em
        .createQuery("DELETE FROM Audit a WHERE a.createdOn < :createdOn");
    query.setParameter("createdOn", createdOn.getTime());
    query.executeUpdate();
  }

  public void truncateAudits(long retentionPeriod) {
    LOGGER.log(Level.FINER, "Clearing audit log data older than {0}",
        String.valueOf(retentionPeriod));
    Query query = em
        .createQuery("DELETE FROM Audit a WHERE a.createdOn < :createdOn");
    query.setParameter("createdOn", Calendar.getInstance()
        .getTimeInMillis() - retentionPeriod);
    query.executeUpdate();
  }

  public int countAudits(List<String> levelNames, List<String> referenceIds,
      List<String> groupNames, Date startDate, Date endDate) {
    LOGGER.log(
        Level.FINER,
        "Counting audits, levelNames count = {0}, referenceId count = {1}, groupNames count {2}, "
            + "startDate = {3} and endDate = {4}",
        new String[]{
            (levelNames != null) ? String.valueOf(levelNames.size())
                : "0",
            (referenceIds != null) ? String.valueOf(referenceIds
                .size()) : "0",
            (groupNames != null) ? String.valueOf(groupNames.size())
                : "0",
            (startDate != null) ? startDate.toString() : "NONE",
            (endDate != null) ? endDate.toString() : "NONE"});

    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<Long> cq = cb.createQuery(Long.class);
    Root<Audit> root = cq.from(Audit.class);
    cq = cq.select(cb.count(root));

    cq = applySearchCriteria(cb, cq, root, referenceIds, levelNames,
        groupNames, startDate, endDate);

    TypedQuery<Long> query = em.createQuery(cq);
    return query.getSingleResult().intValue();
  }

  public List<String> getAuditLogsColumn(List<SearchDTO> searchList, Date startDate,
      Date endDate, String column) {
    LOGGER.log(
        Level.FINER,
        "getAuditLogs for a requested column {4}, searchList count = {0}, startDate = {1} and endDate = {2}",
        new String[]{
            (searchList != null) ? String.valueOf(searchList.size())
                : "0",
            (startDate != null) ? startDate.toString() : "NONE",
            (endDate != null) ? endDate.toString() : "NONE",
            (column != null) ? column : "NONE"});

    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<String> cq = cb.createQuery(String.class);
    Root<Audit> root = cq.from(Audit.class);

    if (column == null) {
      return null;
    } else {
      cq.select(root.get(column).as(String.class));
    }

    cq = applySearchCriteria(cb, cq, root, searchList, startDate, endDate);

    TypedQuery<String> query = em.createQuery(cq);

    return query.getResultList();
  }

  public AuditLogDTO getAuditById(String auditId) {
    Audit log = em.find(Audit.class, auditId);

    return ConverterUtil.convertToAuditLogDTO(log);
  }

  public List<AuditLogDTO> listAudits(List<String> levelNames,
      List<String> referenceIds, List<String> groupNames, Date startDate,
      Date endDate, boolean isAscending, PagingParams pagingParams) {
    LOGGER.log(
        Level.FINER,
        "Listing audits audits, levelNames count = {0}, referenceId count = {1}, groupNames count {2}, "
            + "startDate = {3} and endDate = {4}",
        new String[]{
            (levelNames != null) ? String.valueOf(levelNames.size())
                : "0",
            (referenceIds != null) ? String.valueOf(referenceIds
                .size()) : "0",
            (groupNames != null) ? String.valueOf(groupNames.size())
                : "0",
            (startDate != null) ? startDate.toString() : "NONE",
            (endDate != null) ? endDate.toString() : "NONE"});

    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<Audit> cq = cb.createQuery(Audit.class);
    Root<Audit> root = cq.from(Audit.class);

    cq = applySearchCriteria(cb, cq, root, referenceIds, levelNames,
        groupNames, startDate, endDate);

    if (isAscending) {
      cq.orderBy(cb.asc(root.get("createdOn")));
    } else {
      cq.orderBy(cb.desc(root.get("createdOn")));
    }

    TypedQuery<Audit> query = em.createQuery(cq);
    if (pagingParams != null && pagingParams.getCurrentPage() > -1) {
      query.setFirstResult((pagingParams.getCurrentPage() - 1)
          * pagingParams.getPageSize());
      query.setMaxResults(pagingParams.getPageSize());
    }

    return ConverterUtil.convertToAuditLogList(query.getResultList());
  }

  public List<AuditLogDTO> listAuditLogs(List<SearchDTO> searchList,
      Date startDate, Date endDate, List<SortDTO> sortList,
      PagingParams pagingParams, boolean fetchTraceData) {
    LOGGER.log(
        Level.FINER,
        "listAuditLogs, searchList count = {0}, sortList count = {1}, startDate = {2} and endDate = {3}",
        new String[]{
            (searchList != null) ? String.valueOf(searchList.size())
                : "0",
            (sortList != null) ? String.valueOf(sortList.size())
                : "0",
            (startDate != null) ? startDate.toString() : "NONE",
            (endDate != null) ? endDate.toString() : "NONE"});

    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<Audit> cq = cb.createQuery(Audit.class);
    Root<Audit> root = cq.from(Audit.class);

    cq = applySearchCriteria(cb, cq, root, searchList, startDate, endDate);

    cq = applySortCriteria(cb, cq, root, sortList);
    if (fetchTraceData) {
      root.fetch("traceId", JoinType.LEFT);
    }

    TypedQuery<Audit> query = em.createQuery(cq);
    if (pagingParams != null && pagingParams.getCurrentPage() > -1) {
      query.setFirstResult((pagingParams.getCurrentPage() - 1)
          * pagingParams.getPageSize());
      query.setMaxResults(pagingParams.getPageSize());
    }

    return ConverterUtil.convertToAuditLogList(query.getResultList(), fetchTraceData);
  }

  public List<AuditLogDTO> listAuditLogs(List<SearchDTO> searchList, Date startDate, Date endDate,
      List<SortDTO> sortList, PagingParams pagingParams) {
    return listAuditLogs(searchList, startDate, endDate, sortList, pagingParams, true);
  }

  public int countAuditLogs(List<SearchDTO> searchList, Date startDate,
      Date endDate) {
    LOGGER.log(
        Level.FINER,
        "countAuditLogs, searchList count = {0}, startDate = {1} and endDate = {2}",
        new String[]{
            (searchList != null) ? String.valueOf(searchList.size())
                : "0",
            (startDate != null) ? startDate.toString() : "NONE",
            (endDate != null) ? endDate.toString() : "NONE"});

    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<Long> cq = cb.createQuery(Long.class);
    Root<Audit> root = cq.from(Audit.class);
    cq = cq.select(cb.count(root));
    cq = applySearchCriteria(cb, cq, root, searchList, startDate, endDate);

    TypedQuery<Long> query = em.createQuery(cq);

    return query.getSingleResult().intValue();
  }

}