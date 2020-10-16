package api;

import core.Comment;
import core.NewComment;
import core.User;
import io.dropwizard.auth.Auth;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/comments")
public interface CommentService {

  @POST
  @Path("/post")
  @Consumes(MediaType.APPLICATION_JSON)
  Response postComment(NewComment newComment);

  @GET
  @Path("/{memeID}")
  @Produces(MediaType.APPLICATION_JSON)
  List<Comment> getCommentsFromMeme(@PathParam("memeID") String memeID);

  @GET
  @Path("/replies/{commentID}")
  @Produces(MediaType.APPLICATION_JSON)
  List<Comment> getReplies(@PathParam("commentID") String commentID);
}
