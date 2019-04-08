package com.eurodyn.qlack.fuse.rules.repository;

import com.eurodyn.qlack.common.exception.QDoesNotExistException;
import com.eurodyn.qlack.fuse.rules.model.RulesModel;
import com.querydsl.core.types.Predicate;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.lang.NonNull;

/**
 * @author European Dynamics
 */
public interface RulesRepository<T extends RulesModel, I extends Serializable>
    extends JpaRepository<T, I>, QuerydslPredicateExecutor<T> {

    @NonNull
    List<T> findAll(@NonNull Predicate predicate);

    @NonNull
    List<T> findAll(@NonNull Predicate predicate, @NonNull Sort sort);

    default T fetchById(I id) {
        if (id == null) {
            throw new IllegalArgumentException("Null id");
        }
        Optional<T> optional = findById(id);

        return optional.orElseThrow(
            () -> new QDoesNotExistException(MessageFormat
                .format("Entity with Id {0} could not be found.", id)));
    }
}
