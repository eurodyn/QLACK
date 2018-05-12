package com.eurodyn.qlack.fuse.audit.repository;

import com.eurodyn.qlack.fuse.audit.model.Audit;
import com.eurodyn.qlack.fuse.audit.model.QAudit;
import com.eurodyn.qlack.util.querydsl.GenericQuerydslBinder;
import com.querydsl.core.types.dsl.NumberPath;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
public interface AuditRepository extends PagingAndSortingRepository<Audit, String>,
    QuerydslPredicateExecutor<Audit>, GenericQuerydslBinder<QAudit>, AuditRepositoryExt {

  @Override
  default void customize(QuerydslBindings bindings, QAudit audit) {
    // Add generic bindings.
    GenericQuerydslBinder.super.addGenericBindings(bindings);

    // Add specific bindings.
    bindings.bind(audit.createdOn).all((final NumberPath<Long> path, final Collection<? extends Long> values) -> {
      final List<? extends Long> dates = new ArrayList<>(values);
      Collections.sort(dates);
      if (dates.size() == 2) {
        return Optional.of(path.between(dates.get(0), dates.get(1)));
      } else {
        return Optional.of(path.eq(dates.get(0)));
      }
    });

    // Exclude fields from filter.
    bindings.excluding(audit.shortDescription);
  }
}
