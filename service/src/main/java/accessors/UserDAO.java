package accessors;

import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public interface UserDAO {
  @SqlUpdate("INSERT INTO users (username, email, password) VALUES " +
      "(:username, :email, :password)")
  void addNewUser(@Bind("username") String username,
                  @Bind("email") String email,
                  @Bind("password") String password);

  @SqlQuery("SELECT EXISTS (SELECT 1 FROM users WHERE username = :username)")
  boolean doesUsernameExist(@Bind("username") String username);

  @SqlQuery("SELECT EXISTS (SELECT 1 FROM users where email = :email)")
  boolean doesEmailExists(@Bind("email") String email);

}
