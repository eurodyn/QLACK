package com.eurodyn.qlack.fuse.audit.service;

import com.eurodyn.qlack.fuse.audit.dto.AuditLogDTO;
import com.eurodyn.qlack.fuse.audit.mappers.AuditMapper;
import com.eurodyn.qlack.fuse.audit.model.Audit;
import com.eurodyn.qlack.fuse.audit.model.AuditLevel;
import com.eurodyn.qlack.fuse.audit.repository.AuditRepository;
import com.eurodyn.qlack.fuse.audit.util.AuditProperties;
import com.eurodyn.qlack.fuse.audit.util.ConverterUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.core.types.Predicate;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

  // Service references.
  private AuditProperties auditProperties;
  private AuditRepository auditRepository;
  private AuditMapper auditMapper;

  @Autowired
  public AuditService(AuditProperties auditProperties,
      AuditRepository auditRepository, AuditMapper auditMapper) {
    this.auditProperties = auditProperties;
    this.auditRepository = auditRepository;
    this.auditMapper = auditMapper;
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

  public AuditLogDTO getAuditById(String auditId) {
    Audit log = em.find(Audit.class, auditId);

    return ConverterUtil.convertToAuditLogDTO(log);
  }

  public Page<AuditLogDTO> getAuditLogs(Pageable pageable, Predicate predicate) {
    return auditMapper.toAuditLogDTO(auditRepository.findAll(predicate, pageable));
  }

  public List<String> getDistinctEventsForReferenceId(String referenceId) {
    return auditRepository.findDistinctEventsByReferenceId(referenceId);
  }
}
