package resources;

import accessors.MemeDAO;
import accessors.VoteDAO;
import api.VoteService;
import core.Vote;

import javax.validation.constraints.Null;
import javax.ws.rs.PATCH;
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
  @PATCH
  public Response upvoteMeme(@PathParam("memeID") String memeID) {
    String username = "mock_username";

    return voteMeme(memeID, username, 1);
  }

  @Path("/downvote/{memeID}")
  @PATCH
  public Response downvoteMeme(@PathParam("memeID") String memeID) {
    String username = "mock_username";

    return voteMeme(memeID, username, -1);
  }

  private Response voteMeme(String memeID, String username, int vote) {
    if (!memeDAO.memeIDExists(memeID)) {
      return Response.status(400).build();
    }

    Vote existingVote = voteDAO.getVote(memeID, username);

    if (existingVote == null) {
      voteDAO.insertVote(memeID, username, vote);
      return Response.ok().build();
    }

    // update vote from upvoted to downvoted
    if (existingVote.isUpvoted()) {
      voteDAO.updateVote(memeID, username, vote);
    }

    return Response.ok("").build();
  }
}
