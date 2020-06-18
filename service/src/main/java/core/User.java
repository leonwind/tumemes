package core;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.security.Principal;
import java.util.Objects;

public class User implements Principal {

  private final String name;
  private final String email;
  private final String password;

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

  @Override
  public int hashCode() {
    return Objects.hash(name, email, password);
  }

  @Override
  public String toString() {
    return "User{" +
        "name='" + name + '\'' +
        ", email='" + email + '\'' +
        ", password='" + password + '\'' +
        '}';
  }
}
