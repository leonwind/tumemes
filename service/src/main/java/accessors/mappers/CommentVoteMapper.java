package accessors.mappers;

import core.CommentVote;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class CommentVoteMapper implements RowMapper<CommentVote> {

  @Override
  public CommentVote map(ResultSet rs, StatementContext ctx) throws SQLException {
    return new CommentVote(
        UUID.fromString(rs.getString("commentID")),
        rs.getInt("vote"),
        rs.getString("username"));
  }
}
