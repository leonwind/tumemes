package resources;

import accessors.MemeDAO;
import api.MemeService;
import core.Meme;
import core.User;
import io.dropwizard.auth.Auth;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

public class MemeResource implements MemeService {

  private final MemeDAO memeDAO;

  public MemeResource(MemeDAO memeDAO) {
    this.memeDAO = memeDAO;
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public List<Meme> getMemes(@Auth User user,
                             @QueryParam("sortBy") String sortBy) {
    if (sortBy == null) {
      return memeDAO.getAllMemesByVotes();
    }

    if (sortBy.equals("created")) {
      return memeDAO.getAllMemesByDate();
    }
    return memeDAO.getAllMemesByVotes();
  }

  @GET
  @Path("/{memeID}")
  @Produces(MediaType.APPLICATION_JSON)
  public Meme getMemeByID(@Auth User user, @PathParam("memeID") String memeID) {
    return memeDAO.getMemeByID(memeID);
  }

}
