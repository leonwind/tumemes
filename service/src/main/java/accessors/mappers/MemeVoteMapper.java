package accessors.mappers;

import core.MemeVote;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class MemeVoteMapper implements RowMapper<MemeVote> {

    @Override
    public MemeVote map(ResultSet rs, StatementContext ctx) throws SQLException {
        return new MemeVote(
                UUID.fromString(rs.getString("memeID")),
                rs.getInt("vote"),
                rs.getString("username"));
    }
}
