package resources;

import accessors.MemeDAO;
import api.MemeService;
import core.Meme;
import core.User;
import io.dropwizard.auth.Auth;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class MemeResource implements MemeService {

  private final MemeDAO memeDAO;

  public MemeResource(MemeDAO memeDAO) {
    this.memeDAO = memeDAO;
  }

  /**
   * 9007199254740991 = 2^53 - 1 = Number.MAX_SAFE_INTEGER
   * Javascript safe upper limit
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
      return memeDAO.getAllMemesByVotes(username, limit, false);
    }

    if (sortBy.equals("created")) {
      return memeDAO.getAllMemesByDate(username, new Timestamp(limit), false);
    }

    return memeDAO.getAllMemesByVotes(username, limit, false);
  }

  @Override
  @GET
  @Path("/user/{username}")
  @Produces(MediaType.APPLICATION_JSON)
  public List<Meme> getMemesFromUser(
      @Auth User user,
      @PathParam("username") String username,
      @QueryParam("limit") @DefaultValue("9007199254740991") long limit,
      @QueryParam("sortBy") String sortBy) {
    if (!username.equals(user.getName())) {
      return new ArrayList<>();
    }

    if (sortBy == null) {
      return memeDAO.getAllMemesByVotes(username, limit, true);
    }

    if (sortBy.equals("created")) {
      return memeDAO.getAllMemesByDate(username, new Timestamp(limit), true);
    }

    return memeDAO.getAllMemesByVotes(username, limit, true);
  }

  @Override
  @GET
  @Path("/meme/{memeID}")
  @Produces(MediaType.APPLICATION_JSON)
  public Meme getMemeByID(@Auth User user, @PathParam("memeID") String memeID) {
    if (memeDAO.memeIDExists(memeID)) {
      return memeDAO.getMemeByID(memeID, user.getName());
    }

    return null;
  }
}
