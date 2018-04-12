package com.eurodyn.qlack.fuse.search.dto.queries;

/**
 * The term query finds documents that contain the exact term specified in the
 * inverted index. Example:
 *
 * <pre>
 * new QFESQueryTerm()
 * 		.setTerm("fooField", "bar")
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
public class QFESQueryTerm extends QFESQuerySpec {

  private String field;
  private Object value;

  public QFESQueryTerm setTerm(String field, Object value) {
    this.field = field;
    this.value = value;

    return this;
  }

  public String getField() {
    return field;
  }

  public Object getValue() {
    return value;
  }

}
