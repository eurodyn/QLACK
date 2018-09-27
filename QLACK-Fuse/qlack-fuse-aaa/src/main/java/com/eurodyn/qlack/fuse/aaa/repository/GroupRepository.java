package com.eurodyn.qlack.fuse.aaa.repository;

import com.eurodyn.qlack.fuse.aaa.model.Group;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface GroupRepository extends AAARepository<Group, String> {

  Group findByName(String name);

  Group findByObjectId(String objectId);

  default Set<String> getAllIds(){

    return findAll().stream()
        .map(Group::getId)
        .collect(Collectors.toSet());
  }

}
