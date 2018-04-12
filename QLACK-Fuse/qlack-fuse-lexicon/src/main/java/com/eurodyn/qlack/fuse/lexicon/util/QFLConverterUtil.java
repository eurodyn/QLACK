package com.eurodyn.qlack.fuse.lexicon.util;


import com.eurodyn.qlack.fuse.lexicon.dto.QFLGroupDTO;
import com.eurodyn.qlack.fuse.lexicon.dto.QFLKeyDTO;
import com.eurodyn.qlack.fuse.lexicon.dto.QFLLanguageDTO;
import com.eurodyn.qlack.fuse.lexicon.dto.QFLTemplateDTO;
import com.eurodyn.qlack.fuse.lexicon.model.QFLData;
import com.eurodyn.qlack.fuse.lexicon.model.QFLGroup;
import com.eurodyn.qlack.fuse.lexicon.model.QFLKey;
import com.eurodyn.qlack.fuse.lexicon.model.QFLLanguage;
import com.eurodyn.qlack.fuse.lexicon.model.QFLTemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class QFLConverterUtil {

  public static QFLGroup groupDTOToGroup(QFLGroupDTO dto) {
    if (dto == null) {
      return null;
    }
    QFLGroup entity = new QFLGroup();
    entity.setTitle(dto.getTitle());
    entity.setDescription(dto.getDescription());
    return entity;
  }

  public static QFLGroupDTO groupToGroupDTO(QFLGroup entity) {
    if (entity == null) {
      return null;
    }
    QFLGroupDTO dto = new QFLGroupDTO();
    dto.setId(entity.getId());
    dto.setTitle(entity.getTitle());
    dto.setDescription(entity.getDescription());
    return dto;
  }

  public static Set<QFLGroupDTO> groupToGroupDTOSet(Collection<QFLGroup> entities) {
    if (entities == null) {
      return null;
    }
    Set<QFLGroupDTO> dtos = new HashSet<>();
    for (QFLGroup entity : entities) {
      dtos.add(groupToGroupDTO(entity));
    }
    return dtos;
  }

  public static QFLTemplateDTO templateToTemplateDTO(QFLTemplate entity) {
    if (entity == null) {
      return null;
    }
    QFLTemplateDTO dto = new QFLTemplateDTO();
    dto.setId(entity.getId());
    dto.setName(entity.getName());
    dto.setContent(entity.getContent());
    dto.setLanguageId(entity.getLanguage().getId());
    return dto;
  }

  public static QFLLanguage languageDTOToLanguage(QFLLanguageDTO dto) {
    if (dto == null) {
      return null;
    }
    QFLLanguage entity = new QFLLanguage();
    entity.setName(dto.getName());
    entity.setLocale(dto.getLocale());
    entity.setActive(dto.isActive());
    return entity;
  }

  public static QFLLanguageDTO languageToLanguageDTO(QFLLanguage entity) {
    if (entity == null) {
      return null;
    }
    QFLLanguageDTO dto = new QFLLanguageDTO();
    dto.setId(entity.getId());
    dto.setName(entity.getName());
    dto.setLocale(entity.getLocale());
    dto.setActive(entity.isActive());
    return dto;
  }

  public static List<QFLLanguageDTO> languageToLanguageDTOList(Collection<QFLLanguage> entities) {
    if (entities == null) {
      return null;
    }
    List<QFLLanguageDTO> dtos = new ArrayList<>(entities.size());
    for (QFLLanguage entity : entities) {
      dtos.add(languageToLanguageDTO(entity));
    }
    return dtos;
  }

  public static QFLKeyDTO keyToKeyDTO(QFLKey entity, boolean includeTranslations) {
    if (entity == null) {
      return null;
    }
    QFLKeyDTO dto = new QFLKeyDTO();
    dto.setId(entity.getId());
    dto.setName(entity.getName());
    dto.setGroupId(entity.getGroup().getId());
    if (includeTranslations) {
      Map<String, String> translations = new HashMap<>();
      if (entity.getData() != null) {
        for (QFLData data : entity.getData()) {
          translations.put(data.getLanguage().getId(), data.getValue());
        }
      }
      dto.setTranslations(translations);
    }
    return dto;
  }

  public static List<QFLKeyDTO> keyToKeyDTOList(Collection<QFLKey> entities,
      boolean includeTranslations) {
    if (entities == null) {
      return null;
    }
    List<QFLKeyDTO> dtos = new ArrayList<>();
    for (QFLKey entity : entities) {
      dtos.add(keyToKeyDTO(entity, includeTranslations));
    }
    return dtos;
  }
}
