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
   * 2147483647 = Integer.MAX_VALUE
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  List<Meme> getMemes(
      @Auth User user,
      @QueryParam("limit") @DefaultValue("9223372036854775807") long limit,
      @QueryParam("sortBy") String sortBy);

  @GET
  @Path("/{memeID}")
  @Produces(MediaType.APPLICATION_JSON)
  Meme getMemeByID(@Auth User user, @PathParam("memeID") String memeID);
}
