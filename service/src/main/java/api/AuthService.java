package api;

import core.NewUser;
import core.User;
import io.dropwizard.auth.Auth;
import io.dropwizard.auth.basic.BasicCredentials;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


public interface AuthService {

  @Path("/register")
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  Response registerUser(NewUser newuser);

  @Path("/login")
  @GET
  @Produces(MediaType.TEXT_PLAIN)
  Response loginUser(BasicCredentials credentials);

  @Path("/refresh_token")
  @GET
  @Produces(MediaType.TEXT_PLAIN)
  Response generateRefreshToken(@Auth User user);
}
