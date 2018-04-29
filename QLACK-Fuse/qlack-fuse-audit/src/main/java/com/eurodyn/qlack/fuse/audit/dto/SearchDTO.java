package com.eurodyn.qlack.fuse.audit.dto;

import com.eurodyn.qlack.fuse.audit.enums.AuditLogColumns;
import com.eurodyn.qlack.fuse.audit.enums.SearchOperator;

import java.io.Serializable;
import java.util.List;


public class SearchDTO implements Serializable {

  private List<String> value;

  private AuditLogColumns column;

  private SearchOperator operator;

  public List<String> getValue() {
    return value;
  }

  public void setValue(List<String> value) {
    this.value = value;
  }

  public AuditLogColumns getColumn() {
    return column;
  }

  public void setColumn(AuditLogColumns column) {
    this.column = column;
  }

  public SearchOperator getOperator() {
    return operator;
  }

  public void setOperator(SearchOperator operator) {
    this.operator = operator;
  }

  @Override
  public String toString() {
    return "SearchDTO{" + "value=" + value + ", column=" + column
        + ", operator=" + operator + '}';
  }

}
