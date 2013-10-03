/*******************************************************************************
 * This file is part of the EEG-database project
 * 
 *   ==========================================
 *  
 *   Copyright (C) 2013 by University of West Bohemia (http://www.zcu.cz/en/)
 *  
 *  ***********************************************************************************************************************
 *  
 *   Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 *   the License. You may obtain a copy of the License at
 *  
 *       http://www.apache.org/licenses/LICENSE-2.0
 *  
 *   Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 *   an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 *   specific language governing permissions and limitations under the License.
 *  
 *  ***********************************************************************************************************************
 *  
 *   ConfigurationSetupTest.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.data;

import org.junit.Test;
import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;

import cz.zcu.kiv.eegdatabase.wui.components.utils.PageParametersUtils;
import cz.zcu.kiv.eegdatabase.wui.ui.security.ConfirmPage;

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
