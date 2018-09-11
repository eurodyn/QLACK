package com.eurodyn.qlack.fuse.aaa.dto;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GroupHasOperationDTO implements Serializable {

  private String id;
  private GroupDTO groupDTO;
  private OperationDTO operationDTO;
  private ResourceDTO resourceDTO;
  private boolean deny;

}
