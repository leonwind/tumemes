package resources;

import accessors.CommentDAO;
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

public class CommentResource implements CommentService {

  private final CommentDAO commentDAO;

  public CommentResource(CommentDAO commentDAO) {
    this.commentDAO = commentDAO;
  }

  @Override
  @POST
  @Path("/post")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response postComment(@Auth User user, NewComment newComment) {
    if (newComment == null) {
      return Response.status(400).entity("Comment object is null").build();
    }

    Comment comment = Comment.fromNewComment(newComment);

    return Response.ok("Posted comment").build();
  }
}
