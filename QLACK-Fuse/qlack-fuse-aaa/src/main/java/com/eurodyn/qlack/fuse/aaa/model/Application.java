package com.eurodyn.qlack.fuse.aaa.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.Query;
import javax.persistence.Table;
import javax.persistence.Version;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "aaa_application")
public class Application {

  @Id
  private String id;
  @Version
  private long dbversion;
  @Column(name = "symbolic_name")
  private String symbolicName;
  private String checksum;
  @Column(name = "executed_on")
  private long executedOn;

  public Application() {
    id = UUID.randomUUID().toString();
  }

  public static Application findBySymbolicName(String symbolicName,
      EntityManager em) {
    Query query = em.createQuery("SELECT a FROM Application a WHERE a.symbolicName = :name");
    query.setParameter("name", symbolicName);
    List<Application> queryResult = query.getResultList();
    if (queryResult.isEmpty()) {
      return null;
    }
    return queryResult.get(0);
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getSymbolicName() {
    return symbolicName;
  }

  public void setSymbolicName(String symbolicName) {
    this.symbolicName = symbolicName;
  }

  public String getChecksum() {
    return checksum;
  }

  public void setChecksum(String checksum) {
    this.checksum = checksum;
  }

  public long getExecutedOn() {
    return executedOn;
  }

  public void setExecutedOn(long executedOn) {
    this.executedOn = executedOn;
  }

}
