package core;

import java.security.Principal;

public class User implements Principal {

  private final String name;
  private final String email;
  private final String hash;
  private final String salt;
  private final boolean verified;

  public User(String name, String email, String hash, String salt, boolean verified) {
    this.name = name;
    this.email = email;
    this.hash = hash;
    this.salt = salt;
    this.verified = verified;
  }

  @Override
  public String getName() {
    return name;
  }

  public String getEmail() {
    return email;
  }

  public String getHash() {
    return hash;
  }

  public String getSalt() {
    return salt;
  }

  public boolean isVerified() {
    return verified;
  }
}
