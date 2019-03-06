package com.eurodyn.qlack.fuse.audit.repository;

import com.eurodyn.qlack.common.exception.QDoesNotExistException;
import com.eurodyn.qlack.fuse.audit.model.AuditBaseEntity;
import com.querydsl.core.types.Predicate;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.lang.NonNull;

public interface AuditBaseRepository<E extends AuditBaseEntity, I extends Serializable> extends
    JpaRepository<E, I>, QuerydslPredicateExecutor<E> {

  @Override
  @NonNull
  List<E> findAll(@NonNull Predicate predicate);

  @Override
  @NonNull
  List<E> findAll(@NonNull Predicate predicate, Sort sort);

  default E fetchById(I id) {
    if (id == null) {
      throw new IllegalArgumentException("Null id");
    }
    Optional<E> optional = findById(id);

    return optional.orElseThrow(
        () -> new QDoesNotExistException(MessageFormat
            .format("Entity with Id {0} could not be found.", id)));
  }
}
