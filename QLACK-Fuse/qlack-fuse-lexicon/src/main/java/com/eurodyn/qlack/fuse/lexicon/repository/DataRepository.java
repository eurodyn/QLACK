package com.eurodyn.qlack.fuse.lexicon.repository;

import java.util.List;

import com.eurodyn.qlack.fuse.lexicon.model.Data;

public interface DataRepository extends LexiconRepository<Data, String> {

	Data findByKeyIdAndLanguageId(String keyId, String languageId);

	Data findByKeyNameAndLanguageId(String keyName, String languageId);

	Data findByKeyIdAndLanguageLocale(String keyId, String locale);

	Data findByKeyNameAndLanguageLocale(String keyName, String locale);

	List<Data> findByKeyGroupIdAndLanguageLocale(String groupId, String locale);

}
