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
 *   FileDownloadStreamWriter.java, 2014/02/16 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.components.form.input;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.util.lang.Bytes;
import org.apache.wicket.util.resource.AbstractResourceStreamWriter;

/**
 * Wicket resource stream writer used for downloading files from wicket.
 * 
 * @author Jakub Rinkes
 *
 */
public class FileDownloadStreamWriter extends AbstractResourceStreamWriter {

    private static final long serialVersionUID = 4809392672411939258L;

    protected Log log = LogFactory.getLog(getClass());

    protected String contentType;
    private Bytes length;
    private File file;

    public FileDownloadStreamWriter(File file, String contentType) {

        this.file = file;
        this.contentType = contentType;
        this.length = Bytes.bytes(file.length());
    }

    @Override
    public void write(OutputStream output) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(file);
        IOUtils.copy(fileInputStream, output);
        
        fileInputStream.close();
        if(!file.delete()){
            log.warn("temporary file " + file.getName() + " cannot be deleted.");
        }
    }

    @Override
    public String getContentType() {
        return this.contentType;
    }

    @Override
    public Bytes length() {
        return this.length;
    }
    
    

}
