package resources;

import accessors.UserDAO;
import api.AuthService;
import auth.Hashing;
import core.LoginUser;
import core.NewUser;
import core.User;
import enums.AllowedEmailDomains;
import io.dropwizard.auth.Auth;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

public class AuthResource implements AuthService {

  private final UserDAO userDAO;
  private final String secretKey;

  // one week until refresh token gets expired
  // time in milli seconds
  final long TTL_REFRESH_TOKEN = 604800000;
  // one hour until normal token gets expired
  // time in milli seconds
  final long TTL_ACCESS_TOKEN = 3600000;

  public AuthResource(UserDAO userDAO, String secretKey) {
    this.userDAO = userDAO;
    this.secretKey = secretKey;
  }

  private static boolean isEmailDomainSupported(String email) {
    String domain = email.substring(email.indexOf('@') + 1);
    return Arrays.stream(AllowedEmailDomains.values())
        .anyMatch(allowedDomain -> allowedDomain.toString().equals(domain));
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

  private static boolean isEmail(String email) {
    return email.indexOf('@') != -1;
  }

  private String createAccessToken(String subject) {
    SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
    long currMillis = System.currentTimeMillis();
    Date currDate = new Date(currMillis);

    Date expirationDate = new Date(currMillis + this.TTL_ACCESS_TOKEN);

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

  private String createRefreshToken() {
    return UUID.randomUUID().toString().replace("-", "");
  }

  @Override
  @Path("/register")
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  public Response registerUser(NewUser newUser) {
    System.out.println("REGISTER NEW USER");
    System.out.println(newUser);

    if (userDAO.doesUsernameExist(newUser.getName())) {
      System.out.println("Username exits");
      return Response.status(400).entity("Username does already exists").build();
    }

    if (!isEmailDomainSupported(newUser.getEmail())) {
      System.out.println("Email not TUM");
      return Response.status(400).entity("Email is not from TUM.").build();
    }

    if (userDAO.doesEmailExists(newUser.getEmail())) {
      System.out.println("Email exists");
      return Response.status(400).entity("Email does already exists").build();
    }

    if (!isSecure(newUser.getPassword())) {
      System.out.println("Password weak");
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

      return Response.ok().build();

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
  @Path("/refresh_token")
  @GET
  @Produces(MediaType.TEXT_PLAIN)
  public Response generateRefreshToken(@Auth User user) {
    String refreshToken = createRefreshToken();
    return Response.ok(refreshToken).build();
  }
}
