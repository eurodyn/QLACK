package com.eurodyn.qlack.fuse.search.dto.queries;

/**
 * A query that uses a query parser in order to parse its content. Example:
 *
 * <pre>
 * new QFESQueryString()
 * 		.setQueryString("foo")
 * 		// .setQueryString("foo*")
 * 		// .setQueryString("foo AND bar")
 * 		// .setQueryString("foo -bar")
 * 		.setIndex("bar")
 * 		.setType("FooBarDTO")
 * 		.setPageSize(10)
 * 		.setStartRecord(0)
 * 		.setExplain(false);
 *
 * </pre>
 *
 * See also:<br>
 * https://www.elastic.co/guide/en/elasticsearch/reference/1.7/query-dsl-query-
 * string-query.html
 */
public class QFESQueryString extends QFESQuerySpec {

  private String queryString;

  /**
   * @return the queryString
   */
  public String getQueryString() {
    return queryString;
  }

  public QFESQueryString setQueryString(String queryString) {
    this.queryString = queryString;

    return this;
  }

}
