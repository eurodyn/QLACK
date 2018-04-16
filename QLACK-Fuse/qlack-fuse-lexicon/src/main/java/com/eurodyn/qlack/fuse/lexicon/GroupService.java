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
import com.eurodyn.qlack.fuse.lexicon.repository.GroupRepository;
import com.eurodyn.qlack.fuse.lexicon.util.ConverterUtil;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Transactional
@Service
@Validated
public class GroupService {

  @PersistenceContext
  private EntityManager em;

  // Querydsl fields.
  private QKey qKey = QKey.key;
  private QData qData = QData.data;
  private QLanguage qLanguage = QLanguage.language;
  private QGroup qGroup = QGroup.group;

  // Service references.
  private GroupRepository groupRepository;
  private GroupMapper groupMapper;

  @Autowired
  public GroupService(GroupRepository groupRepository,
      GroupMapper groupMapper) {
    this.groupRepository = groupRepository;
    this.groupMapper = groupMapper;
  }

  public String createGroup(GroupDTO group) {
    Group entity = ConverterUtil.groupDTOToGroup(group);
    em.persist(entity);
    return entity.getId();
  }

  public void updateGroup(GroupDTO group) {
    Group entity = Group.find(group.getId(), em);
    entity.setTitle(group.getTitle());
    entity.setDescription(group.getDescription());
  }

  public void deleteGroup(String groupID) {
    em.remove(Group.find(groupID, em));
  }

  public GroupDTO getGroup(String groupID) {
    return ConverterUtil.groupToGroupDTO(Group.find(groupID, em));
  }

  public GroupDTO getGroupByName(String groupName) {
    return groupMapper.toGroupDTO(groupRepository.findByTitle(groupName));
    //return ConverterUtil.groupToGroupDTO(Group.findByTitle(groupName, em));
  }

  public List<Group> findAll() {
    List<Group> groups = new ArrayList<>();
    groupRepository.findAll().forEach(groups::add);

    return groups;
  }

  public Group findByTitle(String title) {
    return groupRepository.findByTitle(title);
  }

  public Set<GroupDTO> getRemainingGroups(List<String> excludedGroupNames) {
    List<Group> groups = new JPAQueryFactory(em).selectFrom(qGroup)
        .where(qGroup.title.notIn(excludedGroupNames)).fetch();
    return ConverterUtil.groupToGroupDTOSet(groups);
  }

  public Set<GroupDTO> getGroups() {
    return groupMapper.toGroupDTOSet(groupRepository.findAll());
  }

  public void deleteLanguageTranslations(String groupID, String languageID) {
    Language language = Language.find(languageID, em);

    List<Data> dataList = Data.findByGroupIDAndLocale(groupID, language.getLocale(), em);
    for (Data data : dataList) {
      em.remove(data);
    }
  }

  public void deleteLanguageTranslationsByLocale(String groupID, String locale) {
    List<Data> dataList = Data.findByGroupIDAndLocale(groupID, locale, em);
    for (Data data : dataList) {
      em.remove(data);
    }
  }

  public long getLastUpdateDateForLocale(String groupID, String locale) {
    // The default return value is 'now'.
    long retVal = Instant.now().toEpochMilli();

    // Find when was the last update of any keys on the requested group and
    // locale.
    Data data = new JPAQueryFactory(em).selectFrom(qData)
        .innerJoin(qData.key, qKey)
        .where(qKey.group.id.eq(groupID),
            qData.language.id.eq(
                JPAExpressions.select(qLanguage.id).from(qLanguage)
                    .where(qLanguage.locale.eq(locale))
            )
        ).orderBy(qData.lastUpdatedOn.desc())
        .fetchFirst();

    if (data != null) {
      retVal = data.getLastUpdatedOn();
    }

    return retVal;
  }

}
