package com.eurodyn.qlack.fuse.search.dto.queries;

/**
 * 22/01/2018 : The term query finds documents that contain the exact term specified in the
 * inverted index. In addition to the simple Query Term this query searches directly in the nested objects.
 * This will additional return a inner_hits Object that contains the Id's for the matched nested
 * terms.Example:
 *
 * <pre>
 * new QFESQueryTerm()
 * 		.setTerm("searchField", "searchTerma","NestedObjectName", "idOfMatchedNestedObject")
 * 		.setIndex("foo")
 * 		.setType("FooBarDTO")
 * 		.setPageSize(10)
 * 		.setStartRecord(0)
 * 		.setExplain(false);
 * </pre>
 *
 * See also:<br>
 * https://www.elastic.co/guide/en/elasticsearch/reference/1.7/query-dsl-term-
 * query.html
 */
public class QFESQueryTermNested extends QFESQuerySpec {

  private String field;
  private Object value;
  private String path;
  // The Object name of the inner search results
  private String docvalueFields;

  public QFESQueryTermNested setTerm(String field, Object value, String path,
      String docvalueFields) {
    this.field = field;
    this.value = value;
    this.path = path;
    this.docvalueFields = docvalueFields;

    return this;
  }

  public String getField() {
    return field;
  }

  public Object getValue() {
    return value;
  }

  /**
   * @return path
   */
  public String getPath() {
    return path;
  }

  /**
   * @return DocValueFields
   */
  public String getDocvalueFields() {
    return docvalueFields;
  }

}
