package com.eurodyn.qlack.fuse.audit.repository;

import com.eurodyn.qlack.fuse.audit.model.Audit;
import com.eurodyn.qlack.fuse.audit.model.QAudit;
import com.eurodyn.qlack.util.querydsl.GenericQuerydslBinder;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditRepository extends PagingAndSortingRepository<Audit, String>,
    QuerydslPredicateExecutor<Audit>, GenericQuerydslBinder<QAudit> {

  @Override
  default void customize(QuerydslBindings bindings, QAudit audit) {
    // Add generic bindings.
    GenericQuerydslBinder.super.addGenericBindings(bindings, audit);

    // Exclude fields from filter.
    bindings.excluding(audit.shortDescription);
  }
}
