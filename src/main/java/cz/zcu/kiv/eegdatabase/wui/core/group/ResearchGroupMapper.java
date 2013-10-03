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
 *   ResearchGroupMapper.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.core.group;

import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;

public class ResearchGroupMapper {

    public ResearchGroupDTO convertToDTO(ResearchGroup group) {

        ResearchGroupDTO dto = new ResearchGroupDTO();
        dto.setId(group.getResearchGroupId());
        dto.setTitle(group.getTitle());
        dto.setDescription(group.getDescription());

        return dto;
    }

    public ResearchGroup convertToEntity(ResearchGroupDTO dto, ResearchGroup group) {

        group.setResearchGroupId(dto.getId());
        group.setTitle(dto.getTitle());
        group.setDescription(dto.getDescription());

        return group;
    }
}
