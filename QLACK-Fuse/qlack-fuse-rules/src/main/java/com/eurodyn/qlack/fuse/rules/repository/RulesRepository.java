package com.eurodyn.qlack.fuse.rules.repository;

import com.eurodyn.qlack.common.exceptions.QDoesNotExistException;
import com.eurodyn.qlack.fuse.rules.model.RuntimeBaseState;
import com.querydsl.core.types.Predicate;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.lang.NonNull;

/**
 * @author European Dynamics
 */
    public interface RulesRepository<T extends Serializable, I> extends JpaRepository<T, I>,
        QuerydslPredicateExecutor<T> {

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
                () -> new QDoesNotExistException(MessageFormat.format("Entity with Id {0} could not be found.", id)));
        }


    public static RuntimeBaseState findById(EntityManager em, String id) {
        String jpql =
            "SELECT b " +
                "FROM RuntimeBaseState b " +
                "LEFT JOIN FETCH b.libraries " +
                "WHERE b.id = :id";

        try {
            return em.createQuery(jpql, RuntimeBaseState.class).setParameter("id", id).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
    }
