package com.eurodyn.qlack.fuse.lexicon.repository;

import java.util.List;

import com.eurodyn.qlack.fuse.lexicon.model.Language;

public interface LanguageRepository extends LexiconRepository<Language, String> {

	Language findByLocale(String locale);
	List<Language> findbyActiveTrueOrderByNameAsc();

}
