package cz.zcu.kiv.eegdatabase.wui.core.group;

import java.util.List;

import cz.zcu.kiv.eegdatabase.wui.core.dto.FullPersonDTO;

public interface ResearchGroupFacade {

    public List<ResearchGroupDTO> getResearchGroupsWhereMember(FullPersonDTO person);

    public List<ResearchGroupDTO> getResearchGroupsWhereMember(FullPersonDTO person, int limit);

    public List<ResearchGroupDTO> getResearchGroupsWhereOwner(FullPersonDTO person);

    public List<ResearchGroupDTO> getResearchGroupsWhereAbleToWriteInto(FullPersonDTO person);

    public String getResearchGroupTitle(int groupId);

    public List<ResearchGroupAccountInfo> getGroupDataForAccountOverview(FullPersonDTO person);

    public List getListOfGroupMembers(int groupId);

    public List<ResearchGroupDTO> getResearchGroupsWhereUserIsGroupAdmin(FullPersonDTO person);

    public boolean canSaveTitle(String title, int id);

    public int getCountForList();

    List getGroupsForList(int start, int limit);
}
