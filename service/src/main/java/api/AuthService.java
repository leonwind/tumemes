package api;

import core.LoginUser;
import core.NewUser;
import core.User;
import io.dropwizard.auth.Auth;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


public interface AuthService {

  @Path("/register")
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  Response registerUser(NewUser newuser);

  /*
  @Path("/login")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  Response loginUser();*/

  @Path("/refresh_token")
  @GET
  @Produces(MediaType.TEXT_PLAIN)
  Response generateRefreshToken(@Auth User user);
}
