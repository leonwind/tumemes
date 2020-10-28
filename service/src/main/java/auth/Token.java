package auth;

import accessors.UserDAO;
import core.User;
import io.dropwizard.auth.AuthenticationException;
import io.jsonwebtoken.*;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.time.Duration;
import java.util.Date;
import java.util.Optional;

public class Token {

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
      System.out.println(e.getMessage());
      return Optional.empty();
    }
  }
}
