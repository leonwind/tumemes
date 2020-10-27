package accessors;

import accessors.mappers.CommentMapper;
import accessors.mappers.ReplyMapper;
import core.Comment;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.Date;
import java.util.List;

public interface CommentDAO {

  @SqlQuery("SELECT EXISTS (SELECT 1 FROM comments WHERE commentID = :currCommentID)")
  boolean commentIDExists(@Bind("currCommentID") String currCommentID);

  @SqlUpdate(
      "INSERT INTO comments (commentID, parentID, memeID, content, "
          + "author, created) VALUES (:commentID, :parentID, :memeID, :content, "
          + ":author, :created)")
  void insert(
      @Bind("commentID") String commentID,
      @Bind("parentID") String parentID,
      @Bind("memeID") String memeID,
      @Bind("content") String content,
      @Bind("author") String author,
      @Bind("created") Date created);

  @SqlQuery(
      "SELECT c.*, COALESCE(c1.numReplies, 0) as numReplies, "
          + "COALESCE(v.voteCount, 0) as voteCount, "
          + "COALESCE(v.userVote, 0) as userVote "
          + "FROM comments c "
          + "CROSS JOIN LATERAL ( "
          + "SELECT COUNT(*) numReplies "
          + "FROM comments c1 "
          + "WHERE c1.parentID = c.commentID "
          + ") c1 "
          + "LEFT JOIN ( "
          + "SELECT commentID, "
          + "SUM(vote) as voteCount, "
          + "SUM(vote) FILTER (WHERE username = :currUsername) as userVote "
          + "FROM commentvotes "
          + "GROUP BY commentID "
          + ") v on v.commentID = c.commentID "
          + "WHERE c.memeID = :currMemeID AND c.parentID IS NULL "
          + "ORDER BY voteCount DESC, created DESC")
  @RegisterRowMapper(CommentMapper.class)
  List<Comment> getCommentsFromMemeByVotes(
      @Bind("currMemeID") String currMemeID, @Bind("currUsername") String currUsername);

  @SqlQuery(
      "SELECT c.*, COALESCE(c1.numReplies, 0) as numReplies, "
          + "COALESCE(v.voteCount, 0) as voteCount, "
          + "COALESCE(v.userVote, 0) as userVote "
          + "FROM comments c "
          + "CROSS JOIN LATERAL ( "
          + "SELECT COUNT(*) numReplies "
          + "FROM comments c1 "
          + "WHERE c1.parentID = c.commentID "
          + ") c1 "
          + "LEFT JOIN ( "
          + "SELECT commentID, "
          + "SUM(vote) as voteCount, "
          + "SUM(vote) FILTER (WHERE username = :currUsername) as userVote "
          + "FROM commentvotes "
          + "GROUP BY commentID "
          + ") v on v.commentID = c.commentID "
          + "WHERE c.memeID = :currMemeID AND c.parentID IS NULL "
          + "ORDER BY created DESC")
  @RegisterRowMapper(CommentMapper.class)
  List<Comment> getCommentsFromMemeByDate(
      @Bind("currMemeID") String currMemeID,
      @Bind("currUsername") String currUsername);

  @SqlQuery(
      "SELECT c.*, "
          + "COALESCE(v.voteCount, 0) as voteCount, "
          + "COALESCE(v.userVote, 0) as userVote "
          + "FROM comments c "
          + "LEFT JOIN ( "
          + "SELECT commentID, "
          + "SUM(vote) as voteCount,"
          + "SUM(vote) FILTER (WHERE username = :currUsername) as userVote "
          + "FROM commentvotes "
          + "GROUP BY commentID "
          + ") v on v.commentID = c.commentID "
          + "WHERE parentID = :parentCommentID "
          + "ORDER BY created ASC")
  @RegisterRowMapper(ReplyMapper.class)
  List<Comment> getAllReplies(
      @Bind("parentCommentID") String parentCommentID,
      @Bind("currUsername") String username);
}
