package resources;

import accessors.UserDAO;
import api.AuthService;
import auth.EmailSender;
import auth.Hashing;
import auth.SecurePassword;
import auth.Token;
import core.LoginUser;
import core.NewUser;
import core.User;
import enums.AllowedEmailDomains;
import exceptions.DomainNotSupportedException;
import io.dropwizard.auth.Auth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.Duration;
import java.util.Arrays;
import java.util.Base64;

public class AuthResource implements AuthService {

  private static final Logger log = LoggerFactory.getLogger(AuthResource.class);
  private final UserDAO userDAO;
  private final String secretKey;
  private final String smtpUsername;
  private final String smtpPassword;

  private final Duration TTL_ACCESS_TOKEN = Duration.ofHours(5L);
  private final Duration TTL_EMAIL_VERIFICATION = Duration.ofDays(1L);

  public AuthResource(UserDAO userDAO, String secretKey, String smtpUsername, String smtpPassword) {
    this.userDAO = userDAO;
    this.secretKey = secretKey;
    this.smtpUsername = smtpUsername;
    this.smtpPassword = smtpPassword;
  }

  private static boolean isEmailDomainSupported(String email) {
    String domain = email.substring(email.indexOf('@') + 1);
    return Arrays.stream(AllowedEmailDomains.values())
        .anyMatch(allowedDomain -> allowedDomain.toString().equals(domain));
  }

  private static boolean isEmail(String email) {
    return email.indexOf('@') != -1;
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

    if (!SecurePassword.isSecure(newUser.getPassword())) {
      log.info("Password weak");
      throw new Exception(
          "Password should be at least 8 characters and "
              + "contains one digit, one lowercase and one uppercase character.");
    }
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
      try {
        EmailSender.sendVerificationEmail(
            newUser.getEmail(),
            smtpUsername,
            smtpPassword,
            Token.createToken(secretKey, newUser.getEmail(), TTL_EMAIL_VERIFICATION));
      } catch (Exception e) {
        e.printStackTrace();
        return Response.status(400).entity("Unable to send email").build();
      }
      return Response.ok().entity("Registered user successfully").build();

    } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
      e.printStackTrace();
      return Response.status(400).entity("Error occurred while hashing password.").build();
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
        if (!user.isVerified()) {
          return Response.status(Response.Status.UNAUTHORIZED)
              .entity("Account not validated")
              .build();
        }

        String token = Token.createToken(secretKey, user.getEmail(), TTL_ACCESS_TOKEN);
        return Response.ok(token).build();
      }

      // password or username wrong
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
    String token = Token.createToken(secretKey, user.getEmail(), TTL_ACCESS_TOKEN);
    return Response.ok(token).build();
  }
}
