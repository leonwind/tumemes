package accessors;

import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.Date;

public interface MemeDAO {

    @SqlUpdate("create table memeDB " +
            "(memeID varchar(32) primary key, title varchar, author varchar," +
            "memeFilePath varchar, voteCount int, created Date)")
    void createMemeDBTable();

    @SqlUpdate("insert into memeDB " +
            "(memeID, title, author, memeFilePath, voteCount, created)" +
            "values (:memeID, :title, :author, :memeFilePath, :voteCount, :created)")
    void insert(@Bind("memeID") String memeID, @Bind("title") String title,
                @Bind("author") String author, @Bind("memeFilePath") String memeFilePath,
                @Bind("voteCount") int voteCount, @Bind("Created") Date created);
}
