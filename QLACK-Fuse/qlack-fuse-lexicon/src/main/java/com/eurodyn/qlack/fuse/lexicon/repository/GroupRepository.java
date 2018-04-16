package com.eurodyn.qlack.fuse.lexicon.repository;

import com.eurodyn.qlack.fuse.lexicon.model.Group;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends CrudRepository<Group, String> {
  Group findByTitle(String title);
}
