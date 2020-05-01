package accessors;

import io.dropwizard.hibernate.AbstractDAO;
import models.Meme;
import org.hibernate.SessionFactory;


public class MemeDAO extends AbstractDAO<Meme> {

    public MemeDAO(SessionFactory factory) {
        super(factory);
    }

    public Meme insert(Meme meme) {
        return persist(meme);
    }

    public void delete(Meme meme) {
        currentSession().delete(meme);
    }

    public void update(Meme meme) {
        currentSession().merge(meme);
    }

    public Meme findById(String id) {
        return currentSession().get(Meme.class, id);
    }

    public boolean idExists(String id) {
        return false;
    }
}
