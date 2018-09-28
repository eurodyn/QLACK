package com.eurodyn.qlack.fuse.aaa.dto;

import java.util.Set;
import lombok.Getter;
import lombok.Setter;

/**
 * This is transfer object for AaaSession entity.
 *
 * @author European Dynamics SA
 */
@Getter
@Setter
public class SessionDTO extends BaseDTO {

  private String userId;
  private long createdOn;
  private Long terminatedOn;
  private String applicationSessionID;
  private Set<SessionAttributeDTO> sessionAttributes;

}