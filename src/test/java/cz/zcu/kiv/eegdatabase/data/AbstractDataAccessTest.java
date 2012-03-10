package cz.zcu.kiv.eegdatabase.data;

import org.hibernate.SessionFactory;
import org.junit.runner.RunWith;
import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


/**
 * Created by IntelliJ IDEA.
 * User: Jiri Vlasimsky (vlasimsky.jiri@gmail.com)
 * Date: 6.3.11
 * Time: 16:45
 * Rewritten to support Spring 3 and JUnit 4 annotations by Jiri Novotny
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/test-context.xml"})
public abstract class AbstractDataAccessTest {
    private SessionFactory sessionFactory;

    public AbstractDataAccessTest() {
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

    public void setSessionFactory(SessionFactory factory) {
        this.sessionFactory = factory;
    }
}
