package com.eurodyn.qlack.util.dto;

/**
 * A placeholder for an encoded JWT.
 */
public class JWTTokenDTO {
  private String jwt;

  public String getJwt() {
    return jwt;
  }

  public void setJwt(String jwt) {
    this.jwt = jwt;
  }

  public JWTTokenDTO() {

  }

  public JWTTokenDTO(String jwt) {
    this.jwt = jwt;
  }

}
