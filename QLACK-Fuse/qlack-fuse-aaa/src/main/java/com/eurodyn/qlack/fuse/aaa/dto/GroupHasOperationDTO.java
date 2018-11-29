package com.eurodyn.qlack.fuse.aaa.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GroupHasOperationDTO extends BaseDTO {

    private GroupDTO group;

    private OperationDTO operation;

    private ResourceDTO resource;

    private boolean deny;

}
