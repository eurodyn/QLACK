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
public class GroupDTO extends BaseDTO {

  private String name;
  private String objectID;
  private String description;
  private String parentId;
  private Set<GroupDTO> children;

  public GroupDTO(String id) {
    setId(id);
  }

}
