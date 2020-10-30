package api;

import core.Meme;
import core.User;
import io.dropwizard.auth.Auth;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/memes")
public interface MemeService {

  /**
   * 9007199254740991 = 2^53 - 1 = Number.MAX_SAFE_INTEGER
   * Javascript safe upper limit
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  List<Meme> getMemes(
      @Auth User user,
      @QueryParam("limit") @DefaultValue("9223372036854775807") long limit,
      @QueryParam("sortBy") String sortBy);

  @GET
  @Path("/user/{username}")
  @Produces(MediaType.APPLICATION_JSON)
  List<Meme> getMemesFromUser(
      @Auth User user,
      @PathParam("username") String username,
      @QueryParam("limit") @DefaultValue("9223372036854775807") long limit,
      @QueryParam("sortBy") String sortBy);

  @GET
  @Path("/meme/{memeID}")
  @Produces(MediaType.APPLICATION_JSON)
  Meme getMemeByID(@Auth User user, @PathParam("memeID") String memeID);
}
