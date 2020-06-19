package api;

import core.NewVote;
import core.User;
import core.Vote;
import io.dropwizard.auth.Auth;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/")
public interface VoteService {

    @POST
    @Path("/vote")
    @Consumes(MediaType.APPLICATION_JSON)
    Response voteMeme(@Auth User user, NewVote newVote);
}
