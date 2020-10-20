package core;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class NewCommentVote {

  private final UUID commentID;
  /**
   * vote == 1 is upvote
   * vote == 0 is no vote
   * vote == -1 is downvote
   */
  private final int vote;

  public NewCommentVote(@JsonProperty("commentID") UUID commentID,
                        @JsonProperty("vote") int vote) {
    this.commentID = commentID;
    this.vote = vote;
  }

  public UUID getCommentID() {
    return commentID;
  }

  public int getVote() {
    return vote;
  }
}
