package core;

import java.util.Date;
import java.util.UUID;

public class Comment {

  private final UUID commentID;
  private final UUID parentID;
  private final UUID memeID;
  private final String content;
  private final String author;
  private final Date created;
  private final int numReplies;

  /** Convert new uploaded NewComment object into Comment object */
  public static Comment fromNewComment(NewComment newComment, String author) {
    return new Comment(newComment, author);
  }

  /** Create Comment object from new posted comment */
  public Comment(NewComment newComment, String author) {
    this.commentID = UUID.randomUUID();
    this.parentID = newComment.getParentID();
    this.memeID = newComment.getMemeID();
    this.author = author;
    this.content = newComment.getContent();
    created = new Date();
    this.numReplies = 0;
  }

  /** Create Comment object for existing Comment in the database */
  public Comment(
      UUID commentID, UUID parentID, UUID memeID, String content, String author,
      Date created, int numReplies) {
    this.commentID = commentID;
    this.parentID = parentID;
    this.memeID = memeID;
    this.created = created;
    this.content = content;
    this.author = author;
    this.numReplies = numReplies;
  }

  public UUID getCommentID() {
    return commentID;
  }

  public UUID getParentID() {
    return parentID;
  }

  public UUID getMemeID() {
    return memeID;
  }

  public String getContent() {
    return content;
  }

  public String getAuthor() {
    return author;
  }

  public Date getCreated() {
    return created;
  }

  public int getNumReplies() {
    return numReplies;
  }

  @Override
  public String toString() {
    return "Comment{" +
        "commentID=" + commentID +
        ", parentID=" + parentID +
        ", memeID=" + memeID +
        ", content='" + content + '\'' +
        ", author='" + author + '\'' +
        ", created=" + created +
        '}';
  }
}
