package resources;

import accessors.CommentDAO;
import accessors.CommentVoteDAO;
import accessors.MemeDAO;
import accessors.MemeVoteDAO;
import api.VoteService;
import core.*;
import io.dropwizard.auth.Auth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class VoteResource implements VoteService {

  private static final Logger log = LoggerFactory.getLogger(VoteResource.class);
  private final MemeDAO memeDAO;
  private final MemeVoteDAO memeVoteDAO;
  private final CommentDAO commentDAO;
  private final CommentVoteDAO commentVoteDAO;

  public VoteResource(
      MemeDAO memeDAO,
      MemeVoteDAO memeVoteDAO,
      CommentDAO commentDAO,
      CommentVoteDAO commentVoteDAO) {
    this.memeDAO = memeDAO;
    this.memeVoteDAO = memeVoteDAO;
    this.commentDAO = commentDAO;
    this.commentVoteDAO = commentVoteDAO;
  }

  @Override
  @POST
  @Path("/meme")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response voteMeme(@Auth User user, NewMemeVote newMemeVote) {
    if (newMemeVote == null) {
      return Response.status(400).entity("Vote object is null").build();
    }

    if (!memeDAO.memeIDExists(newMemeVote.getMemeID().toString())) {
      return Response.status(400).entity("Meme does not exist").build();
    }

    log.info("Vote meme");

    String username = user.getName();
    MemeVote existingVote = memeVoteDAO.getVote(newMemeVote.getMemeID().toString(), username);

    if (existingVote == null) {
      memeVoteDAO.insertVote(newMemeVote.getMemeID().toString(), username, newMemeVote.getVote());
      return Response.ok("Meme voted").build();
    }

    // change current vote in database
    if (existingVote.getVote() != newMemeVote.getVote()) {
      memeVoteDAO.updateVote(newMemeVote.getMemeID().toString(), username, newMemeVote.getVote());
    }

    return Response.ok("Meme voted").build();
  }

  @Override
  @POST
  @Path("/comment")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response voteComment(@Auth User user, NewCommentVote newCommentVote) {
    if (newCommentVote == null) {
      return Response.status(400).entity("Vote object is null").build();
    }

    if (!commentDAO.commentIDExists(newCommentVote.getCommentID().toString())) {
      return Response.status(400).entity("Comment does not exist").build();
    }

    log.info("Vote comment");

    String username = user.getName();
    CommentVote existingVote =
        commentVoteDAO.getVote(newCommentVote.getCommentID().toString(), username);

    if (existingVote == null) {
      commentVoteDAO.insertVote(
          newCommentVote.getCommentID().toString(), username, newCommentVote.getVote());
      return Response.ok("Comment voted").build();
    }

    if (existingVote.getVote() != newCommentVote.getVote()) {
      commentVoteDAO.updateVote(
          newCommentVote.getCommentID().toString(), username, newCommentVote.getVote());
    }

    return Response.ok("Comment voted").build();
  }
}
