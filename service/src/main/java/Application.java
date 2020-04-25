import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import resources.PingResource;

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

    }

    @Override
    public void run(Configuration configuration, Environment environment) {
        final PingResource ping = new PingResource();
        environment.jersey().register(ping);
    }
}