package models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class Meme {

    private final String title;
    private final String author;
    private int voteCount;
    private final Date created;

    public Meme(
            @JsonProperty("title") String title,
            @JsonProperty("author") String author) {
        this.title = title;
        this.author = author;
        voteCount = 0;
        created = new Date();
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

    @Override
    public String toString() {
        return "title: " + title + '\n' +
               "author: " + author + '\n' +
               "vote count: " + voteCount + '\n' +
               "created: " + created;
    }
}
