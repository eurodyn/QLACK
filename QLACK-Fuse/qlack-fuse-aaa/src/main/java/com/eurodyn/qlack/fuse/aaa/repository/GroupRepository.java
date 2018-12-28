package com.eurodyn.qlack.fuse.aaa.repository;

import com.eurodyn.qlack.fuse.aaa.model.Group;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.stream.Collectors;

@Repository
public interface GroupRepository extends AAARepository<Group, String> {

  Group findByName(String name);

  Group findByObjectId(String objectId);

  default Set<String> getAllIds(){

    return findAll().stream()
        .map(Group::getId)
        .collect(Collectors.toSet());
  }

}
