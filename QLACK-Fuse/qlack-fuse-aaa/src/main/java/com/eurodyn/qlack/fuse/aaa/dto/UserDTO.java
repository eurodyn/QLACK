package com.eurodyn.qlack.fuse.aaa.dto;

import com.eurodyn.qlack.common.annotation.ResourceId;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO extends BaseDTO {

    @ResourceId("id")
    private String id;

    private String username;

    private String password;

    private byte status;

    private boolean superadmin;

    private boolean external;

    private Set<UserAttributeDTO> userAttributes;

    /**
     * The session Id created for this user. Expect this to be populated
     * only when attempting to login the user.
     */
    private String sessionId;

//  public UserAttributeDTO getAttribute(String name) {
//    UserAttributeDTO retVal = null;
//    if (userAttributes != null) {
//      for (UserAttributeDTO userAttributesDTO : userAttributes) {
//        if (userAttributesDTO.getName().equalsIgnoreCase(name)) {
//          retVal = userAttributesDTO;
//          break;
//        }
//      }
//    }
//    return retVal;
//  }
//
//  /**
//   * Returns the string representation of the value of an attribute.
//   *
//   * @param name The name of the attribute.
//   * @return The value of the attribute.
//   */
//  public String getAttributeData(String name) {
//    String retVal = null;
//    if (userAttributes != null) {
//      for (UserAttributeDTO userAttributesDTO : userAttributes) {
//        if (userAttributesDTO.getName().equalsIgnoreCase(name)) {
//          retVal = userAttributesDTO.getData();
//          break;
//        }
//      }
//    }
//    return retVal;
//  }
//
//  /**
//   * Returns the binary data of the given attribute.
//   *
//   * @param name The name of the attribute to search for.
//   * @return The value of the binary attribute.
//   */
//  public byte[] getAttributeBinData(String name) {
//    byte[] retVal = null;
//    if (userAttributes != null) {
//      for (UserAttributeDTO userAttributesDTO : userAttributes) {
//        if (userAttributesDTO.getName().equalsIgnoreCase(name)) {
//          retVal = userAttributesDTO.getBinData();
//          break;
//        }
//      }
//    }
//    return retVal;
//  }
//
//  public void setAttribute(UserAttributeDTO attribute) {
//    boolean found = false;
//    if (userAttributes != null) {
//      for (UserAttributeDTO userAttributesDTO : userAttributes) {
//        if (userAttributesDTO.getName().equalsIgnoreCase(attribute.getName())) {
//          userAttributesDTO.setData(attribute.getData());
//          userAttributesDTO.setBinData(attribute.getBinData());
//          userAttributesDTO.setContentType(attribute.getContentType());
//          found = true;
//          break;
//        }
//      }
//    }
//    if (!found) {
//      if (userAttributes == null) {
//        userAttributes = new HashSet<>();
//      }
//      attribute.setUserId(id);
//      userAttributes.add(attribute);
//    }
//  }

}
