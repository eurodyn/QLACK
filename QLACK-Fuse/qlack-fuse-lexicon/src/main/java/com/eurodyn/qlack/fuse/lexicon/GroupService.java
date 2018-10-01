package com.eurodyn.qlack.fuse.lexicon;

import com.eurodyn.qlack.fuse.lexicon.dto.GroupDTO;
import com.eurodyn.qlack.fuse.lexicon.mappers.GroupMapper;
import com.eurodyn.qlack.fuse.lexicon.model.Data;
import com.eurodyn.qlack.fuse.lexicon.model.Group;
import com.eurodyn.qlack.fuse.lexicon.model.Language;
import com.eurodyn.qlack.fuse.lexicon.model.QData;
import com.eurodyn.qlack.fuse.lexicon.model.QGroup;
import com.eurodyn.qlack.fuse.lexicon.model.QKey;
import com.eurodyn.qlack.fuse.lexicon.model.QLanguage;
import com.eurodyn.qlack.fuse.lexicon.repository.DataRepository;
import com.eurodyn.qlack.fuse.lexicon.repository.GroupRepository;
import com.eurodyn.qlack.fuse.lexicon.repository.LanguageRepository;
//import com.eurodyn.qlack.fuse.lexicon.util.ConverterUtil;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Transactional
@Service
@Validated
public class GroupService {

	private final GroupRepository groupRepository;
	private final LanguageRepository languageRepository;
	private final DataRepository dataRepository; 
	private final GroupMapper groupMapper;
		
	
	// @PersistenceContext
	// private EntityManager em;

	// Querydsl fields.
	private QGroup qGroup = QGroup.group;
	private QKey qKey = QKey.key;
	private QData qData = QData.data;
	private QLanguage qLanguage = QLanguage.language;
	// Service references.
	// private GroupRepository groupRepository;

	@Autowired
	public GroupService(GroupRepository groupRepository, GroupMapper groupMapper, LanguageRepository languageRepository, DataRepository dataRepository) {
		this.groupRepository = groupRepository;
		this.groupMapper = groupMapper;
		this.languageRepository = languageRepository; 
		this.dataRepository = dataRepository;
	}

	public String createGroup(GroupDTO group) {
		Group entity = groupMapper.mapToEntity(group); 
				//ConverterUtil.groupDTOToGroup(group);
//		em.persist(entity);
		groupRepository.save(entity);
		return entity.getId();
	}

	public void updateGroup(GroupDTO group) {
		Group entity = groupRepository.fetchById(group.getId());
		entity.setTitle(group.getTitle());
		entity.setDescription(group.getDescription());
		groupRepository.save(entity);
	}

	public void deleteGroup(String groupID) {
//		em.remove(Group.find(groupID, em));
		groupRepository.deleteById(groupID);
	}

	public GroupDTO getGroup(String groupID) {
		
		return groupMapper.mapToDTO(groupRepository.fetchById(groupID));
//		return ConverterUtil.groupToGroupDTO(Group.find(groupID, em));
	}
	

	public GroupDTO getGroupByName(String groupName) {

		return groupMapper.mapToDTO(groupRepository.findByTitle(groupName));
		// return ConverterUtil.groupToGroupDTO(Group.findByTitle(groupName, em));
	}

	public List<Group> findAll() {
		List<Group> groups = new ArrayList<>();
		groupRepository.findAll().forEach(groups::add);
		return groups;
	}

	public Group findByTitle(String title) {
		return groupRepository.findByTitle(title);
	}

	
//	@Query("select User.id from Group where User.superadmin = :superadmin")
	public Set<GroupDTO> getRemainingGroups(List<String> excludedGroupNames) {
//		List<Group> groups =new JPAQueryFactory(em).selectFrom(qGroup).where(qGroup.title.notIn(excludedGroupNames)).fetch();

		Predicate predicate = qGroup.title.notIn(excludedGroupNames);
		
		//return ConverterUtil.groupToGroupDTOSet(groups);
		return groupMapper.mapToDTO(groupRepository.findAll(predicate)).stream().collect(Collectors.toSet());
	}

	public Set<GroupDTO> getGroups() {
		return new HashSet<GroupDTO>(groupMapper.mapToDTO(groupRepository.findAll()));
	}

	public void deleteLanguageTranslations(String groupID, String languageID) {
		Language language = languageRepository.fetchById(languageID);
		// Language.find(languageID, em);
		List<Data> dataList = dataRepository.findByKeyGroupIdAndLanguageLocale(groupID, language.getLocale());
		// Data.findByGroupIDAndLocale(groupID,, em);
		for (Data data : dataList) {
//			em.remove(data);
			dataRepository.delete(data);
		}
	}

	public void deleteLanguageTranslationsByLocale(String groupID, String locale) {
		List<Data> dataList = dataRepository.findByKeyGroupIdAndLanguageLocale(groupID, locale);
		for (Data data : dataList) {
			dataRepository.delete(data);
			//em.remove(data);
		}
	}

	public long getLastUpdateDateForLocale(String groupID, String locale) {
		// The default return value is 'now'.
		long retVal = Instant.now().toEpochMilli();

		// Find when was the last update of any keys on the requested group and
		// locale.
		Predicate predicate = qData.key.group.id.eq(groupID).and(
				qData.language.id.eq(JPAExpressions.select(qLanguage.id).from(qLanguage).where(qLanguage.locale.eq(locale))));	
//		OrderSpecifier<Long> sortOrder = qData.lastUpdatedOn.desc();
		Data data = dataRepository.findAll(predicate,Sort.by("lastUpdatedOn").descending()).iterator().next();

//		VERIFY 
//		Data data = new JPAQueryFactory(em).selectFrom(qData).innerJoin(qData.key, qKey)
//				.where(  qKey.group.id.eq(groupID),  qData.language.id.eq(JPAExpressions.select(qLanguage.id).from(qLanguage).where(qLanguage.locale.eq(locale)))
//						).orderBy(qData.lastUpdatedOn.desc()).fetchFirst();

		if (data != null) {
			retVal = data.getLastUpdatedOn();
		}

		return retVal;
	}

}
