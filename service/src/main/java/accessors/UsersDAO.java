package accessors;

import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public interface UsersDAO {
  @SqlUpdate("INSERT INTO users (username, email, password) VALUES " +
      "(:username, :email, :password)")
  void addNewUser(@Bind("username") String username,
                  @Bind("email") String email,
                  @Bind("password") String password);
}
