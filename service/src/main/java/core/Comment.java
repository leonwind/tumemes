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
  private final int voteCount;
  /** Indicate the current users vote on this comment
   * no vote = 0
   * downvote = -1
   * upvote = 1
   */
  private final int userVote;

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
    numReplies = 0;
    voteCount = 0;
    userVote = 0;
  }

  /** Create Comment object for existing Comment in the database */
  public Comment(
      UUID commentID, UUID parentID, UUID memeID, String content, String author,
      Date created, int numReplies, int voteCount, int userVote) {
    this.commentID = commentID;
    this.parentID = parentID;
    this.memeID = memeID;
    this.created = created;
    this.content = content;
    this.author = author;
    this.numReplies = numReplies;
    this.voteCount = voteCount;
    this.userVote = userVote;
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

  public int getVoteCount() {
    return voteCount;
  }

  public int getUserVote() {
    return userVote;
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
        ", numReplies=" + numReplies +
        ", voteCount=" + voteCount +
        ", userVote=" + userVote +
        '}';
  }
}
