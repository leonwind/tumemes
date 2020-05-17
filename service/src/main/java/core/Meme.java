package core;

import accessors.MemeImageDAO;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class Meme {

  // id is unique to distinguish pictures
  // with same title
  private final UUID memeID;
  private final String title;
  private final String author;
  private final int voteCount;
  private final Date created;
  private final String imagePath;

  /** Convert new uploaded NewMeme object into Meme object */
  public static Meme fromNewMeme(NewMeme newMeme) {
    return new Meme(newMeme.getTitle(), newMeme.getAuthor());
  }

  /** Create Meme object from new posted meme */
  public Meme(String title, String author) {
    this.memeID = UUID.randomUUID();
    this.title = title;
    this.author = author;
    voteCount = 0;
    created = new Date();
    imagePath = MemeImageDAO.getMemeImagePath(memeID);
  }

  /** Create Meme object for existing Meme in the database */
  public Meme(UUID memeID, String title, String author, int voteCount, Date created) {
    this.memeID = memeID;
    this.title = title;
    this.author = author;
    this.voteCount = voteCount;
    this.created = created;
    imagePath = MemeImageDAO.getMemeImagePath(memeID);
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

  public int getVoteCount() {
    return voteCount;
  }

  public Date getCreated() {
    return created;
  }

  public String getImagePath() {
    return imagePath;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Meme meme = (Meme) o;
    return voteCount == meme.voteCount &&
        Objects.equals(memeID, meme.memeID) &&
        Objects.equals(title, meme.title) &&
        Objects.equals(author, meme.author) &&
        Objects.equals(created, meme.created) &&
        Objects.equals(imagePath, meme.imagePath);
  }

  @Override
  public int hashCode() {
    return Objects.hash(memeID, title, author, voteCount, created, imagePath);
  }

  @Override
  public String toString() {
    return "Meme{" +
        "memeID=" + memeID +
        ", title='" + title + '\'' +
        ", author='" + author + '\'' +
        ", voteCount=" + voteCount +
        ", created=" + created +
        ", imagePath='" + imagePath + '\'' +
        '}';
  }
}
