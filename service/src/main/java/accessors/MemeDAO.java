package accessors;

import accessors.mappers.MemeMapper;
import core.Meme;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public interface MemeDAO {

  @SqlQuery("SELECT EXISTS (SELECT 1 FROM memes WHERE memeID = :currMemeID)")
  boolean memeIDExists(@Bind("currMemeID") String currMemeID);

  @SqlUpdate(
      "INSERT INTO memes "
          + "(memeID, title, author, created)"
          + "VALUES (:memeID, :title, :author, :created)")
  void insert(
      @Bind("memeID") String memeID,
      @Bind("title") String title,
      @Bind("author") String author,
      @Bind("created") Date created);

  /**
   * Sorry for this shit indentation. The Google Java Formatter does not allow formatter:off, making
   * it impossible to proper indent the query
   */
  @SqlQuery(
      "SELECT memes.memeID as memeID, memes.title as title, memes.author as "
          + "author, memes.created as created, "
          + "COALESCE(v.voteCount, 0) as voteCount,"
          + "COALESCE(v.userVote, 0) as userVote,"
          + "COALESCE(c.numComments, 0) as numComments "
          + "FROM memes "
          + "LEFT JOIN ( "
          + "SELECT memeID, "
          + "SUM(vote) as voteCount, "
          + "SUM(vote) FILTER (WHERE username = :currUsername) as userVote "
          + "FROM memevotes "
          + "GROUP BY memeID "
          + ") v on v.memeID = memes.memeID "
          + "LEFT JOIN ( "
          + "SELECT memeID, "
          + "COUNT(*) as numComments "
          + "FROM comments "
          + "WHERE parentID IS NULL "
          + "GROUP BY memeID "
          + ") c on c.memeID = memes.memeID "
          + "WHERE voteCount <= :maxVote "
          + "AND (:fromUser IS FALSE OR author = :currUsername)"
          + "ORDER BY voteCount DESC, created DESC "
          + "LIMIT 10")
  @RegisterRowMapper(MemeMapper.class)
  List<Meme> getAllMemesByVotes(
      @Bind("currUsername") String username,
      @Bind("maxVote") long maxVote,
      @Bind("fromUser") boolean fromUser);

  @SqlQuery(
      "SELECT memes.memeID as memeID, memes.title as title, memes.author as "
          + "author, memes.created as created, "
          + "COALESCE(v.voteCount, 0) as voteCount, "
          + "COALESCE(v.userVote, 0) as userVote, "
          + "COALESCE(c.numComments, 0) as numComments "
          + "FROM memes "
          + "LEFT JOIN ( "
          + "SELECT memeID, "
          + "SUM(vote) as voteCount, "
          + "SUM(vote) FILTER (WHERE username = :currUsername) as userVote "
          + "FROM memevotes "
          + "GROUP BY memeID "
          + ") v on v.memeID = memes.memeID "
          + "LEFT JOIN ( "
          + "SELECT memeID, "
          + "COUNT(*) as numComments "
          + "FROM comments "
          + "WHERE parentID IS NULL "
          + "GROUP BY memeID "
          + ") c on c.memeID = memes.memeID "
          + "WHERE created < :lastTimestamp "
          + "AND (:fromUser IS FALSE OR author = :currUsername) "
          + "ORDER BY created DESC "
          + "LIMIT 10")
  @RegisterRowMapper(MemeMapper.class)
  List<Meme> getAllMemesByDate(
      @Bind("currUsername") String username,
      @Bind("lastTimestamp") Timestamp lastTimestamp,
      @Bind("fromUser") boolean fromUser);

  @SqlQuery(
      "SELECT memes.memeID as memeID, memes.title as title, memes.author as "
          + "author, memes.created as created, "
          + "COALESCE(v.voteCount, 0) as voteCount,"
          + "COALESCE(v.userVote, 0) as userVote,"
          + "COALESCE(c.numComments, 0) as numComments "
          + "FROM memes "
          + "LEFT JOIN ( "
          + "SELECT memeID, "
          + "SUM(vote) as voteCount, "
          + "SUM(vote) FILTER (WHERE username = :currUsername) as userVote "
          + "FROM memevotes "
          + "GROUP BY memeID "
          + ") v on v.memeID = :currMemeID "
          + "LEFT JOIN ( "
          + "SELECT memeID, "
          + "COUNT(*) as numComments "
          + "FROM comments "
          + "WHERE parentID IS NULL "
          + "GROUP BY memeID "
          + ") c on c.memeID = :currMemeID "
          + "WHERE memes.memeID = :currMemeID ")
  @RegisterRowMapper(MemeMapper.class)
  Meme getMemeByID(@Bind("currMemeID") String currMemeID, @Bind("currUsername") String username);
}
