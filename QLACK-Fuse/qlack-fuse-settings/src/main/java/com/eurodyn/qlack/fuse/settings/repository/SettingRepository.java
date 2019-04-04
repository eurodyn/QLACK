package com.eurodyn.qlack.fuse.settings.repository;

import com.eurodyn.qlack.fuse.settings.model.Setting;
import com.querydsl.core.types.Predicate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.lang.NonNull;

public interface SettingRepository extends JpaRepository<Setting, String>, QuerydslPredicateExecutor<Setting> {

    @Override
    @NonNull
    List<Setting> findAll(@NonNull Predicate predicate);
}
