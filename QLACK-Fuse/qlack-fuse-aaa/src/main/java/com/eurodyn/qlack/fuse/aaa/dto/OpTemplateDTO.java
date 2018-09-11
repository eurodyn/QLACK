package com.eurodyn.qlack.fuse.aaa.dto;

import java.io.Serializable;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

/**
 * @author European Dynamics SA
 */
@Getter
@Setter
public class OpTemplateDTO implements Serializable {

  private String id;
  private String name;
  private String description;
  private String createdBy;
  private Set<OperationAccessDTO> operations;

}
