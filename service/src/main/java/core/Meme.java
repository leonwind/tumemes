package core;

import accessors.MemeImageDAO;

import java.util.Date;
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
        + "voteCount: "
        + voteCount
        + '\n'
        + "created: "
        + created;
  }
}
