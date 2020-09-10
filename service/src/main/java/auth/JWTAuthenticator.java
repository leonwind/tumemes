package auth;

import accessors.UserDAO;
import core.User;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.jsonwebtoken.*;

import javax.xml.bind.DatatypeConverter;
import java.util.Optional;

public class JWTAuthenticator implements Authenticator<JWTCredentials, User> {
  private final UserDAO userDAO;
  private final String secretKey;

  public JWTAuthenticator(UserDAO userDAO, String secretKey) {
    this.userDAO = userDAO;
    this.secretKey = secretKey;
  }

  private boolean isEmail(String username) {
    return username.indexOf('@') != -1;
  }

  @Override
  public Optional<User> authenticate(JWTCredentials credentials) throws AuthenticationException {
    try {
      Claims claims =
          Jwts.parser()
              .setSigningKey(DatatypeConverter.parseBase64Binary(this.secretKey))
              .parseClaimsJws(credentials.getJwtToken())
              .getBody();

      User user;
      if (isEmail(claims.getSubject())) {
        user = userDAO.getUserByEmail(claims.getSubject());
      } else {
        user = userDAO.getUserByUsername(claims.getSubject());
      }
      return Optional.ofNullable(user);
    } catch (ExpiredJwtException
        | UnsupportedJwtException
        | MalformedJwtException
        | SignatureException
        | IllegalArgumentException e) {
      return Optional.empty();
    }
  }
}
