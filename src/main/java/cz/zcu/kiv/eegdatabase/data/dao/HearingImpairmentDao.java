package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.HearingImpairment;
import cz.zcu.kiv.eegdatabase.data.pojo.HearingImpairmentGroupRel;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;

import java.util.List;

public interface HearingImpairmentDao extends GenericDao<HearingImpairment, Integer> {
    public List<HearingImpairment> getItemsForList();

    //TODO odstranit s wizardem
    public boolean canSaveDescription(String description, int id);

    public boolean canSaveDescription(String description, int groupId, int hearingImpairmentId);

    public boolean canDelete(int id);

    public boolean canSaveDefaultDescription(String description, int hearingImpairmentId);

    public boolean hasGroupRel(int id);

    public void deleteGroupRel(HearingImpairmentGroupRel hearingImpairmentGroupRel);

    public HearingImpairmentGroupRel getGroupRel(int hearingImpairmentId, int researchGroupId);

    public void createGroupRel(HearingImpairment hearingImpairment, ResearchGroup researchGroup);

    public void createGroupRel(HearingImpairmentGroupRel hearingImpairmentGroupRel);

    public boolean isDefault(int id);

    public List<HearingImpairment> getRecordsByGroup(int groupId);

    public void createDefaultRecord(HearingImpairment hearingImpairment);

    public List<HearingImpairment> getDefaultRecords();


}
