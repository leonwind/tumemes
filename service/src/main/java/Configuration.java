import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.bundles.assets.AssetsBundleConfiguration;
import io.dropwizard.bundles.assets.AssetsConfiguration;
import io.dropwizard.db.DataSourceFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class Configuration extends io.dropwizard.Configuration
    implements AssetsBundleConfiguration {

  @Valid @NotNull private DataSourceFactory memes = new DataSourceFactory();

  @Valid @NotNull private DataSourceFactory memeVotes = new DataSourceFactory();

  @Valid @NotNull @JsonProperty
  private final AssetsConfiguration assets = AssetsConfiguration.builder().build();

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

  @Override
  public AssetsConfiguration getAssetsConfiguration() {
    return assets;
  }
}
