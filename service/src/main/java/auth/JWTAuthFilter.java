package auth;

import io.dropwizard.auth.AuthFilter;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.HttpHeaders;
import java.io.IOException;
import java.security.Principal;

public class JWTAuthFilter<P extends Principal> extends AuthFilter<JWTCredentials, P> {

  public static class Builder<P extends Principal>
      extends AuthFilterBuilder<JWTCredentials, P, JWTAuthFilter<P>> {

    @Override
    protected JWTAuthFilter<P> newInstance() {
        return new JWTAuthFilter<>();
    }
  }

  private static JWTCredentials getCredentials(String authLine) {
    String prefix = "Bearer ";
    if (authLine != null && authLine.startsWith(prefix)) {
      JWTCredentials result = new JWTCredentials();
      result.setJwtToken(authLine.substring(prefix.length() + 1));
      return result;
    }
    return null;
  }

  @Override
  public void filter(ContainerRequestContext requestContext) throws IOException {
    final JWTCredentials credentials =
        getCredentials(requestContext.getHeaders().getFirst(HttpHeaders.AUTHORIZATION));
    if (!authenticate(requestContext, credentials, "JWT")) {
      throw new WebApplicationException(
          this.unauthorizedHandler.buildResponse(this.prefix, this.realm));
    }
  }
}
