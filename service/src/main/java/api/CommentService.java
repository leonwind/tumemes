package api;

import core.NewComment;
import core.User;
import io.dropwizard.auth.Auth;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/comments")
public interface CommentService {

  @POST
  @Path("/post")
  @Consumes(MediaType.APPLICATION_JSON)
  Response postComment(@Auth User user, NewComment newComment);
}
