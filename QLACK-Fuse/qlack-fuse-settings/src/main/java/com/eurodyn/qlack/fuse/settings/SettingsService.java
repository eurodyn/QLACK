package com.eurodyn.qlack.fuse.settings;

import com.eurodyn.qlack.common.exceptions.QAlreadyExistsException;
import com.eurodyn.qlack.common.exceptions.QDoesNotExistException;
import com.eurodyn.qlack.fuse.settings.dto.GroupDTO;
import com.eurodyn.qlack.fuse.settings.dto.SettingDTO;
import com.eurodyn.qlack.fuse.settings.mappers.SettingMapper;
import com.eurodyn.qlack.fuse.settings.model.QSetting;
import com.eurodyn.qlack.fuse.settings.model.Setting;
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
public class SettingsService {
  // Logger ref.
  public static final Logger LOGGER = Logger.getLogger(SettingsService.class.getName());

  // An injected Entity Manager.
  @PersistenceContext
  EntityManager em;

  // Service references.
  private SettingMapper settingMapper;

  @Autowired
  public SettingsService(SettingMapper settingMapper) {
    this.settingMapper = settingMapper;
  }

  public List<SettingDTO> getSettings(String owner, boolean includeSensitive) {
    QSetting qsetting = QSetting.setting;
    JPAQuery<Setting> q = new JPAQueryFactory(em).selectFrom(qsetting)
      .where(qsetting.owner.eq(owner));
    if (!includeSensitive) {
      q.where(qsetting.sensitive.ne(true));
    }
    List<Setting> l = q.fetch();

    return settingMapper.map(l);
  }

  public List<GroupDTO> getGroupNames(String owner) {
    QSetting qsetting = QSetting.setting;
    List<Setting> l = new JPAQueryFactory(em).selectFrom(qsetting)
      .where(qsetting.owner.eq(owner))
      .distinct()
      .orderBy(qsetting.group.asc())
      .fetch();

    return settingMapper.mapToGroupDTO(l);
  }

  public SettingDTO getSetting(String owner, String key, String group) {
    SettingDTO retVal;

    QSetting qsetting = QSetting.setting;
    Setting setting = new JPAQueryFactory(em).selectFrom(qsetting)
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

  public List<SettingDTO> getGroupSettings(String owner, String group) {
    QSetting qsetting = QSetting.setting;
    List<Setting> l = new JPAQueryFactory(em).selectFrom(qsetting)
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
      Setting setting = new Setting();
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
    QSetting qsetting = QSetting.setting;
    Setting setting = new JPAQueryFactory(em).selectFrom(qsetting)
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
