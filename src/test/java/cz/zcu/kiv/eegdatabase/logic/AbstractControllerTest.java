package cz.zcu.kiv.eegdatabase.logic;

import cz.zcu.kiv.eegdatabase.data.dao.ArticleDao;
import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.dao.ResearchGroupDao;
import cz.zcu.kiv.eegdatabase.data.dao.ReservationDao;
import cz.zcu.kiv.eegdatabase.data.DaoCollection;
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
        changeParserImplementationToXerces();//Important!
    }

    /**
     * If not changed, oracle parser would try to parse hibernate configurations
     * and fail with the following error:
     * ERROR ErrorLogger - Error parsing XML (31) : http://hibernate.sourceforge.net/hibernate-mapping-3.0.dtd<Line 31, Column 2>:
     * XML-20068: (Fatal Error) content model is not deterministic
     * org.hibernate.InvalidMappingException: Unable to read XML
     * Setting SAXParserFactory and DocumentBuilderFactory will change the parser
     * to xerces, enabling Hibernate-based tests
     */
    private void changeParserImplementationToXerces() {
        System.setProperty("javax.xml.parsers.SAXParserFactory","org.apache.xerces.jaxp.SAXParserFactoryImpl");
        System.setProperty("javax.xml.parsers.DocumentBuilderFactory","org.apache.xerces.jaxp.DocumentBuilderFactoryImpl");
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
