package auth;

import core.User;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;

import java.util.Optional;

public class HTTPBasicAuth implements Authenticator<BasicCredentials, User> {

  @Override
  public Optional<User> authenticate(BasicCredentials credentials) throws AuthenticationException {
    return Optional.empty();
  }
}

