package accessors.mappers;

import core.Meme;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class MemeMapper implements RowMapper<Meme> {

  @Override
  public Meme map(ResultSet rs, StatementContext ctx) throws SQLException {
    return new Meme(
        UUID.fromString(rs.getString("memeID")),
        rs.getString("title"),
        rs.getString("author"),
        rs.getInt("voteCount"),
        rs.getTimestamp("created"),
        rs.getInt("userVote"));
  }
}
