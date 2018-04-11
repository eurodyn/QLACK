package com.eurodyn.qlack.fuse.aaa.dto;

import java.io.Serializable;

/**
 * QFAUser attributes DTO
 *
 * @author European Dynamics
 */
public class QFAUserAttributeDTO implements Serializable {

  private String id;
  private String name;
  private String data;
  private String userId;
  private byte[] binData;
  private String contentType;

  public QFAUserAttributeDTO() {
  }

  public QFAUserAttributeDTO(String name, String data) {
    this.name = name;
    this.data = data;
  }

  public QFAUserAttributeDTO(String columnName, String columnData, String userId) {
    this.name = columnName;
    this.data = columnData;
    this.userId = userId;
  }

  public QFAUserAttributeDTO(String columnName, String columnData,
      byte[] columnBinData, String userID, String contentType) {
    this.name = columnName;
    this.binData = columnBinData;
    this.userId = userID;
    this.data = columnData;
    this.contentType = contentType;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getData() {
    return data;
  }

  public void setData(String data) {
    this.data = data;
  }

  public byte[] getBinData() {
    return binData;
  }

  public void setBinData(byte[] binData) {
    this.binData = binData;
  }

  /**
   * @return the userId
   */
  public String getUserId() {
    return userId;
  }

  /**
   * @param userId the userId to set
   */
  public void setUserId(String userId) {
    this.userId = userId;
  }

  /**
   * @return the contentType
   */
  public String getContentType() {
    return contentType;
  }

  /**
   * @param contentType the contentType to set
   */
  public void setContentType(String contentType) {
    this.contentType = contentType;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

}
