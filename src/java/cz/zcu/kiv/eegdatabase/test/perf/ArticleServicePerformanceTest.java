package cz.zcu.kiv.eegdatabase.test.perf;

import org.databene.contiperf.PerfTest;
import org.junit.Test;
/**
 * Created by IntelliJ IDEA.
 * User: Kabourek
 * Date: 16.5.11
 * Time: 17:37
 * To change this template use File | Settings | File Templates.
 */

public class ArticleServicePerformanceTest  extends PerformanceTest {

    //@Test
    public void createArticleTest(){

    }

    public void createCommonsTest(){

    }

    @Test
    @PerfTest(invocations = 5, threads = 2)
    //@Required(max = 1200, average = 250)
    public void test1() throws Exception {
        Thread.sleep(200);
        System.out.println("helloWord");

    }
}
