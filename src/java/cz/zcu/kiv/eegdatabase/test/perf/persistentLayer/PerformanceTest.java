package cz.zcu.kiv.eegdatabase.test.perf.persistentLayer;

import org.hibernate.SessionFactory;
import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;

/**
 * Created by IntelliJ IDEA.
 * User: Kabourek
 * Date: 16.5.11
 * Time: 17:34
 * To change this template use File | Settings | File Templates.
 * Abstract class includes setting for test.
 */

//@ContextConfiguration(locations = {"/test-context.xml"})
//setting perf. test
//@PerfTest(invocations = 10, threads = 1)
//@RunWith(JUnit4.class)
public class PerformanceTest extends AbstractTransactionalDataSourceSpringContextTests {


//    GraphRenderer executionLogger = new GraphRenderer();
//    @Rule
//    public ContiPerfRule rule = new ContiPerfRule(executionLogger);
     private SessionFactory sessionFactory;

     public PerformanceTest() {
        setDependencyCheck(false);
        setAutowireMode(AUTOWIRE_BY_NAME);
    }

    protected String[] getConfigLocations() {
        return new String[] {"test-context.xml"};
    }

    public void setSessionFactory(SessionFactory factory) {
    this.sessionFactory = factory;
    }

//     @After
//     /**
//      * Method called after test, generate report.
//      */
//	 public void saveContiPerfResults(){
//    	executionLogger.save();
//	 }



}
