package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;

import java.util.List;

/**
 * Data Access Object for accessing ResearchGroup entities. The interface will use
 * only one specific couple of classes, so it doesn't need to be generic.
 *
 * @author Jindrich Pergler
 */
public interface ResearchGroupDao extends GenericDao<ResearchGroup, Integer> {

    public List<ResearchGroup> getResearchGroupsWhereMember(Person person);

    public List<ResearchGroup> getResearchGroupsWhereMember(Person person, int limit);

    public List<ResearchGroup> getResearchGroupsWhereOwner(Person person);

    public List<ResearchGroup> getResearchGroupsWhereAbleToWriteInto(Person person);

    public String getResearchGroupTitle(int groupId);

    public List getGroupDataForAccountOverview(Person person);

    public List getListOfGroupMembers(int groupId);

    public List<ResearchGroup> getResearchGroupsWhereUserIsGroupAdmin(Person person);

    public List<ResearchGroup> getRecordsNewerThan(long oracleScn);

    public boolean canSaveTitle(String title, int id);
}
