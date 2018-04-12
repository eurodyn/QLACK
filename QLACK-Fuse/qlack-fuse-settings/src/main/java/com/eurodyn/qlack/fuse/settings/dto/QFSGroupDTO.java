package com.eurodyn.qlack.fuse.settings.dto;

import java.io.Serializable;

public class QFSGroupDTO implements Serializable {

  private static final long serialVersionUID = -3330713494152798837L;
  private String name;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return "QFSGroupDTO [name=" + name + "]";
  }
}
