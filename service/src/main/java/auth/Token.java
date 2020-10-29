package auth;

import accessors.UserDAO;
import core.User;
import io.dropwizard.auth.AuthenticationException;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.time.Duration;
import java.util.Date;
import java.util.Optional;

public class Token {

  private static final Logger log = LoggerFactory.getLogger(Token.class);

  public static String createToken(String secretKey, String subject, Duration TTL) {
    SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
    long currMillis = System.currentTimeMillis();
    Date currDate = new Date(currMillis);

    Date expirationDate = new Date(currMillis + TTL.toMillis());

    byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secretKey);

    Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

    JwtBuilder builder =
        Jwts.builder()
            .setIssuedAt(currDate)
            .setExpiration(expirationDate)
            .setSubject(subject)
            .signWith(signatureAlgorithm, signingKey);
    return builder.compact();
  }

  public static Optional<User> verifyToken(UserDAO userDAO, String secretKey, String jwtToken)
      throws AuthenticationException {
    try {
      Claims claims =
          Jwts.parser()
              .setSigningKey(DatatypeConverter.parseBase64Binary(secretKey))
              .parseClaimsJws(jwtToken)
              .getBody();
      User user = userDAO.getUserByEmail(claims.getSubject());
      return Optional.of(user);
    } catch (ExpiredJwtException
        | UnsupportedJwtException
        | MalformedJwtException
        | SignatureException
        | IllegalArgumentException e) {
      log.info(e.getMessage());
      return Optional.empty();
    }
  }
}
