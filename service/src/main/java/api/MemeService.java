package api;

import core.Meme;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/memes")
public interface MemeService {


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    List<Meme> getMemes();

    @GET
    @Path("/{memeID}")
    @Produces(MediaType.APPLICATION_JSON)
    Meme getMemeByID(@PathParam("memeID") String memeID);
}
