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

  @SqlUpdate(
      "create table memeDB "
          + "(memeID varchar(32) primary key, title varchar, "
          + "author varchar, voteCount int, created Date)")
  void createMemeDBTable();

  @SqlUpdate(
      "insert into memeDB "
          + "(memeID, title, author, voteCount, created)"
          + "values (:memeID, :title, :author, :voteCount, :created)")
  void insert(
      @Bind("memeID") String memeID,
      @Bind("title") String title,
      @Bind("author") String author,
      @Bind("voteCount") int voteCount,
      @Bind("Created") Date created);

  @SqlQuery(
      "SELECT memeID, title, author, voteCount, created"
          + "FROM memeDB order by voteCount desc limit 100")
  @RegisterRowMapper(MemeMapper.class)
  List<Meme> getMemesByVoteCount();

  @SqlQuery(
      "SELECT memeID, title, author, voteCount, created" + "FROM memeDB WHERE memeID = :memeID")
  @RegisterRowMapper(MemeMapper.class)
  Meme getMemeByID(@Bind("memeID") String memeID);
}
