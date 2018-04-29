package com.eurodyn.qlack.fuse.audit.model;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.Query;
import javax.persistence.Table;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

@Entity
@Table(name = "al_audit_level")
public class AuditLevel {

  private static Cache<String, String> cache = CacheBuilder.newBuilder().build();

  @Id
  private String id;
  private String name;
  private String description;
  @Column(name = "prin_session_id")
  private String prinSessionId;
  @Column(name = "created_on")
  private Long createdOn;


  public AuditLevel() {
    id = java.util.UUID.randomUUID().toString();
  }

  public String getId() {
    return this.id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return this.description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getPrinSessionId() {
    return this.prinSessionId;
  }

  public void setPrinSessionId(String prinSessionId) {
    this.prinSessionId = prinSessionId;
  }

  public Long getCreatedOn() {
    return this.createdOn;
  }

  public void setCreatedOn(Long createdOn) {
    this.createdOn = createdOn;
  }

  public static AuditLevel findByName(EntityManager em, String name) {
    try {
      String id = cache.get(name, new Callable<String>() {
        @Override
        public String call() throws Exception {
          Query q = em.createQuery("SELECT l FROM AuditLevel l WHERE l.name = :name");
          q.setParameter("name", name);
          List<AuditLevel> resultList = q.getResultList();
          return q.getResultList().isEmpty() ? null : resultList.get(0).getId();
        }
      });
      return em.find(AuditLevel.class, id);
    } catch (ExecutionException e) {
      throw new RuntimeException("Unexpected checked exception thrown.", e);
    }
  }

  public static void clearCache() {
    cache.invalidateAll();
  }

  public static List<AuditLevel> findAll(EntityManager em) {
    Query q = em.createQuery("SELECT l FROM AuditLevel l");
    return q.getResultList();
  }

}
