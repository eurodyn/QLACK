package com.eurodyn.qlack.fuse.lexicon.repository;

import com.eurodyn.qlack.fuse.lexicon.model.Key;

public interface KeyRepository extends LexiconRepository<Key, String> {

	Key findByNameAndGroupId(String keyName, String groupId);
}
