package accessors.mappers;

import core.User;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper implements RowMapper<User> {

  @Override
  public User map(ResultSet rs, StatementContext ctx) throws SQLException {
    return new User(
        rs.getString("username"),
        rs.getString("email"),
        rs.getString("hash"),
        rs.getString("salt"));
  }
}
