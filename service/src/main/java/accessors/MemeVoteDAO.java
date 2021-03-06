package accessors;

import accessors.mappers.MemeVoteMapper;
import core.MemeVote;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public interface MemeVoteDAO {

  @SqlUpdate(
      "INSERT INTO memevotes (memeID, username, vote) "
          + "VALUES (:memeID, :username, :vote)")
  void insertVote(
      @Bind("memeID") String memeID,
      @Bind("username") String username,
      @Bind("vote") int vote);

  @SqlQuery(
      "SELECT * FROM memevotes WHERE "
          + "memeID = :currMemeID AND "
          + "username = :currUsername")
  @RegisterRowMapper(MemeVoteMapper.class)
  MemeVote getVote(
      @Bind("currMemeID") String currMemeID, @Bind("currUsername") String currUsername);

  @SqlUpdate(
      "UPDATE memevotes SET vote = :currVote WHERE memeID = "
          + ":currMemeID AND username = :currUsername")
  void updateVote(
      @Bind("currMemeID") String currMemeID,
      @Bind("currUsername") String currUsername,
      @Bind("currVote") int currVote);
}
