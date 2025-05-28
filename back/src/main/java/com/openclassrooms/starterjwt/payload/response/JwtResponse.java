package com.openclassrooms.starterjwt.payload.response;

import lombok.Getter;
import lombok.Setter;

@Getter
public class JwtResponse {
  private final String token;
  private final String type = "Bearer";
  private final Long id;
  private final String username;
  private final String firstName;
  private final String lastName;

  private final Boolean admin;

  public JwtResponse(String accessToken, Long id, String username,String firstName, String lastName, Boolean admin) {
    this.token = accessToken;
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.username = username;
    this.admin = admin;
  }
}
