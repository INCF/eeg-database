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
 *   FileUtils.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.components.utils;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.util.resource.AbstractResourceStreamWriter;

import cz.zcu.kiv.eegdatabase.wui.core.file.DataFileDTO;

/**
 * Utilities class for files.
 * 
 * @author Jakub Rinkes
 *
 */
public class FileUtils {
    
    /**
     * Prepared request handler for file download.
     * 
     * @param file
     * @return
     */
    public static ResourceStreamRequestHandler prepareDownloadFile(final DataFileDTO file) {

        if (file == null || file.getFileContent() == null)
            return null;

        AbstractResourceStreamWriter stream = new AbstractResourceStreamWriter()
        {
            private static final long serialVersionUID = 1L;

            public void write(OutputStream output) throws IOException
            {
                output.write(file.getFileContent());
            }
        };

        return new ResourceStreamRequestHandler(stream).setFileName(file.getFileName());
    }
}
