package com.eurodyn.qlack.fuse.search.dto.queries;

/**
 * A query performing a query string against ES. A query that uses a query parser in order to parse its content
 * Multiple fields can be specified. Simple Query String is like the Query String query but will never throw an exception,
 * and discards invalid parts of the query
 *
 * <pre>
 * new QFESSimpleQueryString()
 * 		.setTerm("fooField", "foo", "AND")
 * 		.setIndex("bar")
 * 		.setType("FooBarDTO")
 * 		.setPageSize(10)
 * 		.setStartRecord(0)
 * 		.setExplain(false);
 * </pre>
 *
 * See also:<br>
 * https://www.elastic.co/guide/en/elasticsearch/reference/1.7/query-dsl-match-
 * query.html
 */
public class QFESSimpleQueryString extends QFESQuerySpec {

  // The field to execute the search against.
  private String field;

  // The value to lookup.
  private Object value;
  // The field to execute the search against.
  private String operator;

  /**
   * A convenience method to set the term of this query.
   *
   * @param field The field name to search against.
   * @param value The value to search.
   */
  public QFESSimpleQueryString setTerm(String field, Object value, String operator) {
    this.field = field;
    this.value = value;
    this.operator = operator;

    return this;
  }

  /**
   * @return the field
   */
  public String getField() {
    return field;
  }

  /**
   * @param field the field to set
   */
  public void setField(String field) {
    this.field = field;
  }

  /**
   * @return the value
   */
  public Object getValue() {
    return value;
  }

  /**
   * @param value the value to set
   */
  public void setValue(Object value) {
    this.value = value;
  }

  /**
   * @return the operator
   */
  public String getOperator() {
    return operator;
  }

  /**
   * @param operator the value to set
   */
  public void setOperator(String operator) {
    this.operator = operator;
  }

}
