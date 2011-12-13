package cz.zcu.kiv.eegdatabase.logic.csv;

import cz.zcu.kiv.eegdatabase.data.dao.GenericDao;
import cz.zcu.kiv.eegdatabase.data.dao.ScenarioDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Scenario;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: pbruha
 * Date: 13.12.11
 * Time: 6:56
 * To change this template use File | Settings | File Templates.
 */
public class CSVFactory {
    private Log log = LogFactory.getLog(getClass());
    @Autowired
    private ScenarioDao scenarioDao;

    private String domain;

    public OutputStream generateCsvFile() throws IOException {

        log.debug("Creating output stream");
        OutputStream out = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(out);
        List<Scenario> scenarioList = scenarioDao.getAllRecords();

        printStream.println(CSVUtils.SCENARIO_TITLE+CSVUtils.SEMICOLON+CSVUtils.SCENARIO_LENGTH+CSVUtils.SEMICOLON+CSVUtils.SCENARIO_DESCRIPTION+CSVUtils.SEMICOLON+CSVUtils.SCENARIO_DETAILS);

        for (int i = 0; i < scenarioList.size(); i++) {
            printStream.println(scenarioList.get(i).getScenarioName() + CSVUtils.SEMICOLON + scenarioList.get(i).getScenarioLength() + CSVUtils.SEMICOLON + scenarioList.get(i).getDescription()+CSVUtils.SEMICOLON+domain+CSVUtils.SCENARIO_URL+scenarioList.get(i).getScenarioId());
        }
        printStream.close();

        return out;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }
}
