package resources;

import accessors.MemeDAO;
import accessors.VoteDAO;
import api.VoteService;
import core.NewVote;
import core.Vote;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class VoteResource implements VoteService {

  private final MemeDAO memeDAO;
  private final VoteDAO voteDAO;

  public VoteResource(MemeDAO memeDAO, VoteDAO voteDAO) {
    this.memeDAO = memeDAO;
    this.voteDAO = voteDAO;
  }

  @Override
  @POST
  @Path("/vote/")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response voteMeme(NewVote newVote) {
    if (newVote == null) {
      return Response.status(400).entity("Vote object is null").build();
    }

    String username = "mock user";

    if (!memeDAO.memeIDExists(newVote.getMemeID().toString())) {
      return Response.status(400).entity("Meme does not exist").build();
    }

    Vote existingVote = voteDAO.getVote(newVote.getMemeID().toString(), username);

    if (existingVote == null) {
      voteDAO.insertVote(newVote.getMemeID().toString(), username, newVote.getVote());
      return Response.ok("Meme voted").build();
    }

    // change current vote in database
    if (existingVote.getVote() != newVote.getVote()) {
      voteDAO.updateVote(newVote.getMemeID().toString(), username, newVote.getVote());
    }

    return Response.ok("Meme voted").build();
  }
}
