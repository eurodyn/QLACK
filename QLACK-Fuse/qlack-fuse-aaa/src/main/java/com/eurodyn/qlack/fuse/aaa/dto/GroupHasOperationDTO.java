package com.eurodyn.qlack.fuse.aaa.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GroupHasOperationDTO extends BaseDTO {

  private GroupDTO groupDTO;
  private OperationDTO operationDTO;
  private ResourceDTO resourceDTO;
  private boolean deny;
}
