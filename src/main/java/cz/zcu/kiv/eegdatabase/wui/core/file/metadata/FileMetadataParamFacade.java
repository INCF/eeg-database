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
 *   FileMetadataParamFacade.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.core.file.metadata;

import java.util.List;

import cz.zcu.kiv.eegdatabase.data.pojo.FileMetadataParamDef;
import cz.zcu.kiv.eegdatabase.data.pojo.FileMetadataParamDefGroupRel;
import cz.zcu.kiv.eegdatabase.data.pojo.FileMetadataParamVal;
import cz.zcu.kiv.eegdatabase.data.pojo.FileMetadataParamValId;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.wui.core.GenericFacade;

public interface FileMetadataParamFacade extends GenericFacade<FileMetadataParamDef, Integer> {

    List<FileMetadataParamDef> getItemsForList();

    boolean canDelete(int id);

    List<FileMetadataParamDef> getRecordsByGroup(int groupId);

    void createDefaultRecord(FileMetadataParamDef fileMetadataParamDef);

    List<FileMetadataParamDef> getDefaultRecords();

    boolean hasGroupRel(int id);

    void deleteGroupRel(FileMetadataParamDefGroupRel fileMetadataParamDefGroupRel);

    FileMetadataParamDefGroupRel getGroupRel(int fileMetadataParamDefId, int researchGroupId);

    void createGroupRel(FileMetadataParamDefGroupRel fileMetadataParamDefGroupRel);

    void createGroupRel(FileMetadataParamDef fileMetadataParamDef, ResearchGroup researchGroup);

    boolean isDefault(int id);
    
    FileMetadataParamValId create(FileMetadataParamVal newInstance);

    FileMetadataParamVal read(FileMetadataParamValId id);

    List<FileMetadataParamVal> readValueByParameter(String parameterName, int parameterValue);

    List<FileMetadataParamVal> readValueByParameter(String parameterName, String parameterValue);

    void update(FileMetadataParamVal transientObject);

    void delete(FileMetadataParamVal persistentObject);

    List<FileMetadataParamVal> getAllValueRecords();

    List<FileMetadataParamVal> getValueRecordsAtSides(int first, int max);

    int getValueCountRecords();
}
