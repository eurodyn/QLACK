package com.eurodyn.qlack.fuse.aaa.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class QFAUserDTO implements Serializable {

  private String id;
  private String username;
  private String password;
  private byte status;
  private boolean superadmin;
  private boolean external;
  private Set<QFAUserAttributeDTO> userAttributes;
  // The session Id created for this user. Expect this to be populated, only, when attempting to
  // login the user.
  private String sessionId;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  /**
   * @return the username
   */
  public String getUsername() {
    return username;
  }

  /**
   * @param username the username to set
   */
  public void setUsername(String username) {
    this.username = username;
  }

  /**
   * @return the password
   */
  public String getPassword() {
    return password;
  }

  /**
   * @param password the password to set
   */
  public void setPassword(String password) {
    this.password = password;
  }

  /**
   * @return the status
   */
  public byte getStatus() {
    return status;
  }

  /**
   * @param status the status to set
   */
  public void setStatus(byte status) {
    this.status = status;
  }

  public boolean isSuperadmin() {
    return superadmin;
  }

  public void setSuperadmin(boolean superadmin) {
    this.superadmin = superadmin;
  }

  public boolean isExternal() {
    return external;
  }

  public void setExternal(boolean external) {
    this.external = external;
  }

  /**
   * @return Set of QFAUserAttributeDTO
   */
  public Set<QFAUserAttributeDTO> getUserAttributes() {
    return userAttributes;
  }

  /**
   * @param userAttributes The list of attributes to set.
   */
  public void setUserAttributes(Set<QFAUserAttributeDTO> userAttributes) {
    this.userAttributes = userAttributes;
  }

  public QFAUserAttributeDTO getAttribute(String name) {
    QFAUserAttributeDTO retVal = null;
    if (userAttributes != null) {
      for (QFAUserAttributeDTO userAttributesDTO : userAttributes) {
        if (userAttributesDTO.getName().equalsIgnoreCase(name)) {
          retVal = userAttributesDTO;
          break;
        }
      }
    }
    return retVal;
  }

  /**
   * Returns the string representation of the value of an attribute.
   *
   * @param name The name of the attribute.
   * @return The value of the attribute.
   */
  public String getAttributeData(String name) {
    String retVal = null;
    if (userAttributes != null) {
      for (QFAUserAttributeDTO userAttributesDTO : userAttributes) {
        if (userAttributesDTO.getName().equalsIgnoreCase(name)) {
          retVal = userAttributesDTO.getData();
          break;
        }
      }
    }
    return retVal;
  }

  /**
   * Returns the binary data of the given attribute.
   *
   * @param name The name of the attribute to search for.
   * @return The value of the binary attribute.
   */
  public byte[] getAttributeBinData(String name) {
    byte[] retVal = null;
    if (userAttributes != null) {
      for (QFAUserAttributeDTO userAttributesDTO : userAttributes) {
        if (userAttributesDTO.getName().equalsIgnoreCase(name)) {
          retVal = userAttributesDTO.getBinData();
          break;
        }
      }
    }
    return retVal;
  }

  public void setAttribute(QFAUserAttributeDTO attribute) {
    boolean found = false;
    if (userAttributes != null) {
      for (QFAUserAttributeDTO userAttributesDTO : userAttributes) {
        if (userAttributesDTO.getName().equalsIgnoreCase(attribute.getName())) {
          userAttributesDTO.setData(attribute.getData());
          userAttributesDTO.setBinData(attribute.getBinData());
          userAttributesDTO.setContentType(attribute.getContentType());
          found = true;
          break;
        }
      }
    }
    if (!found) {
      if (userAttributes == null) {
        userAttributes = new HashSet<>();
      }
      attribute.setUserId(id);
      userAttributes.add(attribute);
    }
  }

  public String getSessionId() {
    return sessionId;
  }

  public void setSessionId(String sessionId) {
    this.sessionId = sessionId;
  }
}
