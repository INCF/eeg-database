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
 *   CSVFactory.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
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
