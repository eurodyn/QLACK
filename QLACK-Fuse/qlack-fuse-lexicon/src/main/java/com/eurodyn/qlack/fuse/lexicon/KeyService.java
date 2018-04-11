package com.eurodyn.qlack.fuse.lexicon;

import com.eurodyn.qlack.fuse.lexicon.criteria.KeySearchCriteria;
import com.eurodyn.qlack.fuse.lexicon.criteria.KeySearchCriteria.SortType;
import com.eurodyn.qlack.fuse.lexicon.dto.KeyDTO;
import com.eurodyn.qlack.fuse.lexicon.model.Data;
import com.eurodyn.qlack.fuse.lexicon.model.Group;
import com.eurodyn.qlack.fuse.lexicon.model.Key;
import com.eurodyn.qlack.fuse.lexicon.model.Language;
import com.eurodyn.qlack.fuse.lexicon.model.QData;
import com.eurodyn.qlack.fuse.lexicon.model.QKey;
import com.eurodyn.qlack.fuse.lexicon.util.ConverterUtil;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

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

@Transactional
@Service
@Validated
public class KeyService {

  // Entities for queries
  QData qData = QData.data;
  QKey qKey = QKey.key;

  @PersistenceContext
  private EntityManager em;

  public String createKey(KeyDTO key, boolean createDefaultTranslations) {
    // Create the new key.
    Key entity = new Key();
    entity.setName(key.getName());
    if (key.getGroupId() != null) {
      entity.setGroup(Group.find(key.getGroupId(), em));
    }
    em.persist(entity);

    if (createDefaultTranslations) {
      List<Language> languages = Language.getAllLanguages(em);
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
      em.remove(Key.find(keyID, em));
    }
  }

  public void deleteKeysByGroupId(String groupId) {
    new JPAQueryFactory(em).delete(qKey).where(qKey.group.id.eq(groupId)).execute();
  }

  public void renameKey(String keyID, String newName) {
    Key key = Key.find(keyID, em);
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
      Key key = Key.find(keyID, em);
      key.setGroup(Group.find(newGroupId, em));
    }
  }

  public KeyDTO getKeyByID(String keyID, boolean includeTranslations) {
    return ConverterUtil.keyToKeyDTO(em.find(Key.class, keyID), includeTranslations);
  }

  public KeyDTO getKeyByName(String keyName, String groupId, boolean includeTranslations) {
    return ConverterUtil.keyToKeyDTO(Key.findByName(keyName, groupId, em), includeTranslations);
  }

  public List<KeyDTO> findKeys(KeySearchCriteria criteria,
      boolean includeTranslations) {
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<Key> cq = cb.createQuery(Key.class);
    Root<Key> root = cq.from(Key.class);

    // Add query criteria
    if (criteria.getKeyName() != null) {
      Predicate pr = cb.like(root.<String>get("name"), criteria.getKeyName());
      cq = addPredicate(cq, cb, pr);
    }
    if (criteria.getGroupId() != null) {
      Predicate pr = cb.equal(root.get("group").get("id"), criteria.getGroupId());
      cq = addPredicate(cq, cb, pr);
    }

    // Set ordering
    Order order = null;
    if (criteria.isAscending()) {
      order = cb.asc(root.get("name"));
    } else {
      order = cb.desc(root.get("name"));
    }
    cq = cq.orderBy(order);

    TypedQuery<Key> query = em.createQuery(cq);

    // Apply pagination
    if (criteria.getPaging() != null && criteria.getPaging().getCurrentPage() > -1) {
      query.setFirstResult(
          (criteria.getPaging().getCurrentPage() - 1) * criteria.getPaging().getPageSize());
      query.setMaxResults(criteria.getPaging().getPageSize());
    }

    return ConverterUtil.keyToKeyDTOList(query.getResultList(), includeTranslations);
  }

  private <T> CriteriaQuery<T> addPredicate(CriteriaQuery<T> query,
      CriteriaBuilder cb, Predicate pr) {
    CriteriaQuery<T> cq = query;
    if (cq.getRestriction() != null) {
      cq = cq.where(cb.and(cq.getRestriction(), pr));
    } else {
      cq = cq.where(pr);
    }
    return cq;
  }

  private void update(Data data) {
    data.setLastUpdatedOn(Instant.now().toEpochMilli());
    em.merge(data);
  }

  public void updateTranslation(String keyID, String languageID, String value) {
    Key key = Key.find(keyID, em);
    Data data = Data.findByKeyAndLanguageId(keyID, languageID, em);
    if (data == null) {
      data = new Data();
      data.setKey(key);
      data.setLanguage(Language.find(languageID, em));
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
    Data data = new JPAQueryFactory(em).selectFrom(qData).where(qData.key.name.eq(keyName)
        .and(qData.key.group.id.eq(groupId))
        .and(qData.language.id.eq(languageId))).fetchOne();
    if (data == null) {
      data = new Data();
      data.setKey(Key.findByName(keyName, groupId, em));
      data.setLanguage(Language.find(languageId, em));
    }
    data.setValue(value);
    update(data);
  }

  public void updateTranslationByKeyName(String keyName, String groupID, String languageID,
      String value) {
    Data data = Data.findByKeyNameAndLanguageId(keyName, languageID, em);
    if (data == null) {
      data = new Data();
      data.setKey(Key.findByName(keyName, groupID, em));
      data.setLanguage(Language.find(languageID, em));
    }
    data.setValue(value);
    update(data);
  }

  public void updateTranslationByLocale(String keyID, String locale, String value) {
    Data data = Data.findByKeyIdAndLocale(keyID, locale, em);
    if (data == null) {
      data = new Data();
      data.setKey(Key.find(keyID, em));
      data.setLanguage(Language.findByLocale(locale, em));
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
    Data data = Data.findByKeyNameAndLocale(keyName, locale, em);
    if (data == null) {
      return null;
    }
    return data.getValue();
  }

  public Map<String, String> getTranslationsForKeyName(String keyName, String groupID) {
    Key key = Key.findByName(keyName, groupID, em);
    Map<String, String> translations = new HashMap<>();
    for (Data data : key.getData()) {
      translations.put(data.getLanguage().getLocale(), data.getValue());
    }
    return translations;
  }

  public String getTranslationForKeyGroupLocale(String keyName, String groupName, String locale) {
    List<String> list = new JPAQueryFactory(em)
        .select(qData.value).from(qData)
        .where(
            qData.key.name.eq(keyName)
                .and(qData.key.group.title.eq(groupName))
                .and(qData.language.locale.eq(locale))
        )
        .fetch();
    if (list != null && !list.isEmpty()) {
      return list.get(0);
    }
    return null;
  }

  public Map<String, String> getTranslationsForLocale(String locale) {
    Language language = Language.findByLocale(locale, em);
    Map<String, String> translations = new HashMap<>();
    for (Data data : language.getData()) {
      translations.put(data.getKey().getName(), data.getValue());
    }
    return translations;
  }

  public Map<String, String> getTranslationsForGroupAndLocale(String groupId, String locale) {
    List<Data> dataList = Data.findByGroupIDAndLocale(groupId, locale, em);
    Map<String, String> translations = new HashMap<>();
    for (Data data : dataList) {
      translations.put(data.getKey().getName(), data.getValue());
    }
    return translations;
  }

  public Map<String, String> getTranslationsForGroupNameAndLocale(String groupName, String locale) {

    List<Tuple> listTuples = new JPAQueryFactory(em)
        .select(qData.key.name, qData.value)
        .from(qData)
        .leftJoin(qData.key)
        .where(
            (qData.key.group.title.eq(groupName))
                .and(qData.language.locale.eq(locale))
        )
        .fetch();

    Map<String, String> translations = new HashMap<>();
    if (listTuples != null) {
      for (Tuple t : listTuples) {
        translations.put(t.get(0, String.class), t.get(1, String.class));
      }
    }

    return translations;
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
