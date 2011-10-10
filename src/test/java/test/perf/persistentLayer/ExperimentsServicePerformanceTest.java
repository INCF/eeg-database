package test.perf.persistentLayer;

import cz.zcu.kiv.eegdatabase.data.dao.ExperimentDao;
import cz.zcu.kiv.eegdatabase.data.dao.HardwareDao;
import cz.zcu.kiv.eegdatabase.data.dao.HistoryDao;
import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.data.pojo.Hardware;
import cz.zcu.kiv.eegdatabase.data.pojo.History;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Kabourek
 * Date: 17.5.11
 * Time: 20:05
 * To change this template use File | Settings | File Templates.
 * Identificator of test /PPT_E_1_WorWitExp_L/. Contains document Testovaci scenare.docx.
 */
public class ExperimentsServicePerformanceTest extends PerformanceTest {

    @Autowired
    ExperimentDao experimentDao;

    @Autowired
    PersonDao personeDao;

    @Autowired
    HardwareDao hardwareDao;

    @Autowired
    HistoryDao historyDao;


    private Experiment experiment;
    private Hardware hardware;
    private History history;


    /**
     * Method test create experiment.
     * Identificator of test /PPT_E_2_AddExp_F/. Contains document Testovaci scenare.docx.
     */
    //@Test
    public void createExperimentTest(){
        experiment = new Experiment();
        hardware = new Hardware();
        history = new History();

        hardware.setTitle("hardware");
        hardware.setDescription("testovaci");
        hardware.setType("type");
        hardwareDao.create(hardware);

        history.setPerson(personeDao.getPerson("kaby"));
        historyDao.create(history);

        experiment.setHardwares((Set<Hardware>) hardware);
        experiment.setHistories((Set<History>) history);
        experiment.setPersons((Set<Person>) personeDao.getPerson("kaby"));
        experimentDao.create(experiment);

    }

   /**
     * Method test edit experiment.
     * Identificator of test /PPT_E_3_EdiExp_F/. Contains document Testovaci scenare.docx.
     */
    //@Test
    public void editExperimetnTest(){
        hardware = new Hardware();
        history = new History();

        hardware.setTitle("hardware");
        hardware.setDescription("testovaci");
        hardware.setType("type");
        hardwareDao.create(hardware);

        history.setPerson(personeDao.getPerson("kaby"));
        historyDao.create(history);

        experiment.setHardwares((Set<Hardware>) hardware);
        experiment.setHistories((Set<History>) history);
        experiment.setPersons((Set<Person>) personeDao.getPerson("kaby"));
        experimentDao.update(experiment);
    }
    /**
     * Method test insert file to experiment.
     * Identificator of test /PPT_E_4_AddDatFil_F/. Contains document Testovaci scenare.docx.
     */
    //@Test
    public void insertFileToExperimentTest(){

    }
}
