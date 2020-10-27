package resources;

import accessors.UserDAO;
import api.EmailVerificationService;
import auth.JWTAuthenticator;
import core.User;
import io.dropwizard.auth.AuthenticationException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;

public class EmailVerificationResource implements EmailVerificationService {

  private final UserDAO userDAO;
  private final String secretKey;

  public EmailVerificationResource(UserDAO userDAO, String secretKey) {
    this.userDAO = userDAO;
    this.secretKey = secretKey;
  }

  @GET
  @Path("/{token}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response validateEmail(@PathParam("token") String token) {
    try {
      Optional<User> userOptional =
          JWTAuthenticator.verifyToken(this.userDAO, this.secretKey, token);

      if (userOptional.isEmpty()) {
        return Response.status(400).entity("User does not exist").build();
      }

      User user = userOptional.get();
      if (user.isVerified()) {
        return Response.ok("Email already verified").build();
      }

      userDAO.verifyUser(user.getEmail());
      return Response.ok("Verified the email " + user.getEmail()).build();

    } catch (AuthenticationException ex) {
      return Response.status(400).entity(ex.getMessage()).build();
    }
  }
}
