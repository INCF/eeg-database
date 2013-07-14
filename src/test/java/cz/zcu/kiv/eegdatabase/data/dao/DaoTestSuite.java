package cz.zcu.kiv.eegdatabase.data.dao;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * TestSuite for data Layer
 *
 * User: Tomas Pokryvka
 */

@RunWith(Suite.class)
@Suite.SuiteClasses({
        ArticleDaoTest.class,
        DigitizationDaoTest.class,
        ExperimentOptParamDefDaoTest.class,
        FileMetadataParamDefDaoTest.class,
        HardwareDaoTest.class,
        PersonDaoTest.class,
        PersonOptParamDefDaoTest.class,
        ResearchGroupDaoTest.class,
        ScenarioDaoTest.class,
        SimpleReservationDaoTest.class,
        SimpleScenarioSchemasDaoTest.class,
        SimpleScenarioTypeDaoTest.class,
        StimulusDaoTest.class,
        WeatherDaoTest.class

})
public class DaoTestSuite {
}


