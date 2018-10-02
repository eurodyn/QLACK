package com.eurodyn.qlack.fuse.audit.model;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "al_audit_level")
@Getter
@Setter
public class AuditLevel extends AuditBaseEntity{

  private static Cache<String, String> cache = CacheBuilder.newBuilder().build();
  private String name;
  private String description;
  @Column(name = "prin_session_id")
  private String prinSessionId;
  @Column(name = "created_on")
  private Long createdOn;

  public AuditLevel() {
    setId(java.util.UUID.randomUUID().toString());
  }

  public static void clearCache() {
    cache.invalidateAll();
  }

}
