package api;


@Path("/ping")
@Produces(MediaType.APPLICATION_JSON)
public interface PingService {

    @GET
    Response ping();

}
