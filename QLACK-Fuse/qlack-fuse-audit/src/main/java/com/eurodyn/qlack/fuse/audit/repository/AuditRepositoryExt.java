package com.eurodyn.qlack.fuse.audit.repository;

import java.util.List;

public interface AuditRepositoryExt {
  List<String> findDistinctEventsByReferenceId(String referenceId);
}
