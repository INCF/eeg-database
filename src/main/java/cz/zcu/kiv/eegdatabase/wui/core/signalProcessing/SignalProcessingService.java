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
 * SignalProcessingService.java, 13. 12. 2012 12:33:09, Jan Stebetak
 *
 **********************************************************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.core.signalProcessing;

import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.ServiceResult;
import cz.zcu.kiv.eegdatabase.logic.signal.ChannelInfo;
import cz.zcu.kiv.eegdatabase.webservices.EDPClient.DataFile;
import cz.zcu.kiv.eegdatabase.webservices.EDPClient.MethodParameters;
import cz.zcu.kiv.eegdatabase.webservices.EDPClient.SupportedFormat;
import cz.zcu.kiv.eegdatabase.wui.core.GenericService;
import cz.zcu.kiv.eegdatabase.wui.core.file.FileDTO;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: stebjan
 * Date: 11.12.13
 * Time: 13:15
 * To change this template use File | Settings | File Templates.
 */
public interface SignalProcessingService extends GenericService<ServiceResult, Integer> {

    public List<String> getAvailableMethods();

    public List<String> getSuitableHeaders(int experimentId);

    public List<MethodParameters> getMethodParameters(String methodName);

    public List<ChannelInfo> getChannelInfo(int experimentId, String header) throws SQLException;

    public List<DataFile> getDataFiles(int experimentId, String header);

    public byte[] processService(List<DataFile> files, SupportedFormat format, String methodName, List<String> params);

    public Person getLoggedPerson();

    public List<ServiceResult> getResults(Person person);

    @Transactional(readOnly = true)
    FileDTO getResultFile(int resultId);

    public Blob createBlob(byte[] content);
}