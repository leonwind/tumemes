package auth;

import core.User;
import io.dropwizard.auth.Authorizer;

public class UserAuthorizer implements Authorizer<User> {

  /**
   * Decides if access is granted for given principal in the given role
   * Till now we do not have any admin roles so every resource is allowed
   */
  @Override
  public boolean authorize(User user, String role) {
    return user != null;
  }
}
