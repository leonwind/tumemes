import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.bundles.assets.AssetsBundleConfiguration;
import io.dropwizard.bundles.assets.AssetsConfiguration;
import io.dropwizard.db.DataSourceFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Configuration extends io.dropwizard.Configuration
    implements AssetsBundleConfiguration {

  @Valid @NotNull @JsonProperty private String jwtSecret;
  @Valid @NotNull @JsonProperty private String smtpUsername;
  @Valid @NotNull @JsonProperty private String smtpPassword;
  @Valid @NotNull @JsonProperty private String memeFolder;
  @Valid @NotNull private DataSourceFactory database = new DataSourceFactory();

  @Valid @NotNull @JsonProperty
  private final AssetsConfiguration assets = AssetsConfiguration.builder().build();

  public String getJwtSecret() {
    return jwtSecret;
  }

 public String getSMTPUsername() {
    return smtpUsername;
  }

  public String getSMTPPassword() {
    return smtpPassword;
  }

  public Path getMemeFolder() {
    return Paths.get(memeFolder);
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
