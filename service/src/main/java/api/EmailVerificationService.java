package api;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/verification")
public interface EmailVerificationService {

  @GET
  @Path("/{token}")
  @Produces(MediaType.APPLICATION_JSON)
  Response validateEmail(@PathParam("token") String token);
}
