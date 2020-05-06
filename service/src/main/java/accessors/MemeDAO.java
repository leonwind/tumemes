package accessors;

import accessors.mappers.MemeMapper;
import core.Meme;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

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

  @SqlQuery(
      "SELECT memes.memeID as memeID, memes.title as title, memes.author as author, "
          + "COALESCE(sum(memevotes.vote), 0) as voteCount, memes.created as created "
          + "FROM memes LEFT JOIN memeVotes ON memes.memeID = memeVotes.memeID "
          + "GROUP BY memes.memeID ORDER BY voteCount DESC")
  @RegisterRowMapper(MemeMapper.class)
  List<Meme> getAllMemesByVotes();

  @SqlQuery("SELECT memes.memeID as memeID, memes.title as title, memes" +
          ".author as author, COALESCE(sum(memevotes.vote), 0) as " +
          "voteCount, memes.created as" +
          " created FROM memes LEFT JOIN memeVotes ON :currMemeID = memeVotes" +
          ".memeID GROUP BY memes.memeID")
  @RegisterRowMapper(MemeMapper.class)
  Meme getMemeByID(@Bind("currMemeID") String currMemeID);
}
