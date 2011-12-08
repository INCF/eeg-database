package cz.zcu.kiv.eegdatabase.data;

import org.hibernate.SessionFactory;
import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;


/**
 * Created by IntelliJ IDEA.
 * User: Jiri Vlasimsky (vlasimsky.jiri@gmail.com)
 * Date: 6.3.11
 * Time: 16:45
 */
public class AbstractDataAccessTest extends AbstractTransactionalDataSourceSpringContextTests{
    private SessionFactory sessionFactory;

    public AbstractDataAccessTest() {
        changeParserImplementationToXerces();//Important!
        setDependencyCheck(false);
        setAutowireMode(AUTOWIRE_BY_NAME);
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

    protected String[] getConfigLocations() {
        return new String[] {"test-context.xml"};
    }

    public void setSessionFactory(SessionFactory factory) {
        this.sessionFactory = factory;
    }



}
