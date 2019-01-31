package com.eurodyn.qlack.fuse.aaa.dto;

import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author European Dynamics SA
 */
@Getter
@Setter
@NoArgsConstructor
public class UserGroupDTO extends BaseDTO {

  private String name;
  private String objectId;
  private String description;
  private String parentId;
  private Set<UserGroupDTO> children;

  public UserGroupDTO(String id) {
    setId(id);
  }

}
