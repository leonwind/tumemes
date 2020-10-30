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


  /**
   * 2147483647 = Integer.MAX_VALUE
   */
  @Override
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public List<Meme> getMemes(
      @Auth User user,
      @QueryParam("limit") @DefaultValue("9223372036854775807") long limit,
      @QueryParam("sortBy") String sortBy) {
    System.out.println(limit);

    String username = user.getName();

    if (sortBy == null) {
      return memeDAO.getAllMemesByVotes(username, limit);
    }

    if (sortBy.equals("created")) {
      List<Meme> ans = memeDAO.getAllMemesByDate(username, limit);
      System.out.println(ans);
      return ans;
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
