package accessors.mappers;

import core.Comment;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class CommentMapper implements RowMapper<Comment> {

  @Override
  public Comment map(ResultSet rs, StatementContext ctx) throws SQLException {
    return new Comment(
        UUID.fromString(rs.getString("commentID")),
        null,
        UUID.fromString(rs.getString("memeID")),
        rs.getString("content"),
        rs.getString("author"),
        rs.getTimestamp("created"),
        rs.getInt("numReplies"),
        rs.getInt("voteCount"),
        rs.getInt("userVote"));
  }
}
