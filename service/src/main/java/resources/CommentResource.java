package resources;

import accessors.CommentDAO;
import accessors.MemeDAO;
import api.CommentService;
import core.Comment;
import core.NewComment;
import core.User;
import io.dropwizard.auth.Auth;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

public class CommentResource implements CommentService {

  private final CommentDAO commentDAO;
  private final MemeDAO memeDAO;

  public CommentResource(CommentDAO commentDAO, MemeDAO memeDAO) {
    this.commentDAO = commentDAO;
    this.memeDAO = memeDAO;
  }

  @Override
  @POST
  @Path("/post")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response postComment(@Auth User user, NewComment newComment) {
    if (newComment == null) {
      return Response.status(400).entity("Comment object is null").build();
    }

    if (!memeDAO.memeIDExists(newComment.getMemeID().toString())) {
      return Response.status(400).entity("Meme does not exist").build();
    }

    String username = user.getName();
    Comment comment = Comment.fromNewComment(newComment, username);

    commentDAO.insert(
        comment.getCommentID().toString(),
        comment.getParentID().toString(),
        comment.getMemeID().toString(),
        comment.getContent(),
        comment.getAuthor(),
        comment.getCreated());

    return Response.ok("Posted comment").build();
  }

  @Override
  public List<Comment> getCommentsFromMeme(@Auth User user, String memeID) {
    return commentDAO.getCommentsFromMeme(memeID);
  }

  @Override
  public List<Comment> getReplies(@Auth User user, String commentID) {
    return commentDAO.getAllReplies(commentID);
  }
}
