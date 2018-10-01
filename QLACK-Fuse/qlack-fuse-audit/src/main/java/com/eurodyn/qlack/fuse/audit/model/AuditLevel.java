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

//  public static AuditLevel findByName(EntityManager em, String name) {
//    try {
//      String id = cache.get(name, new Callable<String>() {
//        @Override
//        public String call() throws Exception {
//          Query q = em.createQuery("SELECT l FROM AuditLevel l WHERE l.name = :name");
//          q.setParameter("name", name);
//          List<AuditLevel> resultList = q.getResultList();
//          return q.getResultList().isEmpty() ? null : resultList.get(0).getId();
//        }
//      });
//      return em.find(AuditLevel.class, id);
//    } catch (ExecutionException e) {
//      throw new RuntimeException("Unexpected checked exception thrown.", e);
//    }
//  }

  public static void clearCache() {
    cache.invalidateAll();
  }

//  public static List<AuditLevel> findAll(EntityManager em) {
//    Query q = em.createQuery("SELECT l FROM AuditLevel l");
//    return q.getResultList();
//  }

}
