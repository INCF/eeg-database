package cz.zcu.kiv.eegdatabase.wui.core.group;

import cz.zcu.kiv.eegdatabase.data.pojo.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.mail.MailException;

import java.util.List;

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
    public List<ResearchGroup> readByParameter(String parameterName, Object parameterValue) {
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

    @Override
    public List<ResearchGroup> getUnique(ResearchGroup example) {
        return service.getUnique(example);
    }

    @Override
    public ResearchGroupMembershipId createMemberhip(ResearchGroupMembership newInstance) {
        return service.createMemberhip(newInstance);
    }

    @Override
    public ResearchGroupMembership readMemberhip(ResearchGroupMembershipId id) {
        return service.readMemberhip(id);
    }

    @Override
    public List<ResearchGroupMembership> readMemberhipByParameter(String parameterName, int parameterValue) {
        return service.readMemberhipByParameter(parameterName, parameterValue);
    }

    @Override
    public List<ResearchGroupMembership> readMemberhipByParameter(String parameterName, String parameterValue) {
        return service.readMemberhipByParameter(parameterName, parameterValue);
    }

    @Override
    public void updateMemberhip(ResearchGroupMembership transientObject) {
        service.updateMemberhip(transientObject);
    }

    @Override
    public void deleteMemberhip(ResearchGroupMembership persistentObject) {
        service.deleteMemberhip(persistentObject);
    }

    @Override
    public List<ResearchGroupMembership> getAllMemberhipRecords() {
        return service.getAllMemberhipRecords();
    }

    @Override
    public List<ResearchGroupMembership> getMemberhipRecordsAtSides(int first, int max) {
        return service.getMemberhipRecordsAtSides(first, max);
    }

    @Override
    public int getCountMemberhipRecords() {
        return service.getCountMemberhipRecords();
    }

    @Override
    public Integer createPermissionRequest(GroupPermissionRequest newInstance) throws MailException {
        return service.createPermissionRequest(newInstance);
    }

    @Override
    public GroupPermissionRequest readPermissionRequest(int id) {
        return service.readPermissionRequest(id);
    }

    @Override
    public List<GroupPermissionRequest> readPermissionRequestByParameter(String parameterName, int parameterValue) {
        return service.readPermissionRequestByParameter(parameterName, parameterValue);
    }

    @Override
    public List<GroupPermissionRequest> readPermissionRequestByParameter(String parameterName, String parameterValue) {
        return service.readPermissionRequestByParameter(parameterName, parameterValue);
    }

    @Override
    public void updatePermissionRequest(GroupPermissionRequest transientObject) {
        service.updatePermissionRequest(transientObject);
    }

    @Override
    public void deletePermissionRequest(GroupPermissionRequest persistentObject) {
        service.deletePermissionRequest(persistentObject);
    }

    @Override
    public List<GroupPermissionRequest> getAllPermissionRequestRecords() {
        return service.getAllPermissionRequestRecords();
    }

    @Override
    public List<GroupPermissionRequest> getPermissionRequestRecordsAtSides(int first, int max) {
        return service.getPermissionRequestRecordsAtSides(first, max);
    }

    @Override
    public int getCountPermissionRequestRecords() {
        return service.getCountPermissionRequestRecords();
    }

    @Override
    public void grantGroupPermisson(GroupPermissionRequest request) {
        service.grantGroupPermisson(request);
    }

    @Override
    public boolean existsGroup(String title){
        List<ResearchGroup> groups = service.readByParameter("title", title);
        return groups != null && groups.size() > 0;
    }

    @Override
    public ResearchGroup getGroupByTitle(String title) {
        List<ResearchGroup> groups = service.readByParameter("title", title);
        if(groups.size() > 0){
            return groups.get(0);
        } else {
            return null;
        }
    }

	@Override
    public void markAsPaid(ResearchGroup user) {
        service.changeStatus(user, true);
    }

    @Override
    public void markAsUnpaid(ResearchGroup user) {
        service.changeStatus(user, true);
    }

}
