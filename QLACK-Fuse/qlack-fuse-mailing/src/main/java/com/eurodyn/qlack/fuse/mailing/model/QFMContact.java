package com.eurodyn.qlack.fuse.mailing.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "mai_contact")
public class QFMContact implements java.io.Serializable {

  @Id
  private String id;

  @Column(name = "email", nullable = false, length = 45)
  private String email;

  @Column(name = "first_name", length = 254)
  private String firstName;

  @Column(name = "last_name", length = 254)
  private String lastName;

  @Column(name = "locale", length = 5)
  private String locale;

  @Column(name = "user_id", length = 36)
  private String userId;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(name = "mai_distr_list_has_contact",
      joinColumns = {
          @JoinColumn(name = "contact_id", nullable = false, updatable = false)
      },
      inverseJoinColumns = {
          @JoinColumn(name = "distribution_list_id", nullable = false, updatable = false)
      }
  )
  private Set<QFMDistributionList> distributionLists = new HashSet<QFMDistributionList>(0);

  // -- Constructors

  public QFMContact() {
    this.id = java.util.UUID.randomUUID().toString();
  }

  // -- Accessors

  public String getId() {
    return this.id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getEmail() {
    return this.email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getFirstName() {
    return this.firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return this.lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getLocale() {
    return this.locale;
  }

  public void setLocale(String locale) {
    this.locale = locale;
  }

  public String getUserId() {
    return this.userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public Set<QFMDistributionList> getDistributionLists() {
    return this.distributionLists;
  }

  public void setDistributionLists(Set<QFMDistributionList> distributionLists) {
    this.distributionLists = distributionLists;
  }

}
