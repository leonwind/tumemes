import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.bundles.assets.AssetsBundleConfiguration;
import io.dropwizard.bundles.assets.AssetsConfiguration;
import io.dropwizard.db.DataSourceFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class Configuration extends io.dropwizard.Configuration
    implements AssetsBundleConfiguration {

  @Valid @NotNull private String jwtSecret;
  @Valid @NotNull private DataSourceFactory database = new DataSourceFactory();

  @Valid @NotNull @JsonProperty
  private final AssetsConfiguration assets = AssetsConfiguration.builder().build();

  public String getJwtSecret() {
    return jwtSecret;
  }

  @JsonProperty("database")
  public void setDatabase(DataSourceFactory factory) {
    database = factory;
  }

  @JsonProperty("memes")
  public DataSourceFactory getDatabase() {
    return database;
  }

  @Override
  public AssetsConfiguration getAssetsConfiguration() {
    return assets;
  }
}
