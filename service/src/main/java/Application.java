import accessors.*;
import auth.*;
import core.User;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthFilter;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.UnauthorizedHandler;
import io.dropwizard.bundles.assets.ConfiguredAssetsBundle;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.forms.MultiPartBundle;
import io.dropwizard.jdbi3.JdbiFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.jdbi.v3.core.Jdbi;
import resources.*;

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
    // Enable variable substitution with environment variables
    bootstrap.setConfigurationSourceProvider(
        new SubstitutingSourceProvider(
            bootstrap.getConfigurationSourceProvider(), new EnvironmentVariableSubstitutor(false)));

    bootstrap.addBundle(new MultiPartBundle());
    bootstrap.addBundle(new ConfiguredAssetsBundle("/images/", "/images/"));
  }

  @Override
  public void run(Configuration configuration, Environment environment) {
    // TODO: Fix and refactor meme class
    MemeImageDAO.setMemeFileLocation(configuration.getMemeFolder());

    final JdbiFactory factory = new JdbiFactory();
    final Jdbi jdbi = factory.build(environment, configuration.getDatabase(), "postgres");

    final MemeDAO memeDAO = jdbi.onDemand(MemeDAO.class);
    final UserDAO userDAO = jdbi.onDemand(UserDAO.class);
    final CommentDAO commentDAO = jdbi.onDemand(CommentDAO.class);
    final MemeVoteDAO memeVoteDAO = jdbi.onDemand(MemeVoteDAO.class);
    final CommentVoteDAO commentVoteDAO = jdbi.onDemand(CommentVoteDAO.class);

    UnauthorizedHandler unauthorizedHandler = new UnauthorizedResourceHandler();

    final AuthFilter<JWTCredentials, User> JWTAuthFilter =
        new JWTAuthFilter.Builder<User>()
            .setAuthenticator(new JWTAuthenticator(userDAO, configuration.getJwtSecret()))
            .setPrefix("Bearer")
            .setAuthorizer(new UserAuthorizer())
            .setUnauthorizedHandler(unauthorizedHandler)
            .buildAuthFilter();

    environment.jersey().register(new AuthDynamicFeature(JWTAuthFilter));
    environment.jersey().register(RolesAllowedDynamicFeature.class);
    environment.jersey().register(new AuthValueFactoryProvider.Binder<>(User.class));
    environment.jersey().register(unauthorizedHandler);

    final PingResource pingResource = new PingResource();
    final AuthResource authResource =
        new AuthResource(
            userDAO,
            configuration.getJwtSecret(),
            configuration.getSMTPUsername(),
            configuration.getSMTPPassword());
    final MemeResource memeResource = new MemeResource(memeDAO);
    final UploadResource uploadResource = new UploadResource(memeDAO);
    final VoteResource voteResource =
        new VoteResource(memeDAO, memeVoteDAO, commentDAO, commentVoteDAO);
    final CommentResource commentResource = new CommentResource(commentDAO, memeDAO);
    final AccountResource accountResource =
        new AccountResource(
            userDAO,
            configuration.getJwtSecret(),
            configuration.getSMTPUsername(),
            configuration.getSMTPPassword());

    environment.jersey().register(pingResource);
    environment.jersey().register(authResource);
    environment.jersey().register(memeResource);
    environment.jersey().register(uploadResource);
    environment.jersey().register(voteResource);
    environment.jersey().register(commentResource);
    environment.jersey().register(accountResource);
  }
}
