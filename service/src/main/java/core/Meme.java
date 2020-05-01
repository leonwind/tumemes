package core;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.*;
import java.util.Date;
import java.util.UUID;

public class Meme {

    private final static String MEME_FILE_LOCATION = "src/main/resources/memes/";

    // id is unique to distinguish pictures
    // with same title
    private final String memeID;
    private final String title;
    private final String author;
    private final String memeFilePath;
    private int voteCount;
    private final Date created;

    /*
     * Create Meme object for uploaded
     * meme through the POST request
     */
    public Meme(
            @JsonProperty("title") String title,
            @JsonProperty("author") String author) {
        this.memeID = UUID.randomUUID().toString().replace("-", "");
        this.memeFilePath = MEME_FILE_LOCATION + memeID;
        this.title = title;
        this.author = author;
        voteCount = 0;
        created = new Date();
    }

    /*
     * Create Meme object for existing Meme
     * in the database
     */
    public Meme(String memeID, String title, String author,
                String memeFilePath, int voteCount, Date created) {
        this.memeID = memeID;
        this.title = title;
        this.author = author;
        this.memeFilePath = memeFilePath;
        this.voteCount = voteCount;
        this.created = created;
    }

    public String getMemeID() {
        return memeID;
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
        return memeFilePath;
    }

    public void saveMemeImage(InputStream image) throws IOException {
        int read = 0;
        byte[] bytes = new byte[1024];

        OutputStream outputStream = new FileOutputStream(new File(memeFilePath));

        while ((read = image.read(bytes)) != -1) {
            outputStream.write(bytes, 0, read);
        }

        outputStream.flush();
        outputStream.close();
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
        return this.getMemeID().equals(newMeme.getMemeID());
    }

    @Override
    public String toString() {
        return "memeID: " + memeID + '\n' +
                "title: " + title + '\n' +
                "author: " + author + '\n' +
                "vote count: " + voteCount + '\n' +
                "created: " + created;
    }
}
