package core;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class NewComment {

  private final UUID parentID;
  private final UUID memeID;
  private final String author;
  private final String text;

  public NewComment(
      @JsonProperty("parentID") UUID parentID,
      @JsonProperty("memeID") UUID memeID,
      @JsonProperty("author") String author,
      @JsonProperty("text") String text) {
    this.parentID = parentID;
    this.memeID = memeID;
    this.author = author;
    this.text = text;
  }

  public UUID getParentID() {
    return parentID;
  }

  public UUID getMemeID() {
    return memeID;
  }

  public String getAuthor() {
    return author;
  }

  public String getText() {
    return text;
  }
}
