package api;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("/")
public interface VoteService {

    @POST
    @Path("/upvote/{memeID}")
    Response upvoteMeme(@PathParam("memeID") String memeID);

    @POST
    @Path("/downvote/{memeID}")
    Response downvoteMeme(@PathParam("memeID") String memeID);
}
