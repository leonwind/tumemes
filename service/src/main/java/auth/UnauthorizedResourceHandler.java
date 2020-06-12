package auth;

import io.dropwizard.auth.UnauthorizedHandler;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

public class UnauthorizedResourceHandler implements UnauthorizedHandler {

  @Context
  private HttpServletRequest request;

  @Override
  public Response buildResponse(String prefix, String realm) {
    Response.Status unauthorized = Response.Status.UNAUTHORIZED;
    return Response.status(unauthorized).build();
  }

}
