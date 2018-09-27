package com.eurodyn.qlack.fuse.aaa.model;

import java.io.Serializable;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public class AAAModel implements Serializable {

  @Id
  private String id;
}
