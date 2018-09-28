package com.eurodyn.qlack.fuse.lexicon.repository;

import java.util.List;

import com.eurodyn.qlack.fuse.lexicon.model.Data;

public interface DataRepository extends LexiconRepository<Data, String> {

	Data findByKeyAndLanguageId(String keyId, String languageId);

	Data findByKeyNameAndLanguageId(String keyName, String languageId);

	Data findByKeyIdAndLocale(String keyId, String locale);

	Data findByKeyNameAndLocale(String keyName, String locale);

	List<Data> findByGroupIDAndLocale(String groupId, String locale);

}
