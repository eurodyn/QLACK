package com.eurodyn.qlack.fuse.search.dto.queries;

/**
 * The term query finds documents that contain the exact terms specified in the
 * inverted index. Example:
 *
 * <pre>
 * new QFESQueryTerms()
 * 		.setTerms("fooField", "bar")
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

public class QFESQueryTerms extends QFESQuerySpec {

  private String field;
  private Object values;

  public QFESQueryTerms setTerm(String field, Object values) {
    this.field = field;
    this.values = values;

    return this;
  }

  public String getField() {
    return field;
  }

  public Object getValues() {
    return values;
  }

}
