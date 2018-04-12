package com.eurodyn.qlack.fuse.lexicon.criteria;

import com.eurodyn.qlack.common.search.QCPagingParams;

public class QFLKeySearchCriteria {

  private String groupId;
  private String keyName;
  private boolean ascending = true;
  private QCPagingParams paging;

  public String getGroupId() {
    return groupId;
  }

  private void setGroupId(String groupId) {
    this.groupId = groupId;
  }

  public String getKeyName() {
    return keyName;
  }

  private void setKeyName(String keyName) {
    this.keyName = keyName;
  }

  public boolean isAscending() {
    return ascending;
  }

  private void setAscending(boolean ascending) {
    this.ascending = ascending;
  }

  public QCPagingParams getPaging() {
    return paging;
  }

  private void setPaging(QCPagingParams paging) {
    this.paging = paging;
  }

  public enum SortType {
    ASCENDING,
    DESCENDING
  }

  public static class KeySearchCriteriaBuilder {

    private QFLKeySearchCriteria criteria;
    private QCPagingParams paging;

    private KeySearchCriteriaBuilder() {
      criteria = new QFLKeySearchCriteria();
    }

    public static KeySearchCriteriaBuilder createCriteria() {
      return new KeySearchCriteriaBuilder();
    }

    public QFLKeySearchCriteria build() {
      criteria.setPaging(paging);
      return criteria;
    }

    public KeySearchCriteriaBuilder withNameLike(String name) {
      criteria.setKeyName(name);
      return this;
    }

    public KeySearchCriteriaBuilder inGroup(String groupId) {
      criteria.setGroupId(groupId);
      return this;
    }

    public KeySearchCriteriaBuilder withPageSize(int pageSize) {
      if (paging == null) {
        paging = new QCPagingParams();
      }
      paging.setPageSize(pageSize);
      return this;
    }

    public KeySearchCriteriaBuilder getPage(int page) {
      if (paging == null) {
        paging = new QCPagingParams();
      }
      paging.setCurrentPage(page);
      return this;
    }
  }
}
