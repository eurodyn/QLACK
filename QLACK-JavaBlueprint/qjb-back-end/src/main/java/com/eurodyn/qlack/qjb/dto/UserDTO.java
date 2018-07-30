package com.eurodyn.qlack.qjb.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
  private String id;
  private String firstName;
  private String lastName;
  private Instant createdOn;
  private LocalDate dateOfBirth;
  private byte[] photo;
}
