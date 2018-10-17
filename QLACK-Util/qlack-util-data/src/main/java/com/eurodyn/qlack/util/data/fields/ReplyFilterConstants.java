package com.eurodyn.qlack.util.data.fields;

/**
 * A set of utility constants.
 */
public class ReplyFilterConstants {

  private ReplyFilterConstants() {
  }

  // The default fields that should be preserved while filtering in order to have a working
  // {@link org.springframework.data.domain.Page} result type. It is the responsibility of the user
  // of {@link ReplyFilter} to add these fields into the annotation.
  //
  // The `content` field of {@link org.springframework.data.domain.Page} is not part of the default
  // fields below as it is expected to be the one the callerer of {@link org.springframework.data.domain.Page}
  // will use to filter by.
  //
  // Example:
  // @ReplyFilter(ReplyFilterConstants.SPRING_PAGE_DEFAULT_FIELDS + ",content[field1,field2,-field3]")
  public final static String SPRING_PAGE_DEFAULT_FIELDS = "first,last,number,numberOfElements,pageable,size,sort,totalElements,totalPages";
}
