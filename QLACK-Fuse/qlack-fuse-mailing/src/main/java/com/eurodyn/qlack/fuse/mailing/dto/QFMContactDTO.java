package com.eurodyn.qlack.fuse.mailing.dto;

import java.io.Serializable;

/**
 * Data transfer object for contacts.
 *
 * @author European Dynamics SA.
 */
public class QFMContactDTO implements Serializable {

  private String id;
  private String userID;
  private String firstName;
  private String lastName;
  private String email;
  private String locale;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getUserID() {
    return userID;
  }

  public void setUserID(String userID) {
    this.userID = userID;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getLocale() {
    return locale;
  }

  public void setLocale(String locale) {
    this.locale = locale;
  }

}
