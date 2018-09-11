package com.eurodyn.qlack.fuse.aaa.dto;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * @author European Dynamics
 */
@Getter
@Setter
public class ResourceDTO implements Serializable {

  private String id;
  private String name;
  private String description;
  private String objectID;

}
