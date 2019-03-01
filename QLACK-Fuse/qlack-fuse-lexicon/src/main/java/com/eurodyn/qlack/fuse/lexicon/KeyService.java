package com.eurodyn.qlack.fuse.lexicon;

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
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
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
        DataRepository dataRepository, LanguageRepository languageRepository) {
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
                String translation = key.getTranslations() != null ? key.getTranslations().get(language.getId()) : key.getName();
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

    public void deleteKey(String keyId) {
        keyRepository.deleteById(keyId);
    }

    public void deleteKeys(Collection<String> keyIds) {
        for (String keyId : keyIds) {
            deleteKey(keyId);
        }
    }

    public void deleteKeysByGroupId(String groupId) {
        Predicate predicate = qKey.group.id.eq(groupId);
        keyRepository.deleteAll(keyRepository.findAll(predicate));
    }

    public void renameKey(String keyId, String newName) {
        Key key = keyRepository.fetchById(keyId);
        key.setName(newName);
    }

    public void moveKey(String keyId, String newGroupId) {
        Key key = keyRepository.fetchById(keyId);
        key.setGroup(groupRepository.fetchById(newGroupId));
    }

    public void moveKeys(Collection<String> keyIds, String newGroupId) {
        for (String keyId : keyIds) {
            moveKey(keyId, newGroupId);
        }
    }

    public KeyDTO getKeyById(String keyId, boolean includeTranslations) {
        Key key = keyRepository.fetchById(keyId);
        return getKey(key, includeTranslations);
    }

    public KeyDTO getKeyByName(String keyName, String groupId, boolean includeTranslations) {
        Key key = keyRepository.findByNameAndGroupId(keyName, groupId);
        return getKey(key, includeTranslations);
    }

    private KeyDTO getKey(Key key, boolean includeTranslations) {
        return keyMapper.mapToDTO(key, includeTranslations);
    }

    public List<KeyDTO> findKeys(KeySearchCriteria criteria, boolean includeTranslations) {

        Predicate predicate = new BooleanBuilder();

        if (criteria.getKeyName() != null) {
            predicate = ((BooleanBuilder) predicate).and(qKey.name.eq(criteria.getKeyName()));
        }
        if (criteria.getGroupId() != null) {
            predicate = ((BooleanBuilder) predicate).and(qKey.group.id.eq(criteria.getGroupId()));
        }

        List<KeyDTO> dtos = new ArrayList<>();
        keyRepository.findAll(predicate, criteria.getPageable()).forEach(entity -> dtos.add(getKey(entity, includeTranslations)));
        return dtos;
    }

    private void update(Data data) {
        data.setLastUpdatedOn(Instant.now().toEpochMilli());
        dataRepository.save(data);
    }

    public void updateTranslation(String keyId, String languageId, String value) {
        Data data = dataRepository.findByKeyIdAndLanguageId(keyId, languageId);
        Language language = data == null ? languageRepository.fetchById(languageId) : null;
        commonUpdateTranslationWithKeyId(data, keyId, language, value);
    }

    public void updateTranslationByLocale(String keyId, String locale, String value) {
        Data data = dataRepository.findByKeyIdAndLanguageLocale(keyId, locale);
        Language language = data == null ? languageRepository.findByLocale(locale) : null;
        commonUpdateTranslationWithKeyId(data, keyId, language, value);
    }

    private void commonUpdateTranslationWithKeyId(Data data, String keyId, Language language, String value) {
        if (data == null) {
            data = new Data();
            data.setKey(keyRepository.fetchById(keyId));
            data.setLanguage(language);
        }
        data.setValue(value);
        update(data);
    }

    public void updateTranslationByGroupId(String keyName, String groupId, String languageId, String value) {
        Predicate predicate = qData.key.name.eq(keyName).and(qData.key.group.id.eq(groupId)).and(qData.language.id.eq(languageId));
        Data data = dataRepository.findOne(predicate).get();
        commonUpdateTranslationWithGroupId(data, keyName, groupId, languageId, value);
    }

    public void updateTranslationByKeyName(String keyName, String groupId, String languageId, String value) {
        Data data = dataRepository.findByKeyNameAndLanguageId(keyName, languageId);
        commonUpdateTranslationWithGroupId(data, keyName, groupId, languageId, value);
    }

    private void commonUpdateTranslationWithGroupId(Data data, String keyName, String groupId, String languageId, String value) {
        if (data == null) {
            data = new Data();
            data.setKey(keyRepository.findByNameAndGroupId(keyName, groupId));
            data.setLanguage(languageRepository.fetchById(languageId));
        }
        data.setValue(value);
        update(data);
    }

    public void updateTranslationsByGroupId(Map<String, String> keys, String groupId, String languageId) {
        for (Map.Entry<String, String> key : keys.entrySet()) {
            updateTranslationByGroupId(key.getKey(), groupId, languageId, key.getValue());
        }
    }

    public void updateTranslationsForKey(String keyId, Map<String, String> translations) {
        for (String languageId : translations.keySet()) {
            updateTranslation(keyId, languageId, translations.get(languageId));
        }
    }

    public void updateTranslationsForKeyByLocale(String keyId, Map<String, String> translations) {
        for (String locale : translations.keySet()) {
            updateTranslationByLocale(keyId, locale, translations.get(locale));
        }
    }

    public void updateTranslationsForLanguage(String languageId, Map<String, String> translations) {
        for (String keyId : translations.keySet()) {
            updateTranslation(keyId, languageId, translations.get(keyId));
        }
    }

    public void updateTranslationsForLanguageByKeyName(String languageId, String groupId, Map<String, String> translations) {
        for (String keyName : translations.keySet()) {
            updateTranslationByKeyName(keyName, groupId, languageId, translations.get(keyName));
        }
    }

    public String getTranslation(String keyName, String locale) {
        Data data = dataRepository.findByKeyNameAndLanguageLocale(keyName, locale);
        return data != null ? data.getValue() : null;
    }

    public Map<String, String> getTranslationsForKeyName(String keyName, String groupId) {
        Key key = keyRepository.findByNameAndGroupId(keyName, groupId);
        Map<String, String> translations = new HashMap<>();
        for (Data data : key.getData()) {
            translations.put(data.getLanguage().getLocale(), data.getValue());
        }
        return translations;
    }

    public String getTranslationForKeyGroupLocale(String keyName, String groupName, String locale) {

        Predicate predicate = qData.key.name.eq(keyName).and(qData.key.group.title.eq(groupName)).and(qData.language.locale.eq(locale));
        List<Data> data = dataRepository.findAll(predicate);
        List<String> list = new ArrayList<>();

        for (Data d : data) {
            list.add(d.getValue());
        }

        return !list.isEmpty() ? list.get(0) : null;
    }

    public Map<String, String> getTranslationsForLocale(String locale) {
        Language language = languageRepository.findByLocale(locale);
        return getTranslations(language.getData());
    }

    public Map<String, String> getTranslationsForGroupAndLocale(String groupId, String locale) {
        List<Data> dataList = dataRepository.findByKeyGroupIdAndLanguageLocale(groupId, locale);
        return getTranslations(dataList);
    }

    private Map<String, String> getTranslations(List<Data> dataList) {
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

    private SortedSet<TranslationKV> getSortedDataForGroupNameAndLocale(String groupName, String locale, SortType sortType) {
        Map<String, String> translations = getTranslationsForGroupNameAndLocale(groupName, locale);
        // sort in java side as we cannot order by in SQL side because translation data value is CLOB and not string
        SortedSet<TranslationKV> sortedSet = new TreeSet<>(new TranslationKV(sortType));
        for (Map.Entry<String, String> entry : translations.entrySet()) {
            sortedSet.add(new TranslationKV(entry.getKey(), entry.getValue()));
        }
        return sortedSet;
    }

    public Map<String, String> getTranslationsForGroupNameAndLocaleSorted(String groupName, String locale, SortType sortType) {
        SortedSet<TranslationKV> sortedSet = getSortedDataForGroupNameAndLocale(groupName, locale, sortType);
        HashMap<String, String> sortedMap = new LinkedHashMap<>();
        for (TranslationKV x : sortedSet) {
            sortedMap.put(x.key, x.value);
        }
        return sortedMap;
    }

    public List<String> getKeysSortedByTranslation(String groupName, String locale, SortType sortType) {
        SortedSet<TranslationKV> sortedSet = getSortedDataForGroupNameAndLocale(groupName, locale, sortType);
        List<String> sortedList = new ArrayList<>(sortedSet.size());
        for (TranslationKV tkv : sortedSet) {
            sortedList.add(tkv.key);
        }
        return sortedList;
    }

    // This class is used to sort translation keys ordered by value.
    private static class TranslationKV implements Comparator<TranslationKV>, Comparable<TranslationKV> {

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
