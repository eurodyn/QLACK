package com.eurodyn.qlack.fuse.search.dto;

import com.eurodyn.qlack.fuse.search.QFESIndexingService;

import java.io.Serializable;

/**
 * Holds a document to be indexed. The source object to be indexed is specified
 * on the <i>sourceObject</i> property and can be internally converted to a JSON
 * string by the underling methods of {@link QFESIndexingService} when convertToJSON
 * is true.
 */
public class QFESIndexingDTO extends QFESESDocumentIdentifierDTO implements Serializable {

  /**
   * The source object to be indexed.
   */
  private Object sourceObject;

  /**
   * Whether to convert sourceObject to JSON or not
   */
  private boolean convertToJSON = true;

  public QFESIndexingDTO() {
  }

  public QFESIndexingDTO(String index, String type, String id, Object sourceObject) {
    this(index, type, id, sourceObject, false);
  }

  public QFESIndexingDTO(String index, String type, String id, Object sourceObject,
      boolean refresh) {
    super(index, type, id, refresh);
    this.sourceObject = sourceObject;
  }

  /**
   * @return the sourceObject
   */
  public Object getSourceObject() {
    return sourceObject;
  }

  /**
   * @param sourceObject the sourceObject to set
   */
  public void setSourceObject(Object sourceObject) {
    this.sourceObject = sourceObject;
  }

  public boolean isConvertToJSON() {
    return convertToJSON;
  }

  public void setConvertToJSON(boolean convertToJSON) {
    this.convertToJSON = convertToJSON;
  }
}
