package com.eurodyn.qlack.fuse.aaa.util;

import com.eurodyn.qlack.fuse.aaa.dto.UserDTO;
import com.eurodyn.qlack.fuse.aaa.model.User;

/**
 * @author European Dynamics S.A.
 */
public class AaaUserSearchHelper {

  private UserDTO userDTO;


  public AaaUserSearchHelper(User userEntity, Object sortCriterion) {
    //sortCriterion is ignored since it is only included in the query
    //for compatibility with certain DBs.
    userDTO = ConverterUtil.userToUserDTO(userEntity);
  }


  public UserDTO getUserDTO() {
    return userDTO;
  }
}
