package cz.zcu.kiv.eegdatabase.wui.core.group;

import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;

public class ResearchGroupMapper {

    public ResearchGroupDTO convertToDTO(ResearchGroup group) {

        ResearchGroupDTO dto = new ResearchGroupDTO();
        dto.setId(group.getResearchGroupId());
        dto.setTitle(group.getTitle());
        dto.setDescription(group.getTitle());

        return dto;
    }

    public ResearchGroup convertToEntity(ResearchGroupDTO dto, ResearchGroup group) {

        group.setResearchGroupId(dto.getId());
        group.setTitle(dto.getTitle());
        group.setDescription(dto.getDescription());

        return group;
    }
}
