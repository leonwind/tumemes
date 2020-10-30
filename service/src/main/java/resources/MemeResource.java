package resources;

import accessors.MemeDAO;
import api.MemeService;
import core.Meme;
import core.User;
import io.dropwizard.auth.Auth;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.Timestamp;
import java.util.List;

public class MemeResource implements MemeService {

  private final MemeDAO memeDAO;

  public MemeResource(MemeDAO memeDAO) {
    this.memeDAO = memeDAO;
  }


  /**
   * 9007199254740991 = Number.MAX_SAFE_INTEGER
   * Javascript safe upper limit
   * = 2^53 - 1
   */
  @Override
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public List<Meme> getMemes(
      @Auth User user,
      @QueryParam("limit") @DefaultValue("9007199254740991") long limit,
      @QueryParam("sortBy") String sortBy) {
    String username = user.getName();

    if (sortBy == null) {
      return memeDAO.getAllMemesByVotes(username, limit);
    }

    if (sortBy.equals("created")) {
      return memeDAO.getAllMemesByDate(username, new Timestamp(limit));
    }

    return memeDAO.getAllMemesByVotes(username, limit);
  }

  @Override
  @GET
  @Path("/{memeID}")
  @Produces(MediaType.APPLICATION_JSON)
  public Meme getMemeByID(@Auth User user, @PathParam("memeID") String memeID) {
    if (memeDAO.memeIDExists(memeID)) {
      return memeDAO.getMemeByID(memeID, user.getName());
    }

    return null;
  }
}
