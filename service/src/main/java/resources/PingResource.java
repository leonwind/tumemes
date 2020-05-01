package resources;

import api.PingService;

import javax.ws.rs.core.Response;

public class PingResource implements PingService {

  @Override
  public Response ping() {
    return Response.ok("ok").build();
  }
}
