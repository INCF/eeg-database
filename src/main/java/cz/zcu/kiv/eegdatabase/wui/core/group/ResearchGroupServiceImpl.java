package cz.zcu.kiv.eegdatabase.wui.core.group;

import java.util.List;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.dao.ResearchGroupDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;

public class ResearchGroupServiceImpl implements ResearchGroupService {

    ResearchGroupDao researchGroupDAO;
    PersonDao personDAO;

    @Required
    public void setResearchGroupDAO(ResearchGroupDao researchGroupDAO) {
        this.researchGroupDAO = researchGroupDAO;
    }

    @Required
    public void setPersonDAO(PersonDao personDAO) {
        this.personDAO = personDAO;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResearchGroup> getResearchGroupsWhereMember(Person person) {

        return researchGroupDAO.getResearchGroupsWhereMember(person);
    }

    @Override
    public List<ResearchGroup> getResearchGroupsWhereMember(Person person, int limit) {
        List<ResearchGroup> list = getResearchGroupsWhereMember(person);

        return list.subList(0, limit);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResearchGroup> getResearchGroupsWhereOwner(Person person) {

        return researchGroupDAO.getResearchGroupsWhereOwner(person);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResearchGroup> getResearchGroupsWhereAbleToWriteInto(Person person) {
        return researchGroupDAO.getResearchGroupsWhereAbleToWriteInto(person);
    }

    @Override
    @Transactional(readOnly = true)
    public String getResearchGroupTitle(int groupId) {
        return researchGroupDAO.getResearchGroupTitle(groupId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResearchGroupAccountInfo> getGroupDataForAccountOverview(Person person) {
        return researchGroupDAO.getGroupDataForAccountOverview(person);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResearchGroup> getResearchGroupsWhereUserIsGroupAdmin(Person person) {
        return researchGroupDAO.getResearchGroupsWhereUserIsGroupAdmin(person);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean canSaveTitle(String title, int id) {
        return researchGroupDAO.canSaveTitle(title, id);
    }

    @Override
    @Transactional(readOnly = true)
    public int getCountForList() {
        return researchGroupDAO.getCountForList();
    }

    @Override
    @Transactional(readOnly = true)
    public List getGroupsForList(int start, int limit) {
        return researchGroupDAO.getGroupsForList(start, limit);
    }

    @Override
    public List getListOfGroupMembers(int groupId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Transactional
    public void create(ResearchGroup group) {
        researchGroupDAO.create(group);
    }

    @Override
    @Transactional
    public void delete(ResearchGroup group) {
        researchGroupDAO.delete(researchGroupDAO.read(group.getResearchGroupId()));
    }

    @Override
    @Transactional
    public void update(ResearchGroup group) {
        researchGroupDAO.update(group);
    }

    @Override
    @Transactional(readOnly = true)
    public ResearchGroup getResearchGroupById(int id) {
        return researchGroupDAO.read(id);
    }
}
