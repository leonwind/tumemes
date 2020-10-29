package api;

import com.fasterxml.jackson.annotation.JsonProperty;
import core.PasswordReset;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/account")
public interface AccountService {

  @POST
  @Path("/request/verification")
  @Consumes(MediaType.APPLICATION_JSON)
  Response requestNewVerification(Email email);

  @GET
  @Path("/verification/{token}")
  @Produces(MediaType.APPLICATION_JSON)
  Response validateEmail(@PathParam("token") String token);

  @POST
  @Path("/request/password_reset/")
  @Consumes(MediaType.APPLICATION_JSON)
  Response requestPasswordReset(Email email);

  @POST
  @Path("/password_reset")
  @Consumes(MediaType.APPLICATION_JSON)
  Response resetPassword(PasswordReset passwordReset);

  class Email {
    private final String email;

    public Email(@JsonProperty("email") String email) {
      this.email = email;
    }

    public String getEmail() {
      return email;
    }
  }
}
