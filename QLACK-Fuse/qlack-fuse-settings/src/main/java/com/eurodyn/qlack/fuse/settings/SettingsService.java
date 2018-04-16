package com.eurodyn.qlack.fuse.settings;

import com.eurodyn.qlack.common.exceptions.QAlreadyExistsException;
import com.eurodyn.qlack.common.exceptions.QDoesNotExistException;
import com.eurodyn.qlack.fuse.settings.dto.QFSGroupDTO;
import com.eurodyn.qlack.fuse.settings.dto.QFSSettingDTO;
import com.eurodyn.qlack.fuse.settings.mappers.QFSSettingMapper;
import com.eurodyn.qlack.fuse.settings.model.QFSSetting;
import com.eurodyn.qlack.fuse.settings.model.QQFSSetting;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.text.MessageFormat;
import java.util.List;
import java.util.logging.Logger;

@Transactional
@Service
@Validated
public class QFSSettingsService {

  // Logger ref.
  public static final Logger LOGGER = Logger.getLogger(QFSSettingsService.class.getName());

  // An injected Entity Manager.
  @PersistenceContext
  EntityManager em;

  // Service references.
  private QFSSettingMapper settingMapper;
  private QQFSSetting qsetting = QQFSSetting.qFSSetting;

  @Autowired
  public QFSSettingsService(QFSSettingMapper settingMapper) {
    this.settingMapper = settingMapper;
  }

  public List<QFSSettingDTO> getSettings(String owner, boolean includeSensitive) {
    JPAQuery<QFSSetting> q = new JPAQueryFactory(em).selectFrom(qsetting)
        .where(qsetting.owner.eq(owner));
    if (!includeSensitive) {
      q.where(qsetting.sensitive.ne(true));
    }
    List<QFSSetting> l = q.fetch();

    return settingMapper.map(l);
  }

  public List<QFSGroupDTO> getGroupNames(String owner) {
    List<QFSSetting> l = new JPAQueryFactory(em).selectFrom(qsetting)
        .where(qsetting.owner.eq(owner))
        .distinct()
        .orderBy(qsetting.group.asc())
        .fetch();

    return settingMapper.mapToGroupDTO(l);
  }

  public QFSSettingDTO getSetting(String owner, String key, String group) {
    QFSSettingDTO retVal;

    QFSSetting setting = new JPAQueryFactory(em).selectFrom(qsetting)
        .where(qsetting.owner.eq(owner)
            .and(qsetting.key.eq(key))
            .and(qsetting.group.eq(group)))
        .fetchOne();

    if (setting == null) {
      throw new QDoesNotExistException(MessageFormat.format(
          "Did not find a setting with key: {0}.", key));
    } else {
      retVal = settingMapper.map(setting);
    }

    return retVal;
  }

  public List<QFSSettingDTO> getGroupSettings(String owner, String group) {
    List<QFSSetting> l = new JPAQueryFactory(em).selectFrom(qsetting)
        .where(qsetting.owner.eq(owner)
            .and(qsetting.group.eq(group)))
        .fetch();

    return settingMapper.map(l);
  }

  public void createSetting(String owner, String group, String key, String val, boolean sensitive,
      boolean password) {
    try {
      getSetting(owner, key, group);
      throw new QAlreadyExistsException(MessageFormat.format(
          "A setting already exists with key: {0}.", key));
    } catch (QDoesNotExistException e) {
      QFSSetting setting = new QFSSetting();
      setting.setGroup(group);
      setting.setKey(key);
      setting.setOwner(owner);
      setting.setVal(val);
      setting.setSensitive(sensitive);
      setting.setPassword(password);

      em.persist(setting);
    }
  }

  public void setVal(String owner, String key, String val, String group) {
    QFSSetting setting = new JPAQueryFactory(em).selectFrom(qsetting)
        .where(qsetting.owner.eq(owner)
            .and(qsetting.key.eq(key)
                .and(qsetting.group.eq(group))))
        .fetchOne();
    if (setting != null) {
      setting.setVal(val);
    } else {
      throw new QDoesNotExistException(MessageFormat.format(
          "Did not find a setting with key: {0}.", key));
    }
  }
}
