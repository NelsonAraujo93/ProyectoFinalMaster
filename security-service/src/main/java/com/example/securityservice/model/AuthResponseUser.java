package com.example.securityservice.model;

import com.example.securityservice.dto.UserDTO;

public class AuthResponseUser {
  private String token;
  private UserDTO data; // Changed to hold user data
  private boolean isTokenBlacklisted;

  public AuthResponseUser(String token, UserDTO data, boolean isTokenBlacklisted) {
    this.token = token;
    this.data = data;
    this.isTokenBlacklisted = isTokenBlacklisted;
  }

  public AuthResponseUser(String message, boolean isTokenBlacklisted) {
    this.token = message;
    this.isTokenBlacklisted = isTokenBlacklisted;
  }

  // Getters and Setters
  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public UserDTO getData() {
    return data;
  }

  public void setData(UserDTO data) {
    this.data = data;
  }

  public boolean isTokenBlacklisted() {
    return isTokenBlacklisted;
  }

  public void setTokenBlacklisted(boolean tokenBlacklisted) {
    isTokenBlacklisted = tokenBlacklisted;
  }

  public UserDTO getUserDTO() {
    return data;
  }

  public void setUserDTO(UserDTO userDTO) {
    this.data = userDTO;
  }
}
