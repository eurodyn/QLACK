package com.eurodyn.qlack.fuse.lexicon.repository;

import java.util.List;

import com.eurodyn.qlack.fuse.lexicon.model.Template;

public interface TemplateRepository extends LexiconRepository<Template, String> {

	List<Template> findByName(String name);
	
	Template findByNameAndLocale(String templateName, String locale);
}
