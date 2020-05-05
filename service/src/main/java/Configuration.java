import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.db.DataSourceFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class Configuration extends io.dropwizard.Configuration {
  @Valid @NotNull private DataSourceFactory memes = new DataSourceFactory();
  @Valid @NotNull private DataSourceFactory memeVotes = new DataSourceFactory();

  @JsonProperty("memes")
  public void setMemesDataSourceFactory(DataSourceFactory factory) {
    memes = factory;
  }

  @JsonProperty("memes")
  public DataSourceFactory getMemesDataSourceFactory() {
    return memes;
  }

  @JsonProperty("memeVotes")
  public void setMemeVotesDataSourceFactory(DataSourceFactory factory) {
    memeVotes = factory;
  }

  @JsonProperty("memeVotes")
  public DataSourceFactory getMemeVotesDataSourceFactory() {
    return memeVotes;
  }
}
