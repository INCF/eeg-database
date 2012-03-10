package cz.zcu.kiv.eegdatabase.data;

import org.junit.Test;
import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;

/**
 * Created by IntelliJ IDEA.
 * User: jnovotny
 * Date: 10.11.11
 * Time: 12:59
 * This test verifies that the test-context.xml is not seriously misconfigured
 * by trying to initialize a test using it.
 * Body of the test method is empty, it will fail if there is an configuration error
 * in AbstractDataAccessTest, test-context.xml or linked files
 * (hibernate config, hibernate mapping, project properties)
 */
public class ConfigurationSetupTest extends AbstractDataAccessTest {

	@Test
	public void testInitializationFromTestContext(){
		System.out.println("test-context.xml initialization succeeded");
	}


    protected String[] getConfigLocations() {
        return new String[] {"test-context.xml"};
        //Uncomment this if you want the test to use production contexts. Warning: Might need additional configuration to work properly
        //return new String[] {"file:src/main/webapp/WEB-INF/persistence.xml","file:src/main/webapp/WEB-INF/controllers.xml"};
    }
}
