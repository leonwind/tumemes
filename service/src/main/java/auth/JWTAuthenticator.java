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

  @Override
  public Optional<User> authenticate(JWTCredentials credentials) throws AuthenticationException {
    try {
      Claims claims =
          Jwts.parser()
              .setSigningKey(DatatypeConverter.parseBase64Binary(this.secretKey))
              .parseClaimsJws(credentials.getJwtToken())
              .getBody();
      User user = userDAO.getUserByEmail(claims.getSubject());
      return Optional.of(user);
    } catch (ExpiredJwtException
        | UnsupportedJwtException
        | MalformedJwtException
        | SignatureException
        | IllegalArgumentException e) {
      return Optional.empty();
    }
  }
}
