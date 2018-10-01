package com.eurodyn.qlack.fuse.lexicon.criteria;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.eurodyn.qlack.common.search.PagingParams;
import com.eurodyn.qlack.fuse.lexicon.model.Key;
import com.eurodyn.qlack.fuse.lexicon.model.Language;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class KeySearchCriteria {

  private String groupId;
  private String keyName;
  private boolean ascending = true;
//  private PagingParams paging;
  private Pageable pageable;

//  public String getGroupId() {
//    return groupId;
//  }
//
//  private void setGroupId(String groupId) {
//    this.groupId = groupId;
//  }
//
//  public String getKeyName() {
//    return keyName;
//  }
//
//  private void setKeyName(String keyName) {
//    this.keyName = keyName;
//  }
//
//  public boolean isAscending() {
//    return ascending;
//  }
//
//  private void setAscending(boolean ascending) {
//    this.ascending = ascending;
//  }

  public enum SortType {
    ASCENDING,
    DESCENDING
  }

  public static class KeySearchCriteriaBuilder {

    private KeySearchCriteria criteria;
    private Pageable pageable;

    private KeySearchCriteriaBuilder() {
      criteria = new KeySearchCriteria();
    }

    public static KeySearchCriteriaBuilder createCriteria() {
      return new KeySearchCriteriaBuilder();
    }

    public KeySearchCriteria build() {
      criteria.setPageable(pageable);
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

    public KeySearchCriteriaBuilder setPageSizeWithPageNum(int pageSize, int page) {
    	  pageable = PageRequest.of(page, pageSize);
      return this;
    }

//    public KeySearchCriteriaBuilder getPage(int page) {
//      if (pageable == null) {
//    	  pageable = PageRequest.of(page);
//      }
//      return this;
//    }
  }
}
