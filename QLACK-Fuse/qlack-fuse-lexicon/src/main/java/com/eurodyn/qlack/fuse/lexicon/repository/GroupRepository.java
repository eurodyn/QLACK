package com.eurodyn.qlack.fuse.lexicon.repository;

import com.eurodyn.qlack.fuse.lexicon.model.Group;

public interface GroupRepository extends LexiconRepository<Group, String> {

	Group findByTitle(String title);
//	Set<GroupDTO> getRemainingGroups (List<String> excludedGroupNames);
//	Set<GroupDTO> findByReimaing
	
//	Group findBy
	


	
	
//	 JPAQueryFactory(em).selectFrom(qData).innerJoin(qData.key, qKey).where(qKey.group.id.eq(groupID),qData.language.id.eq(JPAExpressions.select(qLanguage.id).from(qLanguage).where(qLanguage.locale.eq(locale))))
//		.orderBy(qData.lastUpdatedOn.desc()).fetchFirst();
//	getLastUpdateDateForLocale
	
}

// @Repository
// public interface GroupRepository extends CrudRepository<Group, String> {
// Group findByTitle(String title);
// }
