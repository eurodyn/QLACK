package com.eurodyn.qlack.fuse.search.request;

public class QFESScrollRequest extends QFESBaseRequest {

  private Integer scroll;
  private String scrollId;

  public Integer getScroll() {
    return scroll;
  }

  public QFESScrollRequest setScroll(Integer scroll) {
    this.scroll = scroll;
    return this;
  }

  public String getScrollId() {
    return scrollId;
  }

  public QFESScrollRequest setScrollId(String scrollId) {
    this.scrollId = scrollId;
    return this;
  }
}
