package com.eurodyn.qlack.fuse.audit.service;

import com.eurodyn.qlack.fuse.audit.dto.AuditDTO;
import com.eurodyn.qlack.fuse.audit.mappers.AuditMapper;
import com.eurodyn.qlack.fuse.audit.repository.AuditLevelRepository;
import com.eurodyn.qlack.fuse.audit.repository.AuditRepository;
import com.eurodyn.qlack.fuse.audit.util.AuditProperties;
import java.util.Date;
import javax.transaction.Transactional;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@Transactional
public class AuditAsyncService extends AuditService {

  public AuditAsyncService(AuditProperties auditProperties,
      AuditRepository auditRepository,
      AuditMapper auditMapper,
      AuditLevelRepository auditLevelRepository) {
    super(auditProperties, auditRepository, auditMapper, auditLevelRepository);
  }

  @Override
  @Async
  public void audit(String level, String event, String groupName, String description, String sessionID,
      Object traceData) {
    super.audit(level, event, groupName, description, sessionID, traceData);
  }

  @Override
  @Async
  public String audit(String level, String event, String groupName, String description, String sessionID,
      Object traceData, String referenceId) {
    return super.audit(level, event, groupName, description, sessionID, traceData, referenceId);
  }

  @Override
  @Async
  public void audit(String level, String event, String groupName, String description, String sessionID,
      String traceData) {
    super.audit(level, event, groupName, description, sessionID, traceData);
  }

  @Override
  @Async
  public String audit(AuditDTO audit) {
    return super.audit(audit);
  }

  @Override
  @Async
  public void deleteAudit(String id) {
    super.deleteAudit(id);
  }

  @Override
  @Async
  public void truncateAudits() {
    super.truncateAudits();
  }

  @Override
  @Async
  public void truncateAudits(Date createdOn) {
    super.truncateAudits(createdOn);
  }

  @Override
  @Async
  public void truncateAudits(long retentionPeriod) {
    super.truncateAudits(retentionPeriod);
  }
}
