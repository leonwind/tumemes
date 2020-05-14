import accessors.MemeDAO;
import accessors.VoteDAO;
import io.dropwizard.bundles.assets.ConfiguredAssetsBundle;
import io.dropwizard.forms.MultiPartBundle;
import io.dropwizard.jdbi3.JdbiFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.jdbi.v3.core.Jdbi;
import resources.MemeResource;
import resources.PingResource;
import resources.UploadResource;
import resources.VoteResource;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import java.util.EnumSet;

public class Application extends io.dropwizard.Application<Configuration> {

  public static void main(String[] args) throws Exception {
    new Application().run(args);
  }

  @Override
  public String getName() {
    return "TUMemes";
  }

  @Override
  public void initialize(Bootstrap<Configuration> bootstrap) {
    bootstrap.addBundle(new MultiPartBundle());
    bootstrap.addBundle(new ConfiguredAssetsBundle("/images/", "/images/"));
  }

  @Override
  public void run(Configuration configuration, Environment environment) {
    configureCORS(environment);

    // setup databases
    final JdbiFactory factory = new JdbiFactory();

    final Jdbi memesJdbi =
        factory.build(environment, configuration.getMemesDataSourceFactory(), "memes");
    final MemeDAO memeDAO = memesJdbi.onDemand(MemeDAO.class);

    final Jdbi memeVotesJdbi =
        factory.build(environment, configuration.getMemeVotesDataSourceFactory(), "memeVotes");
    final VoteDAO voteDAO = memeVotesJdbi.onDemand(VoteDAO.class);

    final PingResource pingResource = new PingResource();
    final MemeResource memeResource = new MemeResource(memeDAO);
    final UploadResource uploadResource = new UploadResource(memeDAO);
    final VoteResource voteResource = new VoteResource(memeDAO, voteDAO);

    environment.jersey().register(pingResource);
    environment.jersey().register(memeResource);
    environment.jersey().register(uploadResource);
    environment.jersey().register(voteResource);
  }

  private void configureCORS(Environment environment) {
    // Enable CORS headers
    final FilterRegistration.Dynamic cors =
        environment.servlets().addFilter("CORS", CrossOriginFilter.class);

    // Configure CORS parameters
    cors.setInitParameter("allowedOrigins", "*");
    cors.setInitParameter("allowedHeaders", "X-Requested-With,Content-Type,Accept,Origin");
    cors.setInitParameter("allowedMethods", "OPTIONS,GET,PUT,POST,DELETE,HEAD");

    // Add URL mapping
    cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
  }
}
