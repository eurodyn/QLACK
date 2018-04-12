package com.eurodyn.qlack.fuse.search.request;

public class QFESUpdateMappingRequest extends QFESBaseRequest {

  private String indexName;
  private String typeName;
  private String indexMapping;

  public String getIndexName() {
    return indexName;
  }

  public void setIndexName(String indexName) {
    this.indexName = indexName;
  }

  public String getTypeName() {
    return typeName;
  }

  public void setTypeName(String typeName) {
    this.typeName = typeName;
  }

  public String getIndexMapping() {
    return indexMapping;
  }

  public void setIndexMapping(String indexMapping) {
    this.indexMapping = indexMapping;
  }
}
