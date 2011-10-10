package test.logic;

import cz.zcu.kiv.eegdatabase.data.dao.ArticleDao;
import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.dao.ResearchGroupDao;
import cz.zcu.kiv.eegdatabase.data.dao.ReservationDao;
import test.data.DaoCollection;
import org.hibernate.SessionFactory;
import org.springframework.context.HierarchicalMessageSource;
import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;

/**
 * Created: 5.5.11
 *
 * @version 0.3
 * @author: Jenda Kolena, jendakolena@gmail.com
 */
public abstract class AbstractControllerTest extends AbstractTransactionalDataSourceSpringContextTests
{
    protected SessionFactory sessionFactory;
    protected ReservationDao reservationDao;
    protected PersonDao personDao;
    protected ResearchGroupDao researchGroupDao;
    protected ArticleDao articleDao;
    protected HierarchicalMessageSource messageSource;

    public AbstractControllerTest()
    {
        setDependencyCheck(false);
        setAutowireMode(AUTOWIRE_BY_NAME);
    }

    protected String[] getConfigLocations()
    {
        return new String[]{"test-context.xml"};
    }

    protected DaoCollection getDaoCollection()
    {
        return new DaoCollection(personDao, researchGroupDao, reservationDao, articleDao);
    }

    public void setSessionFactory(SessionFactory factory)
    {
        this.sessionFactory = factory;
    }

    public void setReservationDao(ReservationDao reservationDao)
    {
        this.reservationDao = reservationDao;
    }

    public void setPersonDao(PersonDao personDao)
    {
        this.personDao = personDao;
    }

    public void setResearchGroupDao(ResearchGroupDao researchGroupDao)
    {
        this.researchGroupDao = researchGroupDao;
    }

    public void setMessageSource(HierarchicalMessageSource messageSource)
    {
        this.messageSource = messageSource;
    }

    protected void debug(String message)
    {
        System.out.println("[DEBUG]: " + message);
    }
}
