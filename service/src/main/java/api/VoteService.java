package api;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("/")
public interface VoteService {

    @PATCH
    @Path("/upvote/{memeID}")
    Response upvoteMeme(@PathParam("memeID") String memeID);

    @PATCH
    @Path("/downvote/{memeID}")
    Response downvoteMeme(@PathParam("memeID") String memeID);
}
