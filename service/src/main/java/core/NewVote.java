package core;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class NewVote {

  private final UUID memeID;
  /**
   * vote == 1 is upvote
   * vote == 0 is no vote
   * vote == -1 is downvote
   */
  private final int vote;

  public NewVote(@JsonProperty("memeID") UUID memeID,
                 @JsonProperty("vote") int vote) {
    this.memeID = memeID;
    this.vote = vote;
  }

  public UUID getMemeID() {
    return memeID;
  }

  public int getVote() {
    return vote;
  }
}
