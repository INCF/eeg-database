package cz.zcu.kiv.eegdatabase.wui.core.group;

import java.util.List;

import oracle.net.aso.s;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;

import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;

public class ResearchGroupFacadeImpl implements ResearchGroupFacade {

    protected Log log = LogFactory.getLog(getClass());

    ResearchGroupService service;

    @Required
    public void setService(ResearchGroupService service) {
        this.service = service;
    }

    @Override
    public List<ResearchGroup> getResearchGroupsWhereMember(Person person) {
        return service.getResearchGroupsWhereMember(person);
    }

    @Override
    public List<ResearchGroup> getResearchGroupsWhereMember(Person person, int limit) {
        return service.getResearchGroupsWhereMember(person, limit);
    }

    @Override
    public List<ResearchGroup> getResearchGroupsWhereOwner(Person person) {
        return service.getResearchGroupsWhereOwner(person);
    }

    @Override
    public List<ResearchGroup> getResearchGroupsWhereAbleToWriteInto(Person person) {
        return service.getResearchGroupsWhereAbleToWriteInto(person);
    }

    @Override
    public String getResearchGroupTitle(int groupId) {
        return service.getResearchGroupTitle(groupId);
    }

    @Override
    public List<ResearchGroupAccountInfo> getGroupDataForAccountOverview(Person person) {
        return service.getGroupDataForAccountOverview(person);
    }

    @Override
    public List getListOfGroupMembers(int groupId) {
        return service.getListOfGroupMembers(groupId);
    }

    @Override
    public List<ResearchGroup> getResearchGroupsWhereUserIsGroupAdmin(Person person) {
        return service.getResearchGroupsWhereUserIsGroupAdmin(person);
    }

    @Override
    public boolean canSaveTitle(String title, int id) {
        return service.canSaveTitle(title, id);
    }

    @Override
    public int getCountForList() {
        return service.getCountForList();
    }

    @Override
    public List getGroupsForList(int start, int limit) {
        return service.getGroupsForList(start, limit);
    }

    @Override
    public Integer create(ResearchGroup user) {
        return service.create(user);
    }

    @Override
    public void delete(ResearchGroup user) {
        service.delete(user);
    }

    @Override
    public void update(ResearchGroup user) {
        service.update(user);
    }

    @Override
    public ResearchGroup getResearchGroupById(int id) {
        return service.getResearchGroupById(id);
    }

    @Override
    public ResearchGroup read(Integer id) {
        return service.read(id);
    }

    @Override
    public List<ResearchGroup> readByParameter(String parameterName, int parameterValue) {
        return service.readByParameter(parameterName, parameterValue);
    }

    @Override
    public List<ResearchGroup> readByParameter(String parameterName, String parameterValue) {
        return service.readByParameter(parameterName, parameterValue);
    }

    @Override
    public List<ResearchGroup> getAllRecords() {
        return service.getAllRecords();
    }

    @Override
    public List<ResearchGroup> getRecordsAtSides(int first, int max) {
        return service.getRecordsAtSides(first, max);
    }

    @Override
    public int getCountRecords() {
        return service.getCountRecords();
    }

}
