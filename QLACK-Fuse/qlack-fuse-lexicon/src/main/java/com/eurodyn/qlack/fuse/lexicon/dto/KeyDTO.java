package com.eurodyn.qlack.fuse.lexicon.dto;

import java.io.Serializable;
import java.util.Map;

public class KeyDTO implements Serializable {

  private String id;
  private String name;
  private String groupId;
  // The translations available for this key. The map key is
  // the language ID while the map value is the actual translation.
  private Map<String, String> translations;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getGroupId() {
    return groupId;
  }

  public void setGroupId(String groupId) {
    this.groupId = groupId;
  }

  public Map<String, String> getTranslations() {
    return translations;
  }

  public void setTranslations(Map<String, String> translations) {
    this.translations = translations;
  }
}
