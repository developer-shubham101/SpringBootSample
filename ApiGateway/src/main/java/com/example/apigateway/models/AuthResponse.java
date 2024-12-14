package com.example.apigateway.models;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
  private String userId;
  private String accessToken;
  private String refreshToken;

  private long expireAt;

  private List<String> authorities;

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  public void setExpireAt(long expireAt) {
    this.expireAt = expireAt;
  }

  public void setRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
  }

  public void setAuthorities(List<String> authorities) {
    this.authorities = authorities;
  }

  public long getExpireAt() {
    return expireAt;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getUserId() {
    return userId;
  }

  public List<String> getAuthorities() {
    return authorities;
  }

  public String getAccessToken() {
    return accessToken;
  }

  public String getRefreshToken() {
    return refreshToken;
  }
}
