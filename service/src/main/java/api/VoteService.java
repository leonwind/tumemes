package api;

import core.NewVote;
import core.Vote;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/")
public interface VoteService {

    @POST
    @Path("/vote")
    @Consumes(MediaType.APPLICATION_JSON)
    Response voteMeme(NewVote newVote);
}
