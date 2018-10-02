package com.eurodyn.qlack.fuse.lexicon;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.eurodyn.qlack.fuse.lexicon.criteria.KeySearchCriteria;
import com.eurodyn.qlack.fuse.lexicon.criteria.KeySearchCriteria.SortType;
import com.eurodyn.qlack.fuse.lexicon.dto.KeyDTO;
import com.eurodyn.qlack.fuse.lexicon.mappers.KeyMapper;
import com.eurodyn.qlack.fuse.lexicon.model.Data;
import com.eurodyn.qlack.fuse.lexicon.model.Key;
import com.eurodyn.qlack.fuse.lexicon.model.Language;
import com.eurodyn.qlack.fuse.lexicon.model.QData;
import com.eurodyn.qlack.fuse.lexicon.model.QKey;
import com.eurodyn.qlack.fuse.lexicon.repository.DataRepository;
import com.eurodyn.qlack.fuse.lexicon.repository.GroupRepository;
import com.eurodyn.qlack.fuse.lexicon.repository.KeyRepository;
import com.eurodyn.qlack.fuse.lexicon.repository.LanguageRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

@Transactional
@Service
@Validated
public class KeyService {

  // Entities for queries
  QData qData = QData.data;
  QKey qKey = QKey.key;

  private final KeyRepository keyRepository;
  private final GroupRepository groupRepository;
  private final DataRepository dataRepository; 
  private final LanguageRepository languageRepository;

  private KeyMapper keyMapper;
  
  @Autowired
	public KeyService(KeyRepository keyRepository, GroupRepository groupRepository, KeyMapper keyMapper,
			DataRepository dataRepository,  LanguageRepository languageRepository) {
		this.keyRepository = keyRepository;
		this.keyMapper = keyMapper;
		this.groupRepository = groupRepository;
		this.dataRepository = dataRepository;
		this.languageRepository = languageRepository;
	}
  
  
  public String createKey(KeyDTO key, boolean createDefaultTranslations) {
    // Create the new key.
    Key entity = new Key();
    entity.setName(key.getName());
    if (key.getGroupId() != null) {
    	entity.setGroup(groupRepository.fetchById(key.getGroupId()));
    }
    keyRepository.save(entity);

    if (createDefaultTranslations) {
      List<Language> languages = languageRepository.findAll();
      for (Language language : languages) {
        String translation = null;
        if (key.getTranslations() != null) {
          translation = key.getTranslations().get(language.getId());
        }
        if (translation == null) {
          translation = key.getName();
        }
        updateTranslation(entity.getId(), language.getId(), translation);
      }
    } else if (key.getTranslations() != null) {
      for (String languageId : key.getTranslations().keySet()) {
        updateTranslation(entity.getId(), languageId, key.getTranslations().get(languageId));
      }
    }

    return entity.getId();
  }

  public List<String> createKeys(List<KeyDTO> keys, boolean createDefaultTranslations) {
    List<String> ids = new ArrayList<>();
    for (KeyDTO key : keys) {
      ids.add(createKey(key, createDefaultTranslations));
    }
    return ids;
  }

  public void deleteKey(String keyID) {
    List<String> keyIDs = new ArrayList<>(1);
    keyIDs.add(keyID);
    // This call also takes care of invalidating the cache.
    deleteKeys(keyIDs);
  }

  public void deleteKeys(Collection<String> keyIDs) {
    for (String keyID : keyIDs) {
    	keyRepository.deleteById(keyID);
    }
  }

  public void deleteKeysByGroupId(String groupId) {
	  Predicate predicate = qKey.group.id.eq(groupId);
	  keyRepository.deleteAll(keyRepository.findAll(predicate));
  }

  public void renameKey(String keyID, String newName) {
    Key key = keyRepository.fetchById(keyID);
    key.setName(newName);
  }

  public void moveKey(String keyID, String newGroupId) {
    List<String> keyIDs = new ArrayList<>(1);
    keyIDs.add(keyID);
    // This call also takes care of invalidating the cache.
    moveKeys(keyIDs, newGroupId);
  }

  public void moveKeys(Collection<String> keyIDs, String newGroupId) {
    for (String keyID : keyIDs) {
      Key key = keyRepository.fetchById(keyID);
      key.setGroup(groupRepository.fetchById(newGroupId));
    }
  }

	public KeyDTO getKeyByID(String keyID, boolean includeTranslations) {

		Key key = keyRepository.fetchById(keyID);

		if (includeTranslations) {
			return keyMapper.mapToDTOwithTranslations(key, true);
		} else {
			return keyMapper.mapToDTO(key);
		}
	}

	public KeyDTO getKeyByName(String keyName, String groupId, boolean includeTranslations) {

		Key key = keyRepository.findByNameAndGroupId(keyName, groupId);
		if (includeTranslations) {
			return keyMapper.mapToDTOwithTranslations(key, true);
		} else {
			return keyMapper.mapToDTO(key);
		}
	}

  public List<KeyDTO> findKeys(KeySearchCriteria criteria,
			boolean includeTranslations) {

		Predicate predicate = new BooleanBuilder();

		if (criteria.getKeyName() != null) {
			predicate = ((BooleanBuilder) predicate).and(qKey.name.eq(criteria.getKeyName()));
		}
		if (criteria.getGroupId() != null) {
			predicate = ((BooleanBuilder) predicate).and(qKey.group.id.eq(criteria.getGroupId()));
		}

		List<KeyDTO> dtos = new ArrayList<>();
		keyRepository.findAll(predicate, criteria.getPageable()).forEach(entity -> {
			KeyDTO dto = keyMapper.mapToDTO(entity);
			if (includeTranslations) {
				Map<String, String> translations = new HashMap<>();
				if (entity.getData() != null) {
					for (Data data : entity.getData()) {
						translations.put(data.getLanguage().getId(), data.getValue());
					}
				}
				dto.setTranslations(translations);
			}
			dtos.add(dto);
		});
		return dtos;
	}


  private void update(Data data) {
    data.setLastUpdatedOn(Instant.now().toEpochMilli());
    dataRepository.save(data);
  }

	public void updateTranslation(String keyID, String languageID, String value) {
		Key key = keyRepository.fetchById(keyID);
		Data data = dataRepository.findByKeyIdAndLanguageId(keyID, languageID);

		if (data == null) {
			data = new Data();
			data.setKey(key);
			data.setLanguage(languageRepository.fetchById(languageID));
			// Language.find(languageID, em));
		}
		data.setValue(value);
		update(data);
	}

  public void updateTranslationsByGroupId(Map<String, String> keys, String groupId,
      String languageId) {
    for (Map.Entry<String, String> key : keys.entrySet()) {
      updateTranslationByGroupId(key.getKey(), key.getValue(), groupId, languageId);
    }
  }

  public void updateTranslationByGroupId(String keyName, String value, String groupId,
			String languageId) {

		Predicate predicate = qData.key.name.eq(keyName).and(qData.key.group.id.eq(groupId))
				.and(qData.language.id.eq(languageId));
		Data data = dataRepository.findOne(predicate).get();

		if (data == null) {
			data = new Data();
			data.setKey(keyRepository.findByNameAndGroupId(keyName, groupId));
			data.setLanguage(languageRepository.fetchById(languageId));
		}
		data.setValue(value);
		update(data);
	}

  public void updateTranslationByKeyName(String keyName, String groupID, String languageID,
			String value) {
		Data data = dataRepository.findByKeyNameAndLanguageId(keyName, languageID);
		if (data == null) {
			data = new Data();
			data.setKey(keyRepository.findByNameAndGroupId(keyName, groupID));
			data.setLanguage(languageRepository.fetchById(languageID));
		}
		data.setValue(value);
		update(data);
	}

  public void updateTranslationByLocale(String keyID, String locale, String value) {

	  Data data = dataRepository.findByKeyIdAndLanguageLocale(keyID, locale);
    if (data == null) {
      data = new Data();
      data.setKey(keyRepository.fetchById(keyID));
      data.setLanguage(languageRepository.findByLocale(locale));
    }
    data.setValue(value);
    update(data);
  }

  public void updateTranslationsForKey(String keyID,
      Map<String, String> translations) {
    for (String languageId : translations.keySet()) {
      // This call also takes care of invalidating the cache.
      updateTranslation(keyID, languageId, translations.get(languageId));
    }
  }

  public void updateTranslationsForKeyByLocale(String keyID, Map<String, String> translations) {
    for (String locale : translations.keySet()) {
      // This call also takes care of invalidating the cache.
      updateTranslationByLocale(keyID, locale, translations.get(locale));
    }
  }

  public void updateTranslationsForLanguage(String languageID,
      Map<String, String> translations) {
    for (String keyId : translations.keySet()) {
      // This call also takes care of invalidating the cache.
      updateTranslation(keyId, languageID, translations.get(keyId));
    }
  }

  public void updateTranslationsForLanguageByKeyName(String languageID, String groupID,
      Map<String, String> translations) {
    for (String keyName : translations.keySet()) {
      // This call also takes care of invalidating the cache.
      updateTranslationByKeyName(keyName, groupID, languageID, translations.get(keyName));
    }
  }

	public String getTranslation(String keyName, String locale) {
		Data data = dataRepository.findByKeyNameAndLanguageLocale(keyName, locale);
		if (data == null) {
			return null;
		}
		return data.getValue();
	}

  public Map<String, String> getTranslationsForKeyName(String keyName, String groupID) {
    Key key = keyRepository.findByNameAndGroupId(keyName, groupID); 
    Map<String, String> translations = new HashMap<>();
    for (Data data : key.getData()) {
      translations.put(data.getLanguage().getLocale(), data.getValue());
    }
    return translations;
  }

	public String getTranslationForKeyGroupLocale(String keyName, String groupName, String locale) {

	  Predicate predicate = qData.key.name.eq(keyName).and(qData.key.group.title.eq(groupName)).and(qData.language.locale.eq(locale));
	  List<Data> data  = dataRepository.findAll(predicate);
	  List<String> list = Collections.emptyList();
	  
	  for (Data d: data) {
		  list.add(d.getValue());
	  }
    if (list != null && !list.isEmpty()) {
      return list.get(0);
    }
    return null;
  }

	public Map<String, String> getTranslationsForLocale(String locale) {
		Language language = languageRepository.findByLocale(locale);
		Map<String, String> translations = new HashMap<>();
		for (Data data : language.getData()) {
			translations.put(data.getKey().getName(), data.getValue());
		}
		return translations;
	}

  public Map<String, String> getTranslationsForGroupAndLocale(String groupId, String locale) {
	  List<Data> dataList = dataRepository.findByKeyGroupIdAndLanguageLocale(groupId, locale);
    
    Map<String, String> translations = new HashMap<>();
    for (Data data : dataList) {
      translations.put(data.getKey().getName(), data.getValue());
    }
    return translations;
  }

  
  
	public Map<String, String> getTranslationsForGroupNameAndLocale(String groupName, String locale) {

		Predicate predicate = (qData.key.group.title.eq(groupName)).and(qData.language.locale.eq(locale));
		return dataRepository.findAll(predicate).stream()
				.collect(Collectors.toMap(a -> a.getKey().getName(), a -> a.getValue()));
	}

  private SortedSet<TranslationKV> getSortedDataForGroupNameAndLocale(String groupName,
      String locale, SortType sortType) {
    Map<String, String> translations = getTranslationsForGroupNameAndLocale(groupName, locale);
    // sort in java side as we cannot order by in SQL side because translation data value is CLOB and not string
    SortedSet<TranslationKV> sortedSet = new TreeSet<>(new TranslationKV(sortType));
    for (Map.Entry<String, String> entry : translations.entrySet()) {
      sortedSet.add(new TranslationKV(entry.getKey(), entry.getValue()));
    }
    return sortedSet;
  }

  public Map<String, String> getTranslationsForGroupNameAndLocaleSorted(String groupName,
      String locale, SortType sortType) {
    SortedSet<TranslationKV> sortedSet = getSortedDataForGroupNameAndLocale(groupName, locale,
        sortType);
    HashMap<String, String> sortedMap = new LinkedHashMap<>();
    for (TranslationKV x : sortedSet) {
      sortedMap.put(x.key, x.value);
    }
    return sortedMap;
  }

  public List<String> getKeysSortedByTranslation(String groupName, String locale,
      SortType sortType) {
    SortedSet<TranslationKV> sortedSet = getSortedDataForGroupNameAndLocale(groupName, locale,
        sortType);
    List<String> sortedList = new ArrayList<>(sortedSet.size());
    for (TranslationKV tkv : sortedSet) {
      sortedList.add(tkv.key);
    }
    return sortedList;
  }

  // This class is used to sort translation keys ordered by value.
  private static class TranslationKV implements Comparator<TranslationKV>,
      Comparable<TranslationKV> {

    String key;
    String value;
    SortType sortType;

    public TranslationKV(SortType sortType) {
      this.sortType = sortType;
    }

    public TranslationKV(String key, String value) {
      this.key = key;
      this.value = value;
    }

    @Override
    public int compare(TranslationKV arg0, TranslationKV arg1) {
      return sortType.equals(SortType.ASCENDING) ? arg0.value.compareTo(arg1.value)
          : arg1.value.compareTo(arg0.value);
    }

    @Override
    public int compareTo(TranslationKV arg0) {
      return sortType.equals(SortType.ASCENDING) ? value.compareTo(arg0.value)
          : arg0.value.compareTo(value);
    }

    @Override
    public boolean equals(Object obj) {
      if (obj == null) {
        return false;
      }
      if (!(obj instanceof TranslationKV)) {
        return false;
      }
      TranslationKV x = (TranslationKV) obj;
      if (value == null) {
        return (x.value == null);
      }
      return value.equals(x.value);
    }

    @Override
    public int hashCode() {
      return super.hashCode();
    }
  }

}
