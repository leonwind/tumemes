package resources;

import accessors.UserDAO;
import api.AccountService;
import auth.EmailSender;
import auth.Hashing;
import auth.SecurePassword;
import auth.Token;
import core.PasswordReset;
import core.User;
import io.dropwizard.auth.AuthenticationException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.Duration;
import java.util.Base64;
import java.util.Optional;

public class AccountResource implements AccountService {

  private final UserDAO userDAO;
  private final String secretKey;
  private final String smtpUsername;
  private final String smtpPassword;

  public AccountResource(
      UserDAO userDAO, String secretKey, String smtpUsername, String smtpPassword) {
    this.userDAO = userDAO;
    this.secretKey = secretKey;
    this.smtpUsername = smtpUsername;
    this.smtpPassword = smtpPassword;
  }

  @Override
  @POST
  @Path("/request/verification")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response requestNewVerification(Email email) {
    User user = userDAO.getUserByEmail(email.getEmail());

    if (user == null) {
      return Response.status(400).entity("No user exists with this email").build();
    }

    if (user.isVerified()) {
      return Response.ok("User already verified").build();
    }

    try {
      EmailSender.sendVerificationEmail(email.getEmail(), smtpUsername, smtpPassword,
          Token.createToken(secretKey, email.getEmail(), Duration.ofDays(1L)));
      return Response.ok("Sent email").build();
    } catch (Exception ex) {
      return Response.status(400).entity("Could not send email").build();
    }
  }

  @Override
  @POST
  @Path("/verification/")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response validateEmail(TokenString token) {
    try {
      Optional<User> userOptional = Token.verifyToken(this.userDAO,
          this.secretKey, token.getToken());

      if (userOptional.isEmpty()) {
        return Response.status(400).entity("Token is not valid").build();
      }

      User user = userOptional.get();
      if (user.isVerified()) {
        return Response.ok("Email already verified").build();
      }

      userDAO.verifyUser(user.getEmail());
      return Response.ok("Verified the account with email " + user.getEmail()).build();

    } catch (AuthenticationException ex) {
      return Response.status(400).entity(ex.getMessage()).build();
    }
  }

  @Override
  @POST
  @Path("/request/password_reset/")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response requestPasswordReset(Email email) {
    User user = userDAO.getUserByEmail(email.getEmail());

    if (user == null) {
      return Response.status(400).entity("No user exists with this email").build();
    }

    try {
      String token = Token.createToken(secretKey, email.getEmail(), Duration.ofDays(1L));
      EmailSender.sendPasswordResetEmail(email.getEmail(), smtpUsername, smtpPassword, token);
    } catch (Exception ex) {
      ex.printStackTrace();
      return Response.status(400).entity("Unable to send email").build();
    }

    return Response.ok().build();
  }

  @Override
  @POST
  @Path("/password_reset")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response resetPassword(PasswordReset passwordReset) {
    try {
      Optional<User> userOptional = Token.verifyToken(userDAO, secretKey, passwordReset.getToken());

      if (userOptional.isEmpty()) {
        return Response.status(400).entity("Token is not valid").build();
      }

      User user = userOptional.get();

      if (!SecurePassword.isSecure(passwordReset.getNewPassword())) {
        return Response.status(400)
            .entity(
                "Password should be at least 8 characters and "
                    + "contains one digit, one lowercase and one uppercase character")
            .build();
      }

      byte[] newSalt = Hashing.generateSalt();
      try {
        byte[] newHash = Hashing.generateHash(passwordReset.getNewPassword(), newSalt);

        Base64.Encoder enc = Base64.getEncoder();
        userDAO.updateUserPassword(
            user.getEmail(), enc.encodeToString(newHash), enc.encodeToString(newSalt));

        return Response.ok("Updated password").build();

      } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
        return Response.status(400).entity("Error occurred while changing your password").build();
      }
    } catch (AuthenticationException ex) {
      return Response.status(400).entity(ex.getMessage()).build();
    }
  }
}
