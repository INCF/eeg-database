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

