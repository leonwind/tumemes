package api;

import core.Meme;
import core.User;
import io.dropwizard.auth.Auth;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/memes")
public interface MemeService {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    List<Meme> getMemes(@Auth User user, @QueryParam("sortBy") String sortBy);

    @GET
    @Path("/{memeID}")
    @Produces(MediaType.APPLICATION_JSON)
    Meme getMemeByID(@Auth User user, @PathParam("memeID") String memeID);
}
