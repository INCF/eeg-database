package cz.zcu.kiv.eegdatabase.test.perf;

import cz.zcu.kiv.eegdatabase.test.perf.service.GraphRenderer;
import org.databene.contiperf.PerfTest;
import org.databene.contiperf.junit.ContiPerfRule;
import org.junit.After;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

/**
 * Created by IntelliJ IDEA.
 * User: Kabourek
 * Date: 16.5.11
 * Time: 17:34
 * To change this template use File | Settings | File Templates.
 * Abstract class includes setting for test.
 */

@ContextConfiguration(locations = {"/test-context.xml"})
//setting perf. test
@PerfTest(invocations = 10, threads = 30)
@RunWith(JUnit4.class)
public abstract class PerformanceTest extends AbstractJUnit4SpringContextTests {

   // @Autowired
    GraphRenderer executionLogger = new GraphRenderer();
    @Rule
    public ContiPerfRule rule = new ContiPerfRule(executionLogger);

     @After
     /**
      * Method called after test, generate report.
      */
	 public void saveContiPerfResults(){
    	executionLogger.save();
	 }

}
