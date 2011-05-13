package cz.zcu.kiv.eegdatabase.test.perf;

import org.databene.contiperf.junit.ContiPerfRule;
import org.junit.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.databene.contiperf.*;
/**
 * Created by IntelliJ IDEA.
 * User: Kabourek
 * Date: 13.5.11
 * Time: 13:52
 * To change this template use File | Settings | File Templates.
 */

@ContextConfiguration(locations = {"/test-context.xml"})
@PerfTest(invocations = 10,threads = 1)
public class PerfTestCase extends AbstractJUnit4SpringContextTests{

    @Rule
    public ContiPerfRule rule =new ContiPerfRule();

    @Test
    @PerfTest(invocations = 10,threads = 1)
    public void test1()
    {
        System.out.println("AhojSvete");
    }


}
