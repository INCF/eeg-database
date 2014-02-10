/***********************************************************************************************************************
 *
 * This file is part of the EEG-database project
 *
 * =============================================
 *
 * Copyright (C) 2014 by University of West Bohemia (http://www.zcu.cz/en/)
 *
 ***********************************************************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 ***********************************************************************************************************************
 *
 * ServiceResultManager.java, 10. 02. 2014 11:27:09, Jan Stebetak
 *
 **********************************************************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.ui.signalProcessing;

import cz.zcu.kiv.eegdatabase.data.dao.ServiceResultDao;
import cz.zcu.kiv.eegdatabase.webservices.EDPClient.DataFile;
import cz.zcu.kiv.eegdatabase.webservices.EDPClient.SupportedFormat;
import cz.zcu.kiv.eegdatabase.wui.core.signalProcessing.SignalProcessingService;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by stebjan on 10.2.14.
 */
public class ServiceResultManager extends Thread {

    @Autowired
    private ServiceResultDao resultDao;

    @SpringBean
    private SignalProcessingService service;

    private List<DataFile> files;
    private SupportedFormat format;
    private String methodName;
    private List<String> parameters;

    public ServiceResultManager(List<DataFile> files, SupportedFormat format, String methodName, List<String> parameters) {

    }
}
