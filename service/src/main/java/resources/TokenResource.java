package resources;

import accessors.UserDAO;
import api.TokenService;
import auth.Hashing;
import auth.JWTCredentials;
import core.NewUser;
import core.User;
import io.dropwizard.auth.AuthenticationException;
import io.jsonwebtoken.*;

import javax.annotation.security.PermitAll;
import javax.crypto.spec.SecretKeySpec;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;

public class TokenResource implements TokenService {

  private final UserDAO userDAO;
  private final String secretKey;
  // one week until token gets expired
  // time in milli seconds
  private final long TTL_MILLIS = 604800000;

  public TokenResource(UserDAO userDAO, String secretKey) {
    this.userDAO = userDAO;
    this.secretKey = secretKey;
  }

  private boolean isEmail(String username) {
    return username.indexOf('@') != -1;
  }

  private boolean isSecure(String password) {
    if (password.length() < 8) {
      return false;
    }

    boolean containsDigit = false;
    boolean containsLowerCase = false;
    boolean containsUpperCase = false;

    for (int i = 0; i < password.length(); i++) {
      char curr = password.charAt(i);

      if (Character.isDigit(curr)) {
        containsDigit = true;
        continue;
      }

      if (Character.isLowerCase(curr)) {
        containsLowerCase = true;
        continue;
      }

      if (Character.isUpperCase(curr)) {
        containsUpperCase = true;
      }
    }
    return containsDigit && containsLowerCase && containsUpperCase;
  }

  private String createToken(String subject) {
    SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
    long currMillis = System.currentTimeMillis();
    Date currDate = new Date(currMillis);
    Date expirationDate = new Date(currMillis + TTL_MILLIS);

    byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(this.secretKey);

    Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

    JwtBuilder builder =
        Jwts.builder()
            .setIssuedAt(currDate)
            .setExpiration(expirationDate)
            .setSubject(subject)
            .signWith(signatureAlgorithm, signingKey);
    return builder.compact();
  }

  private Optional<User> verifyToken(JWTCredentials credentials) throws AuthenticationException {
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

  @POST
  @Path("/register")
  @PermitAll
  @Consumes(MediaType.APPLICATION_JSON)
  public Response registerUser(NewUser newUser) {
    if (userDAO.doesUsernameExist(newUser.getName())) {
      return Response.status(400).entity("Username does already exists").build();
    }
    if (userDAO.doesEmailExists(newUser.getEmail())) {
      return Response.status(400).entity("Email does already exists").build();
    }

    if (!isSecure(newUser.getPassword())) {
      return Response.status(400)
          .entity(
              "Password should be at least 8 characters and contains one digit, "
                  + "one lowercase and one uppercase character.")
          .build();
    }

    byte[] salt = Hashing.generateSalt();
    try {
      byte[] hash = Hashing.generateHash(newUser.getPassword(), salt);

      Base64.Encoder enc = Base64.getEncoder();

      userDAO.addNewUser(
          newUser.getName(),
          newUser.getEmail(),
          enc.encodeToString(hash),
          enc.encodeToString(salt));

    } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
      e.printStackTrace();
      return Response.status(400).entity("Error occurred while hashing your password.").build();
    }
    return Response.ok("Added new user").build();
  }

  @POST
  @PermitAll
  @Consumes(MediaType.APPLICATION_JSON)
  public Response createToken(NewUser newUser) {
    return Response.status(Response.Status.UNAUTHORIZED).build();
  }
}
