package cz.zcu.kiv.eegdatabase.logic.hdf5converter;

import converter.DataProcessor;
import converter.ODMLParserImpl;
import converter.ZipHandler;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import java.io.File;
import java.io.IOException;

/***********************************************************************************************************************
 *
 * This file is part of the eegdatabase project

 * ==========================================
 *
 * Copyright (C) 2016 by University of West Bohemia (http://www.zcu.cz/en/)
 *
 ***********************************************************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 ***********************************************************************************************************************
 *
 * Hdf5Converter, 2016/08/15 13:55 administrator
 *
 **********************************************************************************************************************/
public class Hdf5Converter {

    private Log log = LogFactory.getLog(getClass());

    public String giveMeHDF5package(String zipFileAbsolutePath) throws IOException {
        log.info("Entered Hdf5Converter class with name - "+zipFileAbsolutePath);

        DataProcessor dataProcessor = new DataProcessor();
        String finalConvertedFolderToBeZipped = dataProcessor.generateConvertedDataSet(zipFileAbsolutePath);
        System.out.println("~~~~~ finalConvertedFolderToBeZipped : "+finalConvertedFolderToBeZipped+" ~~~~~~~~");
        String finalZipFile = finalConvertedFolderToBeZipped+"_h5.zip";
        ZipHandler.zipDir(finalZipFile,finalConvertedFolderToBeZipped);
        File zipFile = new File(finalZipFile);
        return zipFile.getAbsolutePath();
    }

}
