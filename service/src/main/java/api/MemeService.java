package api;

import core.Meme;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/memes")
public interface MemeService {


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    List<Meme> getMemes(@QueryParam("sortBy") String sortBy);

    @GET
    @Path("/{memeID}")
    @Produces(MediaType.APPLICATION_JSON)
    Meme getMemeByID(@PathParam("memeID") String memeID);
}
