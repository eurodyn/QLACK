package com.eurodyn.qlack.fuse.lexicon.dto;

import java.io.Serializable;

public class GroupDTO implements Serializable {

  private static final long serialVersionUID = 588067576420029887L;

  private String id;
  private String title;
  private String description;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}
