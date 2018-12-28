package com.eurodyn.qlack.fuse.audit.repository;

import com.eurodyn.qlack.fuse.audit.model.AuditTrace;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditTraceRepository extends AuditBaseRepository<AuditTrace, String> {

}
