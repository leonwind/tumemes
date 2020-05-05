package accessors.mappers;

import core.Vote;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class VoteMapper implements RowMapper<Vote> {

    @Override
    public Vote map(ResultSet rs, StatementContext ctx) throws SQLException {
        return new Vote(
                UUID.fromString(rs.getString("memeID")),
                rs.getString("username"),
                rs.getInt("vote"));
    }
}
