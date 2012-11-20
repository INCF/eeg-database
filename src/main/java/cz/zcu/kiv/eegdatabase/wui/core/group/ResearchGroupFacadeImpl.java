package cz.zcu.kiv.eegdatabase.wui.core.group;

import java.util.List;

import org.springframework.beans.factory.annotation.Required;

import cz.zcu.kiv.eegdatabase.wui.core.dto.FullPersonDTO;

public class ResearchGroupFacadeImpl implements ResearchGroupFacade {

    ResearchGroupService service;

    @Required
    public void setService(ResearchGroupService service) {
        this.service = service;
    }

    @Override
    public List<ResearchGroupDTO> getResearchGroupsWhereMember(FullPersonDTO person) {
        return service.getResearchGroupsWhereAbleToWriteInto(person);
    }

    @Override
    public List<ResearchGroupDTO> getResearchGroupsWhereMember(FullPersonDTO person, int limit) {
        return service.getResearchGroupsWhereMember(person, limit);
    }

    @Override
    public List<ResearchGroupDTO> getResearchGroupsWhereOwner(FullPersonDTO person) {
        return service.getResearchGroupsWhereOwner(person);
    }

    @Override
    public List<ResearchGroupDTO> getResearchGroupsWhereAbleToWriteInto(FullPersonDTO person) {
        return service.getResearchGroupsWhereAbleToWriteInto(person);
    }

    @Override
    public String getResearchGroupTitle(int groupId) {
        return service.getResearchGroupTitle(groupId);
    }

    @Override
    public List<ResearchGroupAccountInfo> getGroupDataForAccountOverview(FullPersonDTO person) {
        return service.getGroupDataForAccountOverview(person);
    }

    @Override
    public List getListOfGroupMembers(int groupId) {
        return service.getListOfGroupMembers(groupId);
    }

    @Override
    public List<ResearchGroupDTO> getResearchGroupsWhereUserIsGroupAdmin(FullPersonDTO person) {
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
    public void create(ResearchGroupDTO user) {
        service.create(user);
    }

    @Override
    public void delete(ResearchGroupDTO user) {
        service.delete(user);
    }

    @Override
    public void update(ResearchGroupDTO user) {
        service.update(user);
    }

    @Override
    public ResearchGroupDTO getResearchGroupById(int id) {
        return service.getResearchGroupById(id);
    }

}
