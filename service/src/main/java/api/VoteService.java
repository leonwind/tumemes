package api;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("/")
public interface VoteService {

    @Path("/upvote/{memeID}")
    @PATCH
    Response upvoteMeme(@PathParam("memeID") String memeID);

    @Path("/downvote/{memeID}")
    @PATCH
    Response downvoteMeme(@PathParam("memeID") String memeID);
}
