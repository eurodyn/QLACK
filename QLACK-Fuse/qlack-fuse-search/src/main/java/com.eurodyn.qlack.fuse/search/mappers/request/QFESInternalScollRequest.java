package com.eurodyn.qlack.fuse.search.mappers.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class QFESInternalScollRequest {

  private String scroll;
  @JsonProperty("scroll_id")
  private String scrollId;

  public String getScroll() {
    return scroll;
  }

  public void setScroll(String scroll) {
    this.scroll = scroll;
  }

  public String getScrollId() {
    return scrollId;
  }

  public void setScrollId(String scrollId) {
    this.scrollId = scrollId;
  }
}
