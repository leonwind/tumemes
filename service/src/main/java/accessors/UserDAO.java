package accessors;

import accessors.mappers.UserMapper;
import core.User;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public interface UserDAO {
  @SqlUpdate("INSERT INTO users (username, email, hash, salt) VALUES " +
      "(:username, :email, :hash, :salt)")
  void addNewUser(@Bind("username") String username,
                  @Bind("email") String email,
                  @Bind("hash") String hash,
                  @Bind("salt") String salt);

  @SqlQuery("SELECT EXISTS (SELECT 1 FROM users WHERE username = :username)")
  boolean doesUsernameExist(@Bind("username") String username);

  @SqlQuery("SELECT EXISTS (SELECT 1 FROM users where email = :email)")
  boolean doesEmailExists(@Bind("email") String email);

  @SqlQuery("SELECT * FROM users WHERE username = :username")
  @RegisterRowMapper(UserMapper.class)
  User getUserByUsername(@Bind("username") String username);

  @SqlQuery("SELECT * FROM users WHERE email = :email")
  @RegisterRowMapper(UserMapper.class)
  User getUserByEmail(@Bind("email") String email);
}
