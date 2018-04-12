package com.eurodyn.qlack.fuse.mailing.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author European Dynamics SA
 */
public class QFMAttributeDTO implements Serializable {

  private Map<String, Object> attribute = new HashMap<String, Object>();

  public Object clearAttribute(String key) {
    return this.getAttribute().remove(key);
  }

  public Object getAttribute(String key) {
    return this.getAttribute().get(key);
  }

  public void setAttribute(String key, Object value) {
    this.getAttribute().put(key, value);
  }

  public Map<String, Object> getAttribute() {
    return attribute;
  }

  public void setAttribute(Map<String, Object> attribute) {
    this.attribute = attribute;
  }

}