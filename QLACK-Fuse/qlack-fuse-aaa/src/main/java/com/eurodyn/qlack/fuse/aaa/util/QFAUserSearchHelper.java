package com.eurodyn.qlack.fuse.aaa.util;

import com.eurodyn.qlack.fuse.aaa.dto.QFAUserDTO;
import com.eurodyn.qlack.fuse.aaa.model.QFAUser;

/**
 * @author European Dynamics S.A.
 */
public class QFAUserSearchHelper {

  private QFAUserDTO userDTO;


  public QFAUserSearchHelper(QFAUser userEntity, Object sortCriterion) {
    //sortCriterion is ignored since it is only included in the query
    //for compatibility with certain DBs.
    userDTO = QFAConverterUtil.userToUserDTO(userEntity);
  }


  public QFAUserDTO getUserDTO() {
    return userDTO;
  }
}
