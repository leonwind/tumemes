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
  /** Indicate the current users vote on this meme.
   * no vote = 0
   * downvote = -1
   * upvote = 1
   */
  private final int userVote;
  private final int numComments;

  /** Convert new uploaded NewMeme object into Meme object */
  public static Meme fromNewMeme(NewMeme newMeme, String author) {
    return new Meme(newMeme, author);
  }

  /** Create Meme object from new posted meme */
  public Meme(NewMeme newMeme, String author) {
    this.memeID = UUID.randomUUID();
    this.title = newMeme.getTitle();
    this.author = author;
    voteCount = 0;
    created = new Date();
    imagePath = MemeImageDAO.getMemeImagePath(memeID);
    userVote = 0;
    numComments = 0;
  }

  public Meme(UUID memeID, String title, String author, int voteCount,
              Date created, int userVote, int numComments) {
    this.memeID = memeID;
    this.title = title;
    this.author = author;
    this.voteCount = voteCount;
    this.created = created;
    imagePath = MemeImageDAO.getMemeImagePath(memeID);
    this.userVote = userVote;
    this.numComments = numComments;
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

  public int getUserVote() {
    return userVote;
  }

  public int getNumComments() {
    return numComments;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Meme meme = (Meme) o;
    return Objects.equals(memeID, meme.memeID);
  }

  @Override
  public int hashCode() {
    return Objects.hash(memeID, title, author, voteCount, created,
        imagePath, userVote);
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
        ", userVote=" + userVote +
        '}';
  }
}
