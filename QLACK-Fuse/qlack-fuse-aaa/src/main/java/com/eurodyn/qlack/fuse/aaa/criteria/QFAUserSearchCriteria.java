package com.eurodyn.qlack.fuse.aaa.criteria;

import com.eurodyn.qlack.common.search.QPagingParams;
import com.eurodyn.qlack.fuse.aaa.criteria.QFAUserSearchCriteria.UserAttributeCriteria.Type;
import com.eurodyn.qlack.fuse.aaa.dto.QFAUserAttributeDTO;

import java.util.Arrays;
import java.util.Collection;

public class QFAUserSearchCriteria {

  private Collection<String> includeIds;
  private Collection<String> excludeIds;
  private Collection<String> includeGroupIds;
  private Collection<String> excludeGroupIds;
  private Collection<Byte> includeStatuses;
  private Collection<Byte> excludeStatuses;
  private String username;
  private UserAttributeCriteria attributeCriteria;
  private Boolean superadmin;
  private String sortColumn;
  private String sortAttribute;
  private boolean ascending = true;
  private QPagingParams paging;
  private QFAUserSearchCriteria() {
  }

  public Collection<String> getIncludeIds() {
    return includeIds;
  }

  private void setIncludeIds(Collection<String> includeIds) {
    this.includeIds = includeIds;
  }

  public Collection<String> getExcludeIds() {
    return excludeIds;
  }

  private void setExcludeIds(Collection<String> excludeIds) {
    this.excludeIds = excludeIds;
  }

  public Collection<String> getIncludeGroupIds() {
    return includeGroupIds;
  }

  private void setIncludeGroupIds(Collection<String> includeGroupIds) {
    this.includeGroupIds = includeGroupIds;
  }

  public Collection<String> getExcludeGroupIds() {
    return excludeGroupIds;
  }

  private void setExcludeGroupIds(Collection<String> excludeGroupIds) {
    this.excludeGroupIds = excludeGroupIds;
  }

  public Collection<Byte> getIncludeStatuses() {
    return includeStatuses;
  }

  private void setIncludeStatuses(Collection<Byte> includeStatuses) {
    this.includeStatuses = includeStatuses;
  }

  public Collection<Byte> getExcludeStatuses() {
    return excludeStatuses;
  }

  private void setExcludeStatuses(Collection<Byte> excludeStatuses) {
    this.excludeStatuses = excludeStatuses;
  }

  public String getUsername() {
    return username;
  }

  private void setUsername(String username) {
    this.username = username;
  }

  public UserAttributeCriteria getAttributeCriteria() {
    return attributeCriteria;
  }

  private void setAttributeCriteria(UserAttributeCriteria attributeCriteria) {
    this.attributeCriteria = attributeCriteria;
  }

  public Boolean getSuperadmin() {
    return superadmin;
  }

  private void setSuperadmin(Boolean superadmin) {
    this.superadmin = superadmin;
  }

  public String getSortColumn() {
    return sortColumn;
  }

  private void setSortColumn(String sortColumn) {
    this.sortColumn = sortColumn;
  }

  public String getSortAttribute() {
    return sortAttribute;
  }

  private void setSortAttribute(String sortAttribute) {
    this.sortAttribute = sortAttribute;
  }

  public boolean isAscending() {
    return ascending;
  }

  private void setAscending(boolean ascending) {
    this.ascending = ascending;
  }

  public QPagingParams getPaging() {
    return paging;
  }

  private void setPaging(QPagingParams paging) {
    this.paging = paging;
  }

  public enum SortColumn {
    USERNAME,
    STATUS,
    ATTRIBUTE
  }

  public enum SortType {
    ASCENDING,
    DESCENDING
  }

  public static class UserSearchCriteriaBuilder {

    private QFAUserSearchCriteria criteria;
    private QPagingParams paging;

    private UserSearchCriteriaBuilder() {
      criteria = new QFAUserSearchCriteria();
    }

    public static UserSearchCriteriaBuilder createCriteria() {
      return new UserSearchCriteriaBuilder();
    }

    public static UserAttributeCriteria and(QFAUserAttributeDTO... attributes) {
      UserAttributeCriteria retVal = new UserAttributeCriteria();
      retVal.setAttributes(attributes);
      retVal.setType(Type.AND);
      return retVal;
    }

    public static UserAttributeCriteria and(Collection<QFAUserAttributeDTO> attributes) {
      UserAttributeCriteria retVal = new UserAttributeCriteria();
      retVal.setAttributes(attributes);
      retVal.setType(Type.AND);
      return retVal;
    }

    public static UserAttributeCriteria or(QFAUserAttributeDTO... attributes) {
      UserAttributeCriteria retVal = new UserAttributeCriteria();
      retVal.setAttributes(attributes);
      retVal.setType(Type.OR);
      return retVal;
    }

    public static UserAttributeCriteria or(Collection<QFAUserAttributeDTO> attributes) {
      UserAttributeCriteria retVal = new UserAttributeCriteria();
      retVal.setAttributes(attributes);
      retVal.setType(Type.OR);
      return retVal;
    }

    public QFAUserSearchCriteria build() {
      // Default sorting if none was specified
      if ((criteria.getSortColumn() == null) && (criteria.getSortAttribute() == null)) {
        criteria.setSortColumn("username");
      }
      criteria.setPaging(paging);
      return criteria;
    }

    /**
     * Specify a collection of IDs in which the IDs of the retrieved users should be contained.
     */
    public UserSearchCriteriaBuilder withIdIn(Collection<String> ids) {
      criteria.setIncludeIds(ids);
      return this;
    }

    /**
     * Specify a collection of IDs in which the IDs of the retrieved users should not be contained.
     */
    public UserSearchCriteriaBuilder withIdNotIn(Collection<String> ids) {
      criteria.setExcludeIds(ids);
      return this;
    }

    /**
     * Specify a collection of IDs in which the IDs of the retrieved users groups should be contained.
     */
    public UserSearchCriteriaBuilder withGroupIdIn(Collection<String> ids) {
      criteria.setIncludeGroupIds(ids);
      return this;
    }

    /**
     * Specify a collection of IDs in which the IDs of the retrieved users groups should not be contained.
     */
    public UserSearchCriteriaBuilder withGroupIdNotIn(Collection<String> ids) {
      criteria.setExcludeGroupIds(ids);
      return this;
    }

    /**
     * Specify a collection of statuses in which the status of the retrieved users should be contained.
     *
     * @param statuses The list of statuses.
     */
    public UserSearchCriteriaBuilder withStatusIn(Collection<Byte> statuses) {
      criteria.setIncludeStatuses(statuses);
      return this;
    }

    /**
     * Specify a collection of statuses in which the status of the retrieved users should not be contained.
     *
     * @param statuses The list of statuses.
     */
    public UserSearchCriteriaBuilder withStatusNotIn(Collection<Byte> statuses) {
      criteria.setExcludeStatuses(statuses);
      return this;
    }

    /**
     * Spacify a username for which to check
     */
    public UserSearchCriteriaBuilder withUsernameLike(String username) {
      criteria.setUsername(username);
      return this;
    }

    /**
     * Specify the attributes the retrieved users should have. This method is intended to be
     * called in conjuction with the and and or static methods which are used to specify
     * the relationship between the different criteria, for example:
     * UserSearchCriteriaBuilder.createCriteria().withAttributes(and(and(att1, att2, att3), or (att4, att5), or(att6, att7)))
     */
    public UserSearchCriteriaBuilder withAttributes(UserAttributeCriteria attCriteria) {
      criteria.setAttributeCriteria(attCriteria);
      return this;
    }

    public UserSearchCriteriaBuilder withSuperadmin(boolean superadmin) {
      criteria.setSuperadmin(superadmin);
      return this;
    }

    public UserAttributeCriteria and(UserAttributeCriteria... attCriteria) {
      UserAttributeCriteria retVal = new UserAttributeCriteria();
      retVal.setAttCriteria(attCriteria);
      retVal.setType(Type.AND);
      return retVal;
    }

    public UserAttributeCriteria or(UserAttributeCriteria... attCriteria) {
      UserAttributeCriteria retVal = new UserAttributeCriteria();
      retVal.setAttCriteria(attCriteria);
      retVal.setType(Type.OR);
      return retVal;
    }

    public UserSearchCriteriaBuilder sortByColumn(SortColumn column, SortType type) {
      switch (column) {
        case USERNAME:
          criteria.setSortColumn("username");
          break;
        case STATUS:
          criteria.setSortColumn("status");
          break;
        case ATTRIBUTE:
          break;
      }
      switch (type) {
        case ASCENDING:
          criteria.setAscending(true);
          break;
        case DESCENDING:
          criteria.setAscending(false);
          break;
      }
      return this;
    }

    public UserSearchCriteriaBuilder sortByAttribute(String attributeName, SortType type) {
      criteria.setSortAttribute(attributeName);
      switch (type) {
        case ASCENDING:
          criteria.setAscending(true);
          break;
        case DESCENDING:
          criteria.setAscending(false);
          break;
      }
      return this;
    }

    public UserSearchCriteriaBuilder withPageSize(int pageSize) {
      if (paging == null) {
        paging = new QPagingParams();
      }
      paging.setPageSize(pageSize);
      return this;
    }

    public UserSearchCriteriaBuilder getPage(int page) {
      if (paging == null) {
        paging = new QPagingParams();
      }
      paging.setCurrentPage(page);
      return this;
    }
  }

  public static class UserAttributeCriteria {

    private Type type;
    private Collection<QFAUserAttributeDTO> attributes;
    private Collection<UserAttributeCriteria> attCriteria;
    private boolean useLike;
    private UserAttributeCriteria() {
    }

    public Type getType() {
      return type;
    }

    private void setType(Type type) {
      this.type = type;
    }

    public Collection<QFAUserAttributeDTO> getAttributes() {
      return attributes;
    }

    private void setAttributes(QFAUserAttributeDTO[] attributes) {
      this.attributes = Arrays.asList(attributes);
    }

    private void setAttributes(Collection<QFAUserAttributeDTO> attributes) {
      this.attributes = attributes;
    }

    public Collection<UserAttributeCriteria> getAttCriteria() {
      return attCriteria;
    }

    private void setAttCriteria(UserAttributeCriteria[] attCriteria) {
      this.attCriteria = Arrays.asList(attCriteria);
    }

    public boolean isUseLike() {
      return useLike;
    }

    public void setUseLike(boolean useLike) {
      this.useLike = useLike;
    }

    public enum Type {
      AND, OR
    }
  }
}
