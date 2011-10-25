package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.logic.Util;

import java.util.List;

/**
 * Implementation of DAO for accessing ResearchGroup entities. This class will use
 * only one specific couple of classes, so it doesn't need to be generic.
 *
 * @author Jindrich Pergler
 */
public class SimpleResearchGroupDao
        extends SimpleGenericDao<ResearchGroup, Integer>
        implements ResearchGroupDao {

    public SimpleResearchGroupDao() {
        super(ResearchGroup.class);
    }

    /**
     * Returns list of research groups, which the person is member of.
     *
     * @param person Object of class Person
     * @return The list with groups
     */
    public List<ResearchGroup> getResearchGroupsWhereMember(Person person) {
        String hqlQuery = "from ResearchGroup researchGroup "
                + "left join fetch researchGroup.researchGroupMemberships as membership "
                + "where membership.person.personId = :personId "
                + "order by researchGroup.title";
        return getHibernateTemplate().findByNamedParam(hqlQuery, "personId", person.getPersonId());
    }

    public List<ResearchGroup> getResearchGroupsWhereMember(Person person, int limit) {
        String hqlQuery = "from ResearchGroup researchGroup "
                + "left join fetch researchGroup.researchGroupMemberships as membership "
                + "where membership.person.personId = :personId "
                + "order by researchGroup.title";

        getHibernateTemplate().setMaxResults(limit);
        List<ResearchGroup> list = getHibernateTemplate().findByNamedParam(hqlQuery, "personId", person.getPersonId());
        getHibernateTemplate().setMaxResults(0);
        return list;
    }

    /**
     * Returns list of research groups, which the person owns.
     *
     * @param person Owner of the searched groups
     * @return The list with groups
     */
    public List<ResearchGroup> getResearchGroupsWhereOwner(Person person) {
        String hqlQuery = "from ResearchGroup researchGroup "
                + "where researchGroup.person.personId = :ownerId";
        return getHibernateTemplate().findByNamedParam(hqlQuery, "ownerId", person.getPersonId());
    }

    public List<ResearchGroup> getResearchGroupsWhereAbleToWriteInto(Person person) {
        String hqlQuery = "from ResearchGroup researchGroup "
                + "left join fetch researchGroup.researchGroupMemberships as membership "
                + "where membership.person.personId = :personId "
                + "and (membership.authority = :experimenter or membership.authority = :admin) "
                + "order by researchGroup.title";

        String[] names = {"personId", "experimenter", "admin"};
        Object[] params = {person.getPersonId(), Util.GROUP_EXPERIMENTER, Util.GROUP_ADMIN};
        List<ResearchGroup> list = getHibernateTemplate().findByNamedParam(hqlQuery, names, params);
        return list;
    }

    public List getGroupDataForAccountOverview(Person person) {
        String hqlQuery = "select new map("
                + "g.researchGroupId as groupId, "
                + "g.title as groupTitle, "
                + "m.authority as authority) "
                + "from ResearchGroup g "
                + "left join g.researchGroupMemberships m "
                + "where m.person.personId = :personId";
        List list = getHibernateTemplate().findByNamedParam(hqlQuery, "personId", person.getPersonId());
        return list;
    }

    public List getListOfGroupMembers(int groupId) {
        String hqlQuery = "select new map("
                + "p.personId as personId, "
                + "p.givenname as givenname, "
                + "p.surname as surname, "
                + "p.username as username, "
                + "rgm.authority as authority"
                + ")"
                + "from Person p "
                + "left join p.researchGroupMemberships rgm "
                + "where rgm.researchGroup.researchGroupId = :groupId "
                + "order by surname, givenname";
        List list = getHibernateTemplate().findByNamedParam(hqlQuery, "groupId", groupId);
        return list;
    }

    public List<ResearchGroup> getResearchGroupsWhereUserIsGroupAdmin(Person person) {
        String hqlQuery = "from ResearchGroup researchGroup "
                + "left join fetch researchGroup.researchGroupMemberships as membership "
                + "where membership.person.personId = :personId "
                + "and (membership.authority = :admin) "
                + "order by researchGroup.title";

        String[] names = {"personId", "admin"};
        Object[] params = {person.getPersonId(), Util.GROUP_ADMIN};
        List<ResearchGroup> list = getHibernateTemplate().findByNamedParam(hqlQuery, names, params);
        return list;
    }

    public List<ResearchGroup> getRecordsNewerThan(long oracleScn) {
        String hqlQuery = "from ResearchGroup r where r.scn > :oracleScn";
        List<ResearchGroup> list = getHibernateTemplate().findByNamedParam(hqlQuery, "oracleScn", oracleScn);
        return list;
    }

    public boolean canSaveTitle(String title, int id) {
        String hqlQuery = "from ResearchGroup g where g.title = :title and g.researchGroupId != :id";
        String[] names = {"title", "id"};
        Object[] values = {title, id};
        List<ResearchGroup> list = getHibernateTemplate().findByNamedParam(hqlQuery, names, values);
        return (list.size() == 0);
    }
}
