package cz.zcu.kiv.eegdatabase.logic.csv;

import cz.zcu.kiv.eegdatabase.data.pojo.Hardware;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;

/**
 * Interface for generating csv from experiments or scenarios
 * User: pbruha
 * Date: 14.12.11
 * Time: 9:38
 * To change this template use File | Settings | File Templates.
 */
public interface CSVFactory {

    /**
     * Generating csv file from scenarios
     * @return    csv file with scenarios
     * @throws java.io.IOException  - error writing to stream
     */
    public OutputStream generateScenariosCsvFile() throws IOException;

    /**
     * Generating csv file from experiments
     * @return    csv file with experiments
     * @throws java.io.IOException  - error writing to stream
     */
    public OutputStream generateExperimentsCsvFile() throws IOException;

}
