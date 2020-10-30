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

  @POST
  @Path("/verification/")
  @Consumes(MediaType.APPLICATION_JSON)
  Response validateEmail(TokenString token);

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

  class TokenString {
    private final String token;

    public TokenString(@JsonProperty("token") String token) {
      this.token = token;
    }

    public String getToken() {
      return token;
    }
  }
}
