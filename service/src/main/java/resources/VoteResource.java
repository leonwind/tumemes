package resources;

import accessors.MemeDAO;
import accessors.VoteDAO;
import api.VoteService;
import core.Vote;

import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

public class VoteResource implements VoteService {

  private final MemeDAO memeDAO;
  private final VoteDAO voteDAO;

  public VoteResource(MemeDAO memeDAO, VoteDAO voteDAO) {
    this.memeDAO = memeDAO;
    this.voteDAO = voteDAO;
  }

  @Path("/upvote/{memeID}")
  @POST
  public Response upvoteMeme(@PathParam("memeID") String memeID) {
    String username = "mock_username";

    return voteMeme(memeID, username, 1);
  }

  @Path("/downvote/{memeID}")
  @POST
  public Response downvoteMeme(@PathParam("memeID") String memeID) {
    String username = "mock_username";

    return voteMeme(memeID, username, -1);
  }

  /**
   * Add vote to memeVote database
   * @param vote is either 1 (upvote) or -1 (downvote)
   */
  private Response voteMeme(String memeID, String username, int vote) {
    if (!memeDAO.memeIDExists(memeID)) {
      return Response.status(400).entity("Meme does not exist").build();
    }

    Vote existingVote = voteDAO.getVote(memeID, username);

    if (existingVote == null) {
      voteDAO.insertVote(memeID, username, vote);
      return Response.ok("Meme voted").build();
    }

    /*
     * Toggle the current vote from down to upvote or the other way around
     * if the new given vote is different from the existing one
     */
    if (existingVote.isUpvoted() != (1 == vote)) {
      voteDAO.updateVote(memeID, username, vote);
    }

    return Response.ok("Meme voted").build();
  }
}
