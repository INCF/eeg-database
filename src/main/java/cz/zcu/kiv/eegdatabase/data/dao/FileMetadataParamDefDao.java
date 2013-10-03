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
 *   FileMetadataParamDefDao.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.FileMetadataParamDef;
import cz.zcu.kiv.eegdatabase.data.pojo.FileMetadataParamDefGroupRel;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;

import java.util.List;

public interface FileMetadataParamDefDao extends GenericDao<FileMetadataParamDef, Integer> {
    public List<FileMetadataParamDef> getItemsForList();

    public boolean canDelete(int id);

    public List<FileMetadataParamDef> getRecordsByGroup(int groupId);

    public void createDefaultRecord(FileMetadataParamDef fileMetadataParamDef);

    public List<FileMetadataParamDef> getDefaultRecords();

    public boolean hasGroupRel(int id);

    public void deleteGroupRel(FileMetadataParamDefGroupRel fileMetadataParamDefGroupRel);

    public FileMetadataParamDefGroupRel getGroupRel(int fileMetadataParamDefId, int researchGroupId);

    public void createGroupRel(FileMetadataParamDefGroupRel fileMetadataParamDefGroupRel);

    public void createGroupRel(FileMetadataParamDef fileMetadataParamDef, ResearchGroup researchGroup);

    public boolean isDefault(int id);
}
