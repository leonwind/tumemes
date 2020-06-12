package core;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.security.Principal;

public class User implements Principal {

  private final String name;
  private final String email;
  private String password;

  public User(@JsonProperty("username") String name,
              @JsonProperty("email") String email,
              @JsonProperty("password") String password) {
    this.name = name;
    this.email = email;
    this.password = password;
  }

  @Override
  public String getName() {
    return name;
  }

  public String getEmail() {
    return email;
  }

  public String getPassword() {
    return password;
  }
}
