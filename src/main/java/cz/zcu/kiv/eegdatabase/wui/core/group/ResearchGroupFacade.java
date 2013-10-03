package cz.zcu.kiv.eegdatabase.wui.core.group;

import cz.zcu.kiv.eegdatabase.data.pojo.*;
import cz.zcu.kiv.eegdatabase.wui.core.GenericFacade;
import org.springframework.mail.MailException;

import java.util.List;

public interface ResearchGroupFacade extends GenericFacade<ResearchGroup, Integer> {

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

    // MEMBERSHIP
    ResearchGroupMembershipId createMemberhip(ResearchGroupMembership newInstance);

    ResearchGroupMembership readMemberhip(ResearchGroupMembershipId id);

    List<ResearchGroupMembership> readMemberhipByParameter(String parameterName, int parameterValue);

    List<ResearchGroupMembership> readMemberhipByParameter(String parameterName, String parameterValue);

    void updateMemberhip(ResearchGroupMembership transientObject);

    void deleteMemberhip(ResearchGroupMembership persistentObject);

    List<ResearchGroupMembership> getAllMemberhipRecords();

    List<ResearchGroupMembership> getMemberhipRecordsAtSides(int first, int max);

    int getCountMemberhipRecords();

    // GROUP ROLE REQUEST
    Integer createPermissionRequest(GroupPermissionRequest newInstance) throws MailException;

    GroupPermissionRequest readPermissionRequest(int id);

    List<GroupPermissionRequest> readPermissionRequestByParameter(String parameterName, int parameterValue);

    List<GroupPermissionRequest> readPermissionRequestByParameter(String parameterName, String parameterValue);

    void updatePermissionRequest(GroupPermissionRequest transientObject);

    void deletePermissionRequest(GroupPermissionRequest persistentObject);

    List<GroupPermissionRequest> getAllPermissionRequestRecords();

    List<GroupPermissionRequest> getPermissionRequestRecordsAtSides(int first, int max);

    int getCountPermissionRequestRecords();

    void grantGroupPermisson(GroupPermissionRequest request);

    public boolean existsGroup(String title);

    ResearchGroup getGroupByTitle(String title);

}
