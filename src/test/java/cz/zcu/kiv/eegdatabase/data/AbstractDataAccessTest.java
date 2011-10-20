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
        setDependencyCheck(false);
        setAutowireMode(AUTOWIRE_BY_NAME);
    }

    protected String[] getConfigLocations() {
        return new String[] {"test-context.xml"};
    }

    public void setSessionFactory(SessionFactory factory) {
        this.sessionFactory = factory;
    }



}
