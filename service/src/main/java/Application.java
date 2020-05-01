import accessors.MemeDAO;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.forms.MultiPartBundle;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import models.Meme;
import resources.PingResource;
import resources.UploadMemeResource;


public class Application extends io.dropwizard.Application<Configuration> {

    private final HibernateBundle<Configuration> hibernate = new HibernateBundle<>(Meme.class) {
        @Override
        public DataSourceFactory getDataSourceFactory(Configuration configuration) {
            return configuration.getDataSourceFactory();
        }
    };

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
        bootstrap.addBundle(hibernate);
    }

    @Override
    public void run(Configuration configuration, Environment environment) {
        final MemeDAO dao = new MemeDAO(hibernate.getSessionFactory());

        final PingResource ping = new PingResource();
        final UploadMemeResource uploadMeme = new UploadMemeResource(dao);

        environment.jersey().register(ping);
        environment.jersey().register(uploadMeme);
    }
}