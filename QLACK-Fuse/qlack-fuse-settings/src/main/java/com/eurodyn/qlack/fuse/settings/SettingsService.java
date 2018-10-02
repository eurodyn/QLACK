package com.eurodyn.qlack.fuse.settings;

import com.eurodyn.qlack.common.exceptions.QAlreadyExistsException;
import com.eurodyn.qlack.common.exceptions.QDoesNotExistException;
import com.eurodyn.qlack.fuse.settings.dto.GroupDTO;
import com.eurodyn.qlack.fuse.settings.dto.SettingDTO;
import com.eurodyn.qlack.fuse.settings.mappers.SettingMapper;
import com.eurodyn.qlack.fuse.settings.model.QSetting;
import com.eurodyn.qlack.fuse.settings.model.Setting;
import com.eurodyn.qlack.fuse.settings.repository.SettingRepository;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Transactional
@Service
@Validated
public class SettingsService {

  // Logger ref.
  public static final Logger LOGGER = Logger.getLogger(SettingsService.class.getName());

  // An injected Entity Manager.
//  @PersistenceContext
//  EntityManager em;

  // Service references.
  private final SettingMapper settingMapper;
  private QSetting qsetting = QSetting.setting;
  private final SettingRepository settingRepository;

  @Autowired
  public SettingsService(SettingMapper settingMapper,
      SettingRepository settingRepository) {
    this.settingMapper = settingMapper;
    this.settingRepository = settingRepository;
  }

  public List<SettingDTO> getSettings(String owner, boolean includeSensitive) {
//    JPAQuery<Setting> q = new JPAQueryFactory(em).selectFrom(qsetting)
//        .where(qsetting.owner.eq(owner));
//    if (!includeSensitive) {
//      q.where(qsetting.sensitive.ne(true));
//    }
//    List<Setting> l = q.fetch();
    Predicate predicate = qsetting.owner.endsWith(owner);
    if(!includeSensitive){
      predicate = ((BooleanExpression) predicate).and(qsetting.sensitive.ne(true));
    }

    return settingMapper.map(settingRepository.findAll(predicate));
  }

  public List<GroupDTO> getGroupNames(String owner) {
//    List<Setting> l = new JPAQueryFactory(em).selectFrom(qsetting)
//        .where(qsetting.owner.eq(owner))
//        .distinct()
//        .orderBy(qsetting.group.asc())
//        .fetch();
    Predicate predicate = qsetting.owner.eq(owner);

    return settingMapper.mapToGroupDTO(settingRepository.findAll(predicate));
  }

  public SettingDTO getSetting(String owner, String key, String group) {
    SettingDTO retVal;

//    Setting setting = new JPAQueryFactory(em).selectFrom(qsetting)
//        .where(qsetting.owner.eq(owner)
//            .and(qsetting.key.eq(key))
//            .and(qsetting.group.eq(group)))
//        .fetchOne();
    Predicate predicate = qsetting.owner.eq(owner)
        .and(qsetting.key.eq(key))
        .and(qsetting.group.eq(group));
    Optional<Setting> setting = settingRepository.findOne(predicate);

//    if (setting == null) {
//      throw new QDoesNotExistException(MessageFormat.format(
//          "Did not find a setting with key: {0}.", key));
//    } else {
//      retVal = settingMapper.map(setting);
//    }
    retVal = settingMapper.map(setting.orElseThrow(
        () -> new QDoesNotExistException(MessageFormat.format(
          "Did not find a setting with key: {0}.", key))));

    return retVal;
  }

  public List<SettingDTO> getGroupSettings(String owner, String group) {
//    List<Setting> l = new JPAQueryFactory(em).selectFrom(qsetting)
//        .where(qsetting.owner.eq(owner)
//            .and(qsetting.group.eq(group)))
//        .fetch();
    Predicate predicate = qsetting.owner.eq(owner)
        .and(qsetting.group.eq(group));

    return settingMapper.map(settingRepository.findAll(predicate));
  }

  public void createSetting(SettingDTO dto) {
    try {
      getSetting(dto.getOwner(), dto.getKey(), dto.getGroup());
      throw new QAlreadyExistsException(MessageFormat.format(
          "A setting already exists with key: {0}.", dto.getKey()));
    } catch (QDoesNotExistException e) {
      Setting setting = settingMapper.mapToEntity(dto);

//      em.persist(setting);
      settingRepository.save(setting);
    }
  }

  public void setVal(String owner, String key, String val, String group) {
//    Setting setting = new JPAQueryFactory(em).selectFrom(qsetting)
//        .where(qsetting.owner.eq(owner)
//            .and(qsetting.key.eq(key)
//                .and(qsetting.group.eq(group))))
//        .fetchOne();
    Predicate predicate = qsetting.owner.eq(owner)
        .and(qsetting.key.eq(key))
        .and(qsetting.group.eq(group));
    Setting setting = settingRepository.findAll(predicate).get(0);
    if (setting != null) {
      setting.setVal(val);
    } else {
      throw new QDoesNotExistException(MessageFormat.format(
          "Did not find a setting with key: {0}.", key));
    }
  }
}
