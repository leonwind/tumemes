package auth;

import accessors.UserDAO;
import core.User;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;

import java.util.Optional;

public class JWTAuthenticator implements Authenticator<JWTCredentials, User> {
  private final UserDAO userDAO;
  private final String secretKey;

  public JWTAuthenticator(UserDAO userDAO, String secretKey) {
    this.userDAO = userDAO;
    this.secretKey = secretKey;
  }

  @Override
  public Optional<User> authenticate(JWTCredentials credentials) throws AuthenticationException {
    return Token.verifyToken(this.userDAO, this.secretKey, credentials.getJwtToken());
  }
}
