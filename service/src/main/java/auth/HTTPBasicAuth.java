package auth;

import accessors.UserDAO;
import core.User;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.Optional;

public class HTTPBasicAuth implements Authenticator<BasicCredentials, User> {

  private final UserDAO userDAO;

  public HTTPBasicAuth(UserDAO userDAO) {
    this.userDAO = userDAO;
  }

  private boolean isEmail(String username) {
    return username.indexOf('@') != -1;
  }

  @Override
  public Optional<User> authenticate(BasicCredentials credentials) throws AuthenticationException {
    User user;
    if (isEmail(credentials.getUsername())) {
      user = userDAO.getUserByEmail(credentials.getUsername());
    } else {
      user = userDAO.getUserByUsername(credentials.getUsername());
    }

    // if user does not exist return unauthorized
    if (user == null) {
      return Optional.empty();
    }

    try {
      byte[] hash = Hashing.generateHash(credentials.getPassword(), user.getSalt());
      String hashedPassword = Base64.getEncoder().encodeToString(hash);

      if (hashedPassword.equals(user.getHash())) {
        return Optional.of(user);
      }

    } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
      return Optional.empty();
    }
    return Optional.empty();
  }
}
