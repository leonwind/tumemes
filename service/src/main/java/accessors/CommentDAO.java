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
      "SELECT c.*, COALESCE(c1.numReplies, 0) as numReplies "
          + "FROM comments c "
          + "CROSS JOIN LATERAL ( "
          + "SELECT COUNT(*) numReplies "
          + "FROM comments c1 "
          + "WHERE c1.parentID = c.commentID "
          + ") c1 "
          + "WHERE c.memeID = :currMemeID AND c.parentID IS NULL")
  @RegisterRowMapper(CommentMapper.class)
  List<Comment> getCommentsFromMeme(@Bind("currMemeID") String currMemeID);

  @SqlQuery("SELECT * FROM comments WHERE parentID = :parentCommentID")
  @RegisterRowMapper(ReplyMapper.class)
  List<Comment> getAllReplies(@Bind("parentCommentID") String parentCommentID);
}
