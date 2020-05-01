package core;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NewMeme {

  private final String title;
  private final String author;

  public NewMeme(@JsonProperty("title") String title, @JsonProperty("author") String author) {
    this.title = title;
    this.author = author;
  }

  public String getTitle() {
    return title;
  }

  public String getAuthor() {
    return author;
  }
}
