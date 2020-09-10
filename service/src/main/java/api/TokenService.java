package api;

import core.NewUser;

import javax.annotation.security.PermitAll;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public interface TokenService {

  @POST
  @Path("/register")
  @PermitAll
  @Consumes(MediaType.APPLICATION_JSON)
  Response registerUser(NewUser newuser);

  @POST
  @PermitAll
  @Consumes(MediaType.APPLICATION_JSON)
  Response createToken(NewUser newUser);
}
