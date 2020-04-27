package resources;

import api.PingService;
import jakarta.ws.rs.core.Response;

public class PingResource implements PingService {

    @Override
    public Response ping() {
        return Response.ok("ok").build();
    }
}
