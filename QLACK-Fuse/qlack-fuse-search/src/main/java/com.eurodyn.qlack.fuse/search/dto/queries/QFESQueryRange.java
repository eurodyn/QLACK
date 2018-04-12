package com.eurodyn.qlack.fuse.search.dto.queries;

/**
 * The term query finds documents that contain the exact terms specified in the
 * inverted index. Example:
 *
 * <pre>
 * new QFESQueryTerms()
 * 		.setTerms("fooField", "bar", "bar1")
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

public class QFESQueryRange extends QFESQuerySpec {

  private String field;
  private Object fromValue;
  private Object toValue;

  public QFESQueryRange setTerm(String field, Object fromValue, Object toValue) {
    this.field = field;
    this.fromValue = fromValue;
    this.toValue = toValue;
    return this;
  }

  public String getField() {
    return field;
  }

  public Object getFromValue() {
    return fromValue;
  }

  public Object getToValue() {
    return toValue;
  }

}
