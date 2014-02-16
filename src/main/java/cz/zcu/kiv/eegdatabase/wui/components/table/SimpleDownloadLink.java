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
 *   SimpleDownloadLink.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.components.table;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.spring.injection.annot.SpringBean;

import cz.zcu.kiv.eegdatabase.wui.components.utils.FileUtils;
import cz.zcu.kiv.eegdatabase.wui.core.file.FileDTO;
import cz.zcu.kiv.eegdatabase.wui.core.file.FileFacade;

/**
 * Simple enhancement link for download file. In constructor is added file id and link get file after click.
 * 
 * @author Jakub Rinkes
 * 
 */
public class SimpleDownloadLink extends Link<Void> {

    private static final long serialVersionUID = -2130280618030423577L;

    @SpringBean
    FileFacade facade;

    private int fileId;

    public SimpleDownloadLink(String id, int fileId) {
        super(id);
        this.fileId = fileId;
        Injector.get().inject(this);
    }

    @Override
    public void onClick() {

        final FileDTO file = getFile();

        getRequestCycle().scheduleRequestHandlerAfterCurrent(FileUtils.prepareDownloadFile(file));
    }

    private FileDTO getFile() {
        return facade.getFile(fileId);
    }

}
