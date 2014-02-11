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
 *   MeasurationDataView.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.view;

import java.sql.Blob;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.view.AbstractView;
import cz.zcu.kiv.eegdatabase.data.pojo.DataFile;

/**
 * This view serves for downloading the data files.
 *
 * @author Jindra
 */
public class MeasurationDataView extends AbstractView {
    Log log = LogFactory.getLog(getClass());

    @Override
    protected void renderMergedOutputModel(Map map, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // Loading the DataFile object loaded by Controller
        DataFile data = (DataFile) map.get("dataObject");
        byte[] b = data.getFileContent().getBytes(1, (int)data.getFileContent().length());
        log.debug("Loading Data object - ID " + data.getDataFileId());

        /** Downloading the bytes for writing to output */

        // Setting the content type, so the download dialog opens and user is able to download data file
        /* TODO: load the original MIME type from database when the POJO object is ready */
        log.debug("Data MIME type is: " + data.getMimetype());
        response.setHeader("Content-Type", data.getMimetype());

        // Setting the original filename
        /* TODO: load the original filename from database when the POJO object is ready */
        log.debug("Data filename is: " + data.getFilename());
        response.setHeader("Content-Disposition", "attachment;filename=" + data.getFilename());

        // Writing the bytes to the output stream
        response.getOutputStream().write(b);
    }

}
