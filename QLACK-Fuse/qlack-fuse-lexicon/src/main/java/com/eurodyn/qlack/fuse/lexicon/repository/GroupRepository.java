package com.eurodyn.qlack.fuse.lexicon.repository;

import com.eurodyn.qlack.fuse.lexicon.model.Group;

public interface GroupRepository extends LexiconRepository<Group, String> {

	Group findByTitle(String title);
}
