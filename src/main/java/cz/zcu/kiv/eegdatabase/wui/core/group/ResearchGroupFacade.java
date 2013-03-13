package cz.zcu.kiv.eegdatabase.wui.core.group;

import java.util.List;

import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroupMembership;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroupMembershipId;
import cz.zcu.kiv.eegdatabase.wui.core.GenericFacade;

public interface ResearchGroupFacade  extends GenericFacade<ResearchGroup, Integer>{

    void delete(ResearchGroup user);

    void update(ResearchGroup user);

    ResearchGroup getResearchGroupById(int id);

    List<ResearchGroup> getResearchGroupsWhereMember(Person person);

    List<ResearchGroup> getResearchGroupsWhereMember(Person person, int limit);

    List<ResearchGroup> getResearchGroupsWhereOwner(Person person);

    List<ResearchGroup> getResearchGroupsWhereAbleToWriteInto(Person person);

    String getResearchGroupTitle(int groupId);

    List<ResearchGroupAccountInfo> getGroupDataForAccountOverview(Person person);

    List getListOfGroupMembers(int groupId);

    List<ResearchGroup> getResearchGroupsWhereUserIsGroupAdmin(Person person);

    boolean canSaveTitle(String title, int id);

    int getCountForList();

    List<ResearchGroup> getGroupsForList(int start, int limit);
    
    ResearchGroupMembershipId createMemberhip(ResearchGroupMembership newInstance);

    ResearchGroupMembership readMemberhip(ResearchGroupMembershipId id);

    List<ResearchGroupMembership> readMemberhipByParameter(String parameterName, int parameterValue);

    List<ResearchGroupMembership> readMemberhipByParameter(String parameterName, String parameterValue);

    void updateMemberhip(ResearchGroupMembership transientObject);

    void deleteMemberhip(ResearchGroupMembership persistentObject);

    List<ResearchGroupMembership> getAllMemberhipRecords();

    List<ResearchGroupMembership> getMemberhipRecordsAtSides(int first, int max);

    int getCountMemberhipRecords();
}
