package auth;

import accessors.UserDAO;
import core.User;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
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

  private String generateHash(String password, String salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
    final int NUM_ITERATIONS = 64436; // 65536 = 2^16
    final int KEY_LENGTH = 128;

    byte[] saltBytes = salt.getBytes();

    KeySpec spec = new PBEKeySpec(password.toCharArray(), saltBytes, NUM_ITERATIONS, KEY_LENGTH);

    SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
    byte[] hash = f.generateSecret(spec).getEncoded();

    return Base64.getEncoder().encodeToString(hash);
  }

  @Override
  public Optional<User> authenticate(BasicCredentials credentials) throws AuthenticationException {
    User user;
    if (isEmail(credentials.getUsername())) {
      user = userDAO.getUserByEmail(credentials.getUsername());
    } else {
      user = userDAO.getUserByUsername(credentials.getUsername());
    }

    try{
      String hashedPassword = generateHash(credentials.getPassword(), user.getSalt());
      if (hashedPassword.equals(user.getHash())) {
        return Optional.of(user);
      }

    } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
      return Optional.empty();
    }
    return Optional.empty();
  }
}
