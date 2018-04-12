package com.eurodyn.qlack.fuse.lexicon;


import com.eurodyn.qlack.fuse.lexicon.dto.QFLGroupDTO;
import com.eurodyn.qlack.fuse.lexicon.model.QFLData;
import com.eurodyn.qlack.fuse.lexicon.model.QFLGroup;
import com.eurodyn.qlack.fuse.lexicon.model.QFLLanguage;
import com.eurodyn.qlack.fuse.lexicon.model.QQFLData;
import com.eurodyn.qlack.fuse.lexicon.model.QQFLGroup;
import com.eurodyn.qlack.fuse.lexicon.model.QQFLKey;
import com.eurodyn.qlack.fuse.lexicon.model.QQFLLanguage;
import com.eurodyn.qlack.fuse.lexicon.util.QFLConverterUtil;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.Instant;
import java.util.List;
import java.util.Set;

@Transactional
@Service
@Validated
public class QFLGroupService {

  @PersistenceContext
  private EntityManager em;

  // Querydsl fields.
  private QQFLKey qKey = QQFLKey.qFLKey;
  private QQFLData qData = QQFLData.qFLData;
  private QQFLLanguage qLanguage = QQFLLanguage.qFLLanguage;
  private QQFLGroup qGroup = QQFLGroup.qFLGroup;

  public String createGroup(QFLGroupDTO group) {
    QFLGroup entity = QFLConverterUtil.groupDTOToGroup(group);
    em.persist(entity);
    return entity.getId();
  }

  public void updateGroup(QFLGroupDTO group) {
    QFLGroup entity = QFLGroup.find(group.getId(), em);
    entity.setTitle(group.getTitle());
    entity.setDescription(group.getDescription());
  }

  public void deleteGroup(String groupID) {
    em.remove(QFLGroup.find(groupID, em));
  }

  public QFLGroupDTO getGroup(String groupID) {
    return QFLConverterUtil.groupToGroupDTO(QFLGroup.find(groupID, em));
  }

  public QFLGroupDTO getGroupByName(String groupName) {
    return QFLConverterUtil.groupToGroupDTO(QFLGroup.findByName(groupName, em));
  }

  public Set<QFLGroupDTO> getRemainingGroups(List<String> excludedGroupNames) {
    List<QFLGroup> groups = new JPAQueryFactory(em).selectFrom(qGroup)
        .where(qGroup.title.notIn(excludedGroupNames)).fetch();
    return QFLConverterUtil.groupToGroupDTOSet(groups);
  }

  public Set<QFLGroupDTO> getGroups() {
    Query q = em.createQuery("SELECT g FROM QFLGroup g");
    return QFLConverterUtil.groupToGroupDTOSet(q.getResultList());
  }

  public void deleteLanguageTranslations(String groupID, String languageID) {
    QFLLanguage language = QFLLanguage.find(languageID, em);

    List<QFLData> dataList = QFLData.findByGroupIDAndLocale(groupID, language.getLocale(), em);
    for (QFLData data : dataList) {
      em.remove(data);
    }
  }

  public void deleteLanguageTranslationsByLocale(String groupID, String locale) {
    List<QFLData> dataList = QFLData.findByGroupIDAndLocale(groupID, locale, em);
    for (QFLData data : dataList) {
      em.remove(data);
    }
  }

  public long getLastUpdateDateForLocale(String groupID, String locale) {
    // The default return value is 'now'.
    long retVal = Instant.now().toEpochMilli();

    // Find when was the last update of any keys on the requested group and
    // locale.
    QFLData data = new JPAQueryFactory(em).selectFrom(qData)
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
