package com.eurodyn.qlack.fuse.aaa.repository;

import com.eurodyn.qlack.fuse.aaa.model.OpTemplate;

public interface OpTemplateRepository extends AAARepository<OpTemplate, String> {

  OpTemplate findByName(String name);

}
