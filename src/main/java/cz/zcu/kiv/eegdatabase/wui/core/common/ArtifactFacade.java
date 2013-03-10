package cz.zcu.kiv.eegdatabase.wui.core.common;

import java.util.List;

import cz.zcu.kiv.eegdatabase.data.pojo.Artifact;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.wui.core.GenericFacade;

public interface ArtifactFacade extends GenericFacade<Artifact, Integer>{
    
    void createGroupRel(Artifact persistent, ResearchGroup researchGroup);

    List<Artifact> getItemsForList();

    List<Artifact> getRecordsByGroup(int groupId);

    boolean canDelete(int id);

    boolean hasGroupRel(int id);

    void deleteGroupRel(Artifact persistent, ResearchGroup researchGroup);
}
