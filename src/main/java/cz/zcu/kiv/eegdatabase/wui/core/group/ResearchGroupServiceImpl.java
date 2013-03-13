package cz.zcu.kiv.eegdatabase.wui.core.group;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import cz.zcu.kiv.eegdatabase.data.dao.GenericDao;
import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.dao.ResearchGroupDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroupMembership;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroupMembershipId;

public class ResearchGroupServiceImpl implements ResearchGroupService {

    protected Log log = LogFactory.getLog(getClass());

    ResearchGroupDao researchGroupDAO;
    PersonDao personDAO;
    GenericDao<ResearchGroupMembership, ResearchGroupMembershipId> membershipDao;

    @Required
    public void setResearchGroupDAO(ResearchGroupDao researchGroupDAO) {
        this.researchGroupDAO = researchGroupDAO;
    }

    @Required
    public void setPersonDAO(PersonDao personDAO) {
        this.personDAO = personDAO;
    }

    public void setMembershipDao(GenericDao<ResearchGroupMembership, ResearchGroupMembershipId> membershipDao) {
        this.membershipDao = membershipDao;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResearchGroup> getResearchGroupsWhereMember(Person person) {

        return researchGroupDAO.getResearchGroupsWhereMember(person);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResearchGroup> getResearchGroupsWhereMember(Person person, int limit) {

        return researchGroupDAO.getResearchGroupsWhereMember(person, limit);
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
    public List<ResearchGroup> getGroupsForList(int start, int limit) {
        return researchGroupDAO.getGroupsForList(start, limit);
    }

    @Override
    @Transactional(readOnly = true)
    public List getListOfGroupMembers(int groupId) {
        return researchGroupDAO.getListOfGroupMembers(groupId);
    }

    @Override
    @Transactional
    public Integer create(ResearchGroup group) {
        return researchGroupDAO.create(group);
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

    @Override
    @Transactional(readOnly = true)
    public ResearchGroup read(Integer id) {
        return researchGroupDAO.read(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResearchGroup> readByParameter(String parameterName, int parameterValue) {
        return researchGroupDAO.readByParameter(parameterName, parameterValue);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResearchGroup> readByParameter(String parameterName, String parameterValue) {
        return researchGroupDAO.readByParameter(parameterName, parameterValue);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResearchGroup> getAllRecords() {
        return researchGroupDAO.getAllRecords();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResearchGroup> getRecordsAtSides(int first, int max) {
        return researchGroupDAO.getRecordsAtSides(first, max);
    }

    @Override
    @Transactional(readOnly = true)
    public int getCountRecords() {
        return researchGroupDAO.getCountRecords();
    }

    @Override
    @Transactional
    public ResearchGroupMembershipId createMemberhip(ResearchGroupMembership newInstance) {
        return membershipDao.create(newInstance);
    }

    @Override
    @Transactional(readOnly = true)
    public ResearchGroupMembership readMemberhip(ResearchGroupMembershipId id) {
        return membershipDao.read(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResearchGroupMembership> readMemberhipByParameter(String parameterName, int parameterValue) {
        return membershipDao.readByParameter(parameterName, parameterValue);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResearchGroupMembership> readMemberhipByParameter(String parameterName, String parameterValue) {
        return membershipDao.readByParameter(parameterName, parameterValue);
    }

    @Override
    @Transactional
    public void updateMemberhip(ResearchGroupMembership transientObject) {
        membershipDao.update(transientObject);
    }

    @Override
    @Transactional
    public void deleteMemberhip(ResearchGroupMembership persistentObject) {
        membershipDao.delete(persistentObject);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResearchGroupMembership> getAllMemberhipRecords() {
        return membershipDao.getAllRecords();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResearchGroupMembership> getMemberhipRecordsAtSides(int first, int max) {
        return membershipDao.getRecordsAtSides(first, max);
    }

    @Override
    @Transactional(readOnly = true)
    public int getCountMemberhipRecords() {
        return membershipDao.getCountRecords();
    }

}
