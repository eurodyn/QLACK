package com.eurodyn.qlack.fuse.audit.repository;

import com.eurodyn.qlack.fuse.audit.model.AuditLevel;

public interface AuditLevelRepository extends AuditBaseRepository<AuditLevel, String> {

  AuditLevel findByName(String name);
}
