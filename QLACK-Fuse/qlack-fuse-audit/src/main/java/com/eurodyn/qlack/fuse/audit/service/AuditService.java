package com.eurodyn.qlack.fuse.audit.service;

import com.eurodyn.qlack.fuse.audit.dto.AuditDTO;
import com.eurodyn.qlack.fuse.audit.dto.AuditTraceDTO;
import com.eurodyn.qlack.fuse.audit.mappers.AuditMapper;
import com.eurodyn.qlack.fuse.audit.model.Audit;
import com.eurodyn.qlack.fuse.audit.repository.AuditLevelRepository;
import com.eurodyn.qlack.fuse.audit.repository.AuditRepository;
import com.eurodyn.qlack.fuse.audit.repository.AuditTraceRepository;
import com.eurodyn.qlack.fuse.audit.util.AuditProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.core.types.Predicate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Transactional
@Validated
public class AuditService {

  private static final Logger LOGGER = Logger
      .getLogger(AuditService.class.getSimpleName());

  private static final ObjectMapper mapper = new ObjectMapper();

//  @PersistenceContext
//  private EntityManager em;

  // Service references.
  private AuditProperties auditProperties;
  private AuditRepository auditRepository;
  private final AuditLevelRepository auditLevelRepository;
  private final AuditTraceRepository auditTraceRepository;
  private final AuditMapper auditMapper;

  @Autowired
  public AuditService(AuditProperties auditProperties,
      AuditRepository auditRepository, AuditMapper auditMapper,
      AuditLevelRepository auditLevelRepository,
      AuditTraceRepository auditTraceRepository) {
    this.auditProperties = auditProperties;
    this.auditRepository = auditRepository;
    this.auditMapper = auditMapper;
    this.auditLevelRepository = auditLevelRepository;
    this.auditTraceRepository = auditTraceRepository;
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
    AuditDTO dto = new AuditDTO();
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
      dto.setTrace(new AuditTraceDTO(traceDataStr));
    }
    return audit(dto);
  }

  public void audit(String level, String event, String groupName,
      String description, String sessionID, String traceData) {
    AuditDTO dto = new AuditDTO();
    dto.setLevel(level);
    dto.setEvent(event);
    dto.setGroupName(groupName);
    dto.setShortDescription(description);
    dto.setPrinSessionId(sessionID);
    if (auditProperties.isTraceData()) {
      dto.setTrace(new AuditTraceDTO(traceData));
    }
    audit(dto);
  }

  public String audit(AuditDTO audit) {
    LOGGER.log(Level.FINER, "Adding audit ''{0}''.", audit);
    if (audit.getCreatedOn() == null) {
      audit.setCreatedOn(Calendar.getInstance().getTimeInMillis());
    }
//    Audit alAudit = ConverterUtil.convertToAuditLogModel(audit);
    Audit alAudit = auditMapper.mapToEntity(audit);
//    alAudit.setLevelId(AuditLevel.findByName(em, audit.getLevel()));
    alAudit.setLevelId(auditLevelRepository.findByName(audit.getLevel()));
    if (null != alAudit.getTrace()) {
//      em.persist(alAudit.getTrace());
      auditTraceRepository.save(alAudit.getTrace());
    }
//    em.persist(alAudit);
    if(alAudit.getCreatedOn() == null){
      alAudit.setCreatedOn(Calendar.getInstance().getTimeInMillis());
    }
    auditRepository.save(alAudit);
    return alAudit.getId();
  }

  public List<String> audits(List<AuditDTO> auditList, String correlationId) {
    LOGGER.log(Level.FINER, "Adding audits ''{0}''.", auditList);

    List<String> uuids = new ArrayList<>();

    for (AuditDTO audit : auditList) {
      if (audit.getCreatedOn() == null) {
        audit.setCreatedOn(Calendar.getInstance().getTimeInMillis());
      }
//      Audit alAudit = ConverterUtil.convertToAuditLogModel(audit);
      Audit alAudit = auditMapper.mapToEntity(audit);
//      alAudit.setLevelId(AuditLevel.findByName(em, audit.getLevel()));
      alAudit.setLevelId(auditLevelRepository.findByName(audit.getLevel()));
      alAudit.setCorrelationId(correlationId);
      if (null != alAudit.getTrace()) {
//        em.persist(alAudit.getTrace());
        auditTraceRepository.save(alAudit.getTrace());
      }
//      em.persist(alAudit);
      if(alAudit.getCreatedOn() == null){
        alAudit.setCreatedOn(Calendar.getInstance().getTimeInMillis());
      }
      auditRepository.save(alAudit);
      uuids.add(alAudit.getId());
    }

    return uuids;
  }

  public void deleteAudit(String id) {
    LOGGER.log(Level.FINER, "Deleting audit ''{0}''.", id);
//    em.remove(em.find(Audit.class, id));
    auditRepository.delete(auditRepository.fetchById(id));
  }

  public void truncateAudits() {
    LOGGER.log(Level.FINER, "Clearing all audit log data.");
//    Query query = em.createQuery("DELETE FROM Audit a");
//    query.executeUpdate();
    auditRepository.deleteAll();
  }

  public void truncateAudits(Date createdOn) {
    LOGGER.log(Level.FINER, "Clearing audit log data before {0}",
        createdOn);
//    Query query = em
//        .createQuery("DELETE FROM Audit a WHERE a.createdOn < :createdOn");
//    query.setParameter("createdOn", createdOn.getTime());
//    query.executeUpdate();
    auditRepository.deleteByCreatedOnBefore(createdOn.toInstant().toEpochMilli());
  }

  public void truncateAudits(long retentionPeriod) {
    LOGGER.log(Level.FINER, "Clearing audit log data older than {0}",
        String.valueOf(retentionPeriod));
//    Query query = em
//        .createQuery("DELETE FROM Audit a WHERE a.createdOn < :createdOn");
//    query.setParameter("createdOn", Calendar.getInstance()
//        .getTimeInMillis() - retentionPeriod);
//    query.executeUpdate();
    auditRepository.deleteByCreatedOnBefore(Calendar.getInstance()
        .getTimeInMillis() - retentionPeriod);
  }

  public AuditDTO getAuditById(String auditId) {
//    Audit log = em.find(Audit.class, auditId);
    Audit log  = auditRepository.fetchById(auditId);

//    return ConverterUtil.convertToAuditDTO(log);
    return auditMapper.mapToDTO(log);
  }

  public Page<AuditDTO> getAuditLogs(Pageable pageable, Predicate predicate) {
    return auditMapper.toAuditDTO(auditRepository.findAll(predicate, pageable));
  }

  public List<String> getDistinctEventsForReferenceId(String referenceId) {
    return auditRepository.findDistinctEventsByReferenceId(referenceId);
  }
}
