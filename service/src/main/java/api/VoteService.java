package api;

import core.NewCommentVote;
import core.NewMemeVote;
import core.User;
import io.dropwizard.auth.Auth;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/vote")
public interface VoteService {

    @POST
    @Path("/meme")
    @Consumes(MediaType.APPLICATION_JSON)
    Response voteMeme(@Auth User user, NewMemeVote newMemeVote);

    @POST
    @Path("/comment")
    @Consumes(MediaType.APPLICATION_JSON)
    Response voteComment(@Auth User user, NewCommentVote newCommentVote);
}
