package core;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NewMeme {

  private final String title;

  public NewMeme(@JsonProperty("title") String title) {
    this.title = title;
  }

  public String getTitle() {
    return title;
  }
}
