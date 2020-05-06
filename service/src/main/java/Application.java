import accessors.MemeDAO;
import accessors.VoteDAO;
import io.dropwizard.forms.MultiPartBundle;
import io.dropwizard.jdbi3.JdbiFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.jdbi.v3.core.Jdbi;
import resources.MemeResource;
import resources.PingResource;
import resources.UploadResource;
import resources.VoteResource;

public class Application extends io.dropwizard.Application<Configuration> {

  public static void main(String[] args) throws Exception {
    new Application().run(args);
  }

  @Override
  public String getName() {
    return "tu-memes";
  }

  @Override
  public void initialize(Bootstrap<Configuration> bootstrap) {
    bootstrap.addBundle(new MultiPartBundle());
  }

  @Override
  public void run(Configuration configuration, Environment environment) {
    final JdbiFactory factory = new JdbiFactory();

    final Jdbi memesJdbi =
        factory.build(environment, configuration.getMemesDataSourceFactory(),
                "memes");
    final MemeDAO memeDAO = memesJdbi.onDemand(MemeDAO.class);

    final Jdbi memeVotesJdbi =
        factory.build(environment,
                configuration.getMemeVotesDataSourceFactory(), "memeVotes");
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
}
