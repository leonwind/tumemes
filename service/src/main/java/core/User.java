package core;

import javax.security.auth.Subject;
import java.security.Principal;

public class User implements Principal {
  @Override
  public String getName() {
    return null;
  }

  @Override
  public boolean implies(Subject subject) {
    return false;
  }
}
