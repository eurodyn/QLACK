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
    Predicate predicate = qsetting.owner.endsWith(owner);
    if(!includeSensitive){
      predicate = ((BooleanExpression) predicate).and(qsetting.sensitive.ne(true));
    }

    return settingMapper.map(settingRepository.findAll(predicate));
  }

  public List<GroupDTO> getGroupNames(String owner) {
    Predicate predicate = qsetting.owner.eq(owner);

    return settingMapper.mapToGroupDTO(settingRepository.findAll(predicate));
  }

  public SettingDTO getSetting(String owner, String key, String group) {
    SettingDTO retVal;
    Optional<Setting> setting = getOptionalSetting(owner, key, group);

    retVal = settingMapper.map(setting.orElseThrow(
        () -> new QDoesNotExistException(MessageFormat.format(
          "Did not find a setting with key: {0}.", key))));

    return retVal;
  }

  public List<SettingDTO> getGroupSettings(String owner, String group) {
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

      settingRepository.save(setting);
    }
  }

  public void setVal(String owner, String key, String val, String group) {
    Setting setting = getOptionalSetting(owner, key, group).orElseThrow(
        () -> new QDoesNotExistException(MessageFormat.format("Did not find a setting with key: {0}.", key)));
    setting.setVal(val);
  }


  private Optional<Setting> getOptionalSetting(String owner, String key, String group) {
    Predicate predicate = qsetting.owner.eq(owner)
        .and(qsetting.key.eq(key))
        .and(qsetting.group.eq(group));
    return settingRepository.findOne(predicate);
  }
}
