package com.eurodyn.qlack.fuse.lexicon;

import com.eurodyn.qlack.fuse.lexicon.dto.GroupDTO;
import com.eurodyn.qlack.fuse.lexicon.dto.KeyDTO;
import com.eurodyn.qlack.fuse.lexicon.dto.LanguageDTO;
import com.eurodyn.qlack.fuse.lexicon.exception.LexiconYMLProcessingException;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.CustomClassLoaderConstructor;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@Validated
@Transactional
public class LexiconConfigService {

  private static final Logger LOGGER = Logger.getLogger(LexiconConfigService.class.getName());

//  @PersistenceContext
//  private EntityManager em;

  // Service references.
  private GroupService groupService;
  private LanguageService languageService;
  private KeyService keyService;

  @Autowired
  public LexiconConfigService(GroupService groupService,
      LanguageService languageService, KeyService keyService) {
    this.groupService = groupService;
    this.languageService = languageService;
    this.keyService = keyService;
  }

  private void updateKeys(List<Map<String, Object>> translations, String groupId,
      String languageId) {
    for (Map<String, Object> translation : translations) {
      String translationKey = translation.keySet().iterator().next();
      String translationValue = (String) translation.get(translationKey);
      KeyDTO keyDTO = keyService.getKeyByName(translationKey, groupId, true);
      // If the key does not exist in the DB then create it.
      if (keyDTO == null) {
        keyDTO = new KeyDTO();
        keyDTO.setGroupId(groupId);
        keyDTO.setName(translationKey);
        Map<String, String> keyTranslations = new HashMap<>();
        keyTranslations.put(languageId, translationValue);
        keyDTO.setTranslations(keyTranslations);
        keyService.createKey(keyDTO, false);
      }
      // If the key exists check if a translation exists and if it
      // does check if it is the same as the key name, which means
      // that the translation was created automatically (ex. when
      // adding a new language) and therefore it should be
      // updated. Otherwise only update the key if the forceUpdate
      // flag is set to true.
      else if ((keyDTO.getTranslations().get(languageId) == null)
          || (keyDTO.getTranslations().get(languageId).equals(translationKey))
          || ((translation.get("forceUpdate") != null)
          && ((Boolean) translation.get("forceUpdate") == true))) {
        keyService.updateTranslation(keyDTO.getId(), languageId, translationValue);
      }
    }
  }

  @PostConstruct
  public void init() {
    try {
      Enumeration<URL> entries = this.getClass().getClassLoader()
          .getResources("qlack-lexicon-config.yaml");
      if (entries != null) {
        while (entries.hasMoreElements()) {
          updateTranslations(entries.nextElement());
        }
      }
    } catch (IOException e) {
      LOGGER.log(Level.SEVERE, "Could not search QLACK Lexicon configuration files.", e);
    }
  }

  public void updateTranslations(URL yamlUrl) {
    try {
      Yaml yaml = new Yaml(new CustomClassLoaderConstructor(getClass().getClassLoader()));

      @SuppressWarnings("unchecked")
      Map<String, Object> contents = (Map<String, Object>) yaml.load(yamlUrl.openStream());

      // Process translation groups
      @SuppressWarnings("unchecked")
      List<Map<String, Object>> groups = (List<Map<String, Object>>) contents.get("groups");
      if (groups != null) {
        for (Map<String, Object> group : groups) {
          String groupName = (String) group.get("name");
          String groupDescription = (String) group.get("description");
          GroupDTO groupDTO = groupService.getGroupByName(groupName);
          // If a group with this name does not exist create it.
          if (groupDTO == null) {
            groupDTO = new GroupDTO();
            groupDTO.setTitle(groupName);
            groupDTO.setDescription(groupDescription);
            groupService.createGroup(groupDTO);
          }
          // Else check the value of the forceUpdate flag
          else if ((group.get("forceUpdate") != null) && ((Boolean) group.get("forceUpdate")
              == true)) {
            groupDTO.setDescription(groupDescription);
            groupService.updateGroup(groupDTO);
          }
        }
      }

      // Process languages
      @SuppressWarnings("unchecked")
      List<Map<String, Object>> languages = (List<Map<String, Object>>) contents.get("languages");
      if (languages != null) {
        for (Map<String, Object> language : languages) {
          String languageName = (String) language.get("name");
          String locale = (String) language.get("locale");
          LanguageDTO languageDTO = languageService.getLanguageByLocale(locale);
          // If a language with this locale does not exist create it.
          if (languageDTO == null) {
            languageDTO = new LanguageDTO();
            languageDTO.setName(languageName);
            languageDTO.setLocale(locale);
            languageDTO.setActive(true);
            languageService.createLanguage(languageDTO, null);
          }
          // Else check the value of the forceUpdate flag
          else if ((language.get("forceUpdate") != null) && ((Boolean) language.get("forceUpdate")
              == true)) {
            languageDTO.setName(languageName);
            languageService.updateLanguage(languageDTO);
          }
        }
      }

      // Process translations
      @SuppressWarnings("unchecked")
      List<Map<String, Object>> translationContents = (List<Map<String, Object>>) contents
          .get("translations");
      if (translationContents != null) {
        for (Map<String, Object> translationContent : translationContents) {
          @SuppressWarnings("unchecked")
          List<String> excludedGroupName = (List<String>) translationContent.get("not_in_group");
          String locale = (String) translationContent.get("locale");
          String languageId = languageService.getLanguageByLocale(locale).getId();
          @SuppressWarnings("unchecked")
          List<Map<String, Object>> translations = (List<Map<String, Object>>) translationContent
              .get("keys");

          // Process not_in_group
          if (excludedGroupName != null) {
            Set<GroupDTO> newGroups = groupService.getRemainingGroups(excludedGroupName);
            for (GroupDTO group : newGroups) {
              updateKeys(translations, group.getId(), languageId);
            }
          } else { // Process group
            String groupName = (String) translationContent.get("group");
            String groupId = groupService.getGroupByName(groupName).getId();
            updateKeys(translations, groupId, languageId);
          }
        }
      }
    } catch (IOException | SecurityException ex) {
      LOGGER.log(Level.SEVERE, "Error handling lexicon YAML file", ex);
      throw new LexiconYMLProcessingException("Error handling lexicon YAML file");
    }

  }
}
