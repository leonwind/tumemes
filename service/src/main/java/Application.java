import accessors.MemeDAO;
import accessors.UserDAO;
import accessors.VoteDAO;
import auth.HTTPBasicAuth;
import auth.UnauthorizedResourceHandler;
import auth.UserAuthorizer;
import core.User;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthFilter;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.UnauthorizedHandler;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.auth.basic.BasicCredentials;
import io.dropwizard.bundles.assets.ConfiguredAssetsBundle;
import io.dropwizard.forms.MultiPartBundle;
import io.dropwizard.jdbi3.JdbiFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.jdbi.v3.core.Jdbi;
import resources.*;

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

    final Jdbi jdbi =
        factory.build(environment, configuration.getMemesDataSourceFactory(), "postgres");
    final MemeDAO memeDAO = jdbi.onDemand(MemeDAO.class);
    final VoteDAO voteDAO = jdbi.onDemand(VoteDAO.class);
    final UserDAO userDAO = jdbi.onDemand(UserDAO.class);

    UnauthorizedHandler unauthorizedHandler = new UnauthorizedResourceHandler();
    AuthFilter<BasicCredentials, User> basicAuthFilter =
        new BasicCredentialAuthFilter.Builder<User>()
            .setAuthenticator(new HTTPBasicAuth(userDAO))
            .setAuthorizer(new UserAuthorizer())
            .setRealm("secret realm")
            .setUnauthorizedHandler(unauthorizedHandler)
            .setPrefix("Basic")
            .buildAuthFilter();

    environment.jersey().register(new AuthDynamicFeature(basicAuthFilter));
    environment.jersey().register(RolesAllowedDynamicFeature.class);
    environment.jersey().register(new AuthValueFactoryProvider.Binder<>(User.class));
    environment.jersey().register(unauthorizedHandler);

    final PingResource pingResource = new PingResource();
    final RegisterResource registerResource = new RegisterResource(userDAO);
    final MemeResource memeResource = new MemeResource(memeDAO);
    final UploadResource uploadResource = new UploadResource(memeDAO);
    final VoteResource voteResource = new VoteResource(memeDAO, voteDAO);

    environment.jersey().register(pingResource);
    environment.jersey().register(registerResource);
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
