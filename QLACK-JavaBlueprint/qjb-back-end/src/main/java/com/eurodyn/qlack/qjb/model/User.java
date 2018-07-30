package com.eurodyn.qlack.qjb.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class User {

  @Id
  @Column(nullable = false, length = 36)
  private String id;

  @Column(name="first_name", nullable = false)
  private String firstName;

  @Column(name="last_name", nullable = false)
  private String lastName;

  @Column(name="created_on", nullable = false)
  private Instant createdOn;

  @Column(name="dob", nullable = false)
  private LocalDate dateOfBirth;

  @Lob
  private byte[] photo;
}
