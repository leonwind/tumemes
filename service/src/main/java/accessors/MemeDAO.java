package accessors;

import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.Date;

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
}
