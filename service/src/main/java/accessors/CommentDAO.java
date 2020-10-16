package accessors;

import accessors.mappers.CommentMapper;
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

  @SqlQuery("SELECT * FROM comments WHERE memeID = :currMemeID AND parentID IS NULL")
  @RegisterRowMapper(CommentMapper.class)
  List<Comment> getCommentsFromMeme(@Bind("currMemeID") String currMemeID);

  @SqlQuery("SELECT * FROM comments WHERE parentID = :parentCommentID")
  @RegisterRowMapper(CommentMapper.class)
  List<Comment> getAllReplies(@Bind("parentCommentID") String parentCommentID);
}
