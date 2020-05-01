package models;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "memeDatabase")
public class Meme {

    private final static String MEME_FILE_LOCATION = "src/main/resources/memes/";

    // id is unique to distinguish pictures
    // with same title
    @Id
    @Column(name = "id", nullable = false)
    @NotNull
    private final String id;

    @Column(name = "title", length = 100, nullable = false)
    @NotNull
    @JsonProperty
    private final String title;

    @Column(name = "author", length = 100, nullable = false)
    @NotNull
    @JsonProperty
    private final String author;
    @Column(name = "created", nullable = false)
    @NotNull
    private final Date created;
    @Column(name = "voteCount", nullable = false)
    @NotNull
    private int voteCount;

    public Meme(
            @JsonProperty("title") String title,
            @JsonProperty("author") String author) {
        this.id = "";
        this.title = title;
        this.author = author;
        voteCount = 0;
        created = new Date();
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public Date getCreated() {
        return created;
    }

    public void increaseVoteCount() {
        voteCount++;
    }

    public void decreaseVoteCount() {
        voteCount--;
    }

    public String getMemeFilePath() {
        return MEME_FILE_LOCATION + id;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (object == null || object.getClass() != this.getClass()) {
            return false;
        }

        Meme newMeme = (Meme) object;
        return this.getId().equals(newMeme.getId());
    }

    @Override
    public String toString() {
        return "title: " + title + '\n' +
                "author: " + author + '\n' +
                "vote count: " + voteCount + '\n' +
                "created: " + created;
    }
}
