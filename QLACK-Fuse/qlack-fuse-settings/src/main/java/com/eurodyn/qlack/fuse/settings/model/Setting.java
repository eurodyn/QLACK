package com.eurodyn.qlack.fuse.settings.model;

import java.time.Instant;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "set_setting")
@Getter
@Setter
public class Setting implements java.io.Serializable {

  @Id
  private String id;

  @Version
  private long dbversion;

  private String owner;

  @Column(name = "group_name")
  private String group;

  @Column(name = "key_name")
  private String key;

  private String val;

  @Column(name = "sensitivity")
  private boolean sensitive;

  @Column(name = "psswrd")
  private Boolean password;

  @Column(name = "created_on")
  private long createdOn;

  public Setting() {
    this.id = UUID.randomUUID().toString();
    this.createdOn = Instant.now().toEpochMilli();
  }

  @Override
  public String toString() {
    return "Setting [id=" + id + ", dbversion=" + dbversion + ", owner=" + owner + ", group="
        + group + ", key="
        + key + ", val=" + val + ", createdOn=" + createdOn + "]";
  }

}
