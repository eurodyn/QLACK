package com.eurodyn.qlack.fuse.rules.repository;

import com.eurodyn.qlack.fuse.rules.model.KnowledgeBase;
import org.springframework.stereotype.Repository;

@Repository
public interface KnowledgeBaseRepository extends RulesRepository<KnowledgeBase, String> {

}
