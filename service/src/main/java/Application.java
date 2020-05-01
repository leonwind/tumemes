import accessors.MemeDAO;
import io.dropwizard.forms.MultiPartBundle;
import io.dropwizard.jdbi3.JdbiFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.jdbi.v3.core.Jdbi;
import resources.PingResource;
import resources.MemeResource;

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
    final Jdbi jdbi =
        factory.build(environment, configuration.getDataSourceFactory(), "postgresql");
    final MemeDAO memeDAO = jdbi.onDemand(MemeDAO.class);

    final PingResource ping = new PingResource();
    final MemeResource uploadMeme = new MemeResource(memeDAO);

    environment.jersey().register(ping);
    environment.jersey().register(uploadMeme);
  }
}
