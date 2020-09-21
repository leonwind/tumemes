package api;

import core.NewUser;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/register")
public interface AuthSerivce {

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  Response registerUser(NewUser newuser);

  @GET
  @Produces(MediaType.TEXT_PLAIN)
  String generateToken();
}
