package accessors;

import accessors.mappers.CommentVoteMapper;
import accessors.mappers.MemeVoteMapper;
import core.CommentVote;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public interface CommentVoteDAO {

  @SqlUpdate(
      "INSERT INTO commentvotes (commentID, username, vote) "
          + "VALUES (:commentID, :username, :vote)")
  void insertVote(
      @Bind("commentID") String commentID,
      @Bind("username") String username,
      @Bind("vote") int vote);

  @SqlQuery(
      "SELECT * FROM commentvotes WHERE "
          + "commentID = :currCommentID AND "
          + "username = :currUsername")
  @RegisterRowMapper(CommentVoteMapper.class)
  CommentVote getVote(
      @Bind("currCommentID") String currCommentID,
      @Bind("currUsername") String currUsername);

  @SqlUpdate(
      "UPDATE commentvotes SET vote = :currVote WHERE commentID = "
          + ":currCommentID AND username = :currUsername")
  void updateVote(
      @Bind("currCommentID") String currCommentID,
      @Bind("currUsername") String currUsername,
      @Bind("currVote") int currVote);
}
