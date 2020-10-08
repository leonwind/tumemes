package core;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class NewComment {

  private final UUID parentID;
  private final UUID memeID;
  private final String content;

  public NewComment(
      @JsonProperty("parentID") UUID parentID,
      @JsonProperty("memeID") UUID memeID,
      @JsonProperty("content") String content) {
    this.parentID = parentID;
    this.memeID = memeID;
    this.content = content;
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
}
