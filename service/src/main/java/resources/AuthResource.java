package resources;

import accessors.UserDAO;
import api.AuthService;
import auth.Hashing;
import core.LoginUser;
import core.NewUser;
import core.User;
import enums.AllowedEmailDomains;
import exceptions.DomainNotSupportedException;
import io.dropwizard.auth.Auth;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.spec.SecretKeySpec;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.Duration;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

public class AuthResource implements AuthService {

  private static final Logger log = LoggerFactory.getLogger(AuthResource.class);
  private final UserDAO userDAO;
  private final String secretKey;

  // one hour until normal token gets expired
  private final Duration TTL_ACCESS_TOKEN = Duration.ofHours(1);

  public AuthResource(UserDAO userDAO, String secretKey) {
    this.userDAO = userDAO;
    this.secretKey = secretKey;
  }

  private static boolean isEmailDomainSupported(String email) {
    String domain = email.substring(email.indexOf('@') + 1);
    return Arrays.stream(AllowedEmailDomains.values())
        .anyMatch(allowedDomain -> allowedDomain.toString().equals(domain));
  }

  private static boolean isEmail(String email) {
    return email.indexOf('@') != -1;
  }

  private static boolean isSecure(String password) {
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

  /** Check if the new user satisfies all the given constraints */
  private void verifyUserCredentials(NewUser newUser) throws Exception {
    if (newUser.getName().indexOf('@') != -1) {
      log.info("@ is not allowed in the username");
      throw new Exception("@ is not allowed in the username");
    }

    if (userDAO.doesUsernameExist(newUser.getName())) {
      log.info("Username exists");
      throw new Exception("Username exists");
    }

    if (!isEmailDomainSupported(newUser.getEmail())) {
      log.info("Email not from TUM");
      throw new DomainNotSupportedException();
    }

    if (userDAO.doesEmailExists(newUser.getEmail())) {
      log.info("Email exists");
      throw new Exception("Email exists");
    }

    if (!isSecure(newUser.getPassword())) {
      log.info("Password weak");
      throw new Exception(
          "Password should be at least 8 characters and "
              + "contains one digit, one lowercase and one uppercase character.");
    }
  }

  private String createAccessToken(String subject) {
    SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
    long currMillis = System.currentTimeMillis();
    Date currDate = new Date(currMillis);

    Date expirationDate = new Date(currMillis + this.TTL_ACCESS_TOKEN.toMillis());

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

  @Override
  @Path("/register")
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response registerUser(NewUser newUser) {
   log.info("Register new user");

    try {
      verifyUserCredentials(newUser);
    } catch (Exception e) {
      return Response.status(400).entity(e.getMessage()).build();
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

      log.info("Successfully added new user");
      String token = createAccessToken(newUser.getEmail());
      return Response.ok().entity(token).build();

    } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
      e.printStackTrace();
      return Response.status(400).entity("Error occurred while hashing your password.").build();
    }
  }

  @Override
  @Path("/login")
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response loginUser(LoginUser loginUser) {
    String username = loginUser.getUsername();
    String password = loginUser.getPassword();

    User user;
    if (isEmail(username)) {
      user = userDAO.getUserByEmail(username);
    } else {
      user = userDAO.getUserByUsername(username);
    }

    if (user == null) {
      return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    try {
      byte[] hash = Hashing.generateHash(password, user.getSalt());
      String hashedPassword = Base64.getEncoder().encodeToString(hash);

      if (hashedPassword.equals(user.getHash())) {
        String token = createAccessToken(user.getEmail());
        return Response.ok(token).build();
      }
      return Response.status(Response.Status.UNAUTHORIZED).build();

    } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
      e.printStackTrace();
      return Response.status(400).entity("Error occurred on the server").build();
    }
  }

  @Override
  @Path("/access_token")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response generateRefreshToken(@Auth User user) {
    String token = createAccessToken(user.getEmail());
    return Response.ok(token).build();
  }
}
