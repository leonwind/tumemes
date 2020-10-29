package core;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PasswordReset {

  private final String token;
  private final String newPassword;

  public PasswordReset(
      @JsonProperty("token") String token,
      @JsonProperty("newPassword") String newPassword) {
    this.token = token;
    this.newPassword = newPassword;
  }

  public String getToken() {
    return token;
  }

  public String getNewPassword() {
    return newPassword;
  }
}
