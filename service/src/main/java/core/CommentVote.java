package core;

import java.util.UUID;

public class CommentVote {

  private final UUID commentID;
  /**
   * vote == 1 is upvote
   * vote == 0 is no vote
   * vote == -1 is downvote
   */
  private final int vote;
  private final String username;

  public CommentVote(UUID commentID, int vote, String username) {
    this.commentID = commentID;
    this.vote = vote;
    this.username = username;
  }

  public UUID getCommentID() {
    return commentID;
  }

  public int getVote() {
    return vote;
  }
}
