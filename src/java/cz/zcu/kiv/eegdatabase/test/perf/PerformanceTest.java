package cz.zcu.kiv.eegdatabase.test.perf;

import cz.zcu.kiv.eegdatabase.test.perf.service.GraphRenderer;
import org.databene.contiperf.PerfTest;
import org.databene.contiperf.junit.ContiPerfRule;
import org.junit.After;
import org.junit.Rule;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

/**
 * Created by IntelliJ IDEA.
 * User: Kabourek
 * Date: 16.5.11
 * Time: 17:34
 * To change this template use File | Settings | File Templates.
 * Abstraktni trida definujici parametry a chovani testu. Metody pro nastaveni testu nejsou
 * abstraktni, jednotlive testy je jiz nemusi implementovat.
 */

@ContextConfiguration(locations = {"/test-context.xml"})
//nastaveni kolikrat se ma testovaci metoda spoustet a v kolika vlaknech, pokud je nastaveni pred metodou ma vyssi prioritu.
@PerfTest(invocations = 10, threads = 30)
//@RunWith(JUnit4.class)
//@SuiteClasses(PerformanceTest.class)
//@Suite.SuiteClasses(ArticleServicePerformanceTest.class)

public abstract class PerformanceTest extends AbstractJUnit4SpringContextTests {

    //@Autowired
    GraphRenderer executionLogger = new GraphRenderer();
    @Rule
    ContiPerfRule rule = new ContiPerfRule(executionLogger);

	//@Autowired
//    public ContiPerfRule rule = new ContiPerfRule();


     @After
     /**
      * Metoda zavolana po kazdem testu, vygereruje report.
      */
	 public void saveContiPerfResults(){
    	executionLogger.save();
	 }

}
