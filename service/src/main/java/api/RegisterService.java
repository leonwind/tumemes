package api;

import core.User;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/register")
public interface RegisterService {

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  Response registerUser(User newUser);
}