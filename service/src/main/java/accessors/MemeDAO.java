package accessors;

import accessors.mappers.MemeMapper;
import core.Meme;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlBatch;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.Date;
import java.util.List;

public interface MemeDAO {

  @SqlUpdate(
      "INSERT INTO memes "
          + "(memeID, title, author, voteCount, created)"
          + "VALUES (:memeID, :title, :author, :voteCount, :created)")
  void insert(
      @Bind("memeID") String memeID,
      @Bind("title") String title,
      @Bind("author") String author,
      @Bind("voteCount") int voteCount,
      @Bind("created") Date created);

  @SqlQuery(
      "SELECT memeID, title, author, voteCount, created "
          + "FROM memes ORDER BY voteCount DESC LIMIT 100")
  @RegisterRowMapper(MemeMapper.class)
  List<Meme> getMemesByVoteCount();

  @SqlQuery(
      "SELECT memeID, title, author, voteCount, created "
          + "FROM memes WHERE memeID = :memeID")
  @RegisterRowMapper(MemeMapper.class)
  Meme getMemeByID(@Bind("memeID") String memeID);
}
