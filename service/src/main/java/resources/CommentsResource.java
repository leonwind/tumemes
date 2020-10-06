package resources;

import api.CommentsService;
import core.NewComment;
import core.User;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class CommentsResource implements CommentsService {

  @Override
  @POST
  @Path("/post")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response postComment(User user, NewComment newComment) {
    if (newComment == null) {
      return Response.status(400).entity("Comment object is null").build();
    }

    return Response.ok("Posted comment").build();
  }
}
