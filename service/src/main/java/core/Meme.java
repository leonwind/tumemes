package core;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class Meme {

  // id is unique to distinguish pictures
  // with same title
  private final UUID memeID;
  private final String title;
  private final String author;
  private final Date created;

  /**
   * Convert new uploaded NewMeme object
   * into Meme object
   */
  public static Meme fromNewMeme(NewMeme newMeme) {
    UUID memeID = UUID.randomUUID();
    return new Meme(memeID, newMeme.getTitle(), newMeme.getAuthor());
  }

  /**
   * Create Meme object from new posted
   * meme
   */
  public Meme(UUID memeID, String title, String author) {
    this.memeID = memeID;
    this.title = title;
    this.author = author;
    created = new Date();
  }

  /**
   * Create Meme object for existing Meme
   * in the database
   */
  public Meme(UUID memeID, String title, String author, int voteCount, Date created) {
    this.memeID = memeID;
    this.title = title;
    this.author = author;
    this.created = created;
  }

  public UUID getMemeID() {
    return memeID;
  }

  public String getTitle() {
    return title;
  }

  public String getAuthor() {
    return author;
  }

  public Date getCreated() {
    return created;
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
    return "memeID: "
        + memeID
        + '\n'
        + "title: "
        + title
        + '\n'
        + "author: "
        + author
        + '\n'
        + "created: "
        + created;
  }
}
