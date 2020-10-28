package core;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PasswordReset {

  private final String email;
  private final String newPassword;

  public PasswordReset(
      @JsonProperty("email") String email, @JsonProperty("newPassword") String newPassword) {
    this.email = email;
    this.newPassword = newPassword;
  }

  public String getEmail() {
    return email;
  }

  public String getNewPassword() {
    return newPassword;
  }
}
