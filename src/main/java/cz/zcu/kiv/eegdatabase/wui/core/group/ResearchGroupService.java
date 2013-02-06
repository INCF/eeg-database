package cz.zcu.kiv.eegdatabase.wui.core.group;

import java.util.List;

import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;

public interface ResearchGroupService {
    
    void create(ResearchGroup group);

    void delete(ResearchGroup group);

    void update(ResearchGroup group);

    ResearchGroup getResearchGroupById(int id);

    public List<ResearchGroup> getResearchGroupsWhereMember(Person person);

    public List<ResearchGroup> getResearchGroupsWhereMember(Person person, int limit);

    public List<ResearchGroup> getResearchGroupsWhereOwner(Person person);

    public List<ResearchGroup> getResearchGroupsWhereAbleToWriteInto(Person person);

    public String getResearchGroupTitle(int groupId);

    public List<ResearchGroupAccountInfo> getGroupDataForAccountOverview(Person person);

    public List getListOfGroupMembers(int groupId);

    public List<ResearchGroup> getResearchGroupsWhereUserIsGroupAdmin(Person person);

    public boolean canSaveTitle(String title, int id);

    public int getCountForList();

    List getGroupsForList(int start, int limit);
}
