package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.data.pojo.VisualImpairment;
import cz.zcu.kiv.eegdatabase.data.pojo.VisualImpairmentGroupRel;

import java.util.List;

public interface VisualImpairmentDao extends GenericDao<VisualImpairment, Integer> {
    public List<VisualImpairment> getItemsForList();

    //TODO odstranit s wizardem
    public boolean canSaveDescription(String description, int id);

    public boolean canSaveDescription(String description, int groupId, int visualImpairmentId);

    public boolean canDelete(int id);

    public boolean canSaveDefaultDescription(String description, int visualImpairmentId);

    public boolean hasGroupRel(int id);

    public void deleteGroupRel(VisualImpairmentGroupRel visualImpairmentGroupRel);

    public VisualImpairmentGroupRel getGroupRel(int visualImpairmentId, int researchGroupId);

    public void createGroupRel(VisualImpairmentGroupRel visualImpairmentGroupRel);

    public void createGroupRel(VisualImpairment visualImpairment, ResearchGroup researchGroup);

    public boolean isDefault(int id);

    public List<VisualImpairment> getRecordsByGroup(int groupId);

    public void createDefaultRecord(VisualImpairment visualImpairment);

    public List<VisualImpairment> getDefaultRecords();


}
