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
 *   SystemTestSuite.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.selenium.webdriver;


import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * TestSuite for Dao Layer
 *
 * User: Tomas Pokryvka
 */

@RunWith(Suite.class)
@Suite.SuiteClasses({
        AddHardwareDefinitionNSAdminTest.class,
        AddHardwareDefinitionNSExperimenterTest.class,
        AddHardwareDefinitionNSUserTest.class,
        AddScenarionNSAdminTest.class,
        AddScenarionNSExperimenterTest.class,
        AddScenarionNSUserTest.class,
        CreateHardwareDefinitionAsUserTest.class,
        CreateHardwareDefinitionAsUserNSTest.class,
        CreateNewResearchGroupUserTest.class,
        CreateNewScenarioUserTest.class,
        CreateResearchGroupNSAdminTest.class,
        CreateResearchGroupNSExperimenterTest.class,
        CreateResearchGroupNSUserTest.class,
        ForgottenPasswordNSTest.class,
        ChangePasswordAdminTest.class,
        ChangePasswordExperimenterTest.class,
        ChangePasswordReaderTest.class,
        ChangePasswordUserTest.class,
        ChangePasswordNSTest.class,
        ChangeRoleExperimenterToAdminTest.class,
        ChangeRoleNSTest.class,
        ChangeRoleReaderToAdminTest.class,
        ChangeRoleUserToAdminTest.class,
        LoginExperimenterTest.class,
        LoginReaderTest.class,
        LoginUserTest.class,
        LoginNSTest.class,
        RegistrationNSTest.class,
        VerifyMenuItemsAdminTest.class,
        VerifyMenuItemsExperimenterTest.class,
        VerifyMenuItemsReaderTest.class,
        VerifyMenuItemsUserTest.class
})
public class SystemTestSuite {
}

