package cz.zcu.kiv.eegdatabase.wui.core.security;

import org.springframework.beans.factory.annotation.Required;

import cz.zcu.kiv.eegdatabase.data.dao.AuthorizationManager;

public class SecurityServiceImpl implements SecurityService {

    AuthorizationManager manager;

    @Required
    public void setManager(AuthorizationManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean personAbleToWriteIntoGroup(int researchGroupId) {
        return manager.personAbleToWriteIntoGroup(researchGroupId);
    }

    @Override
    public boolean userIsExperimenter() {
        return manager.userIsExperimenter();
    }

    @Override
    public boolean isAdmin() {
        return manager.isAdmin();
    }

    @Override
    public boolean userIsGroupAdmin() {
        return manager.userIsGroupAdmin();
    }

    @Override
    public boolean userIsOwnerOrCoexperimenter(int experimentId) {
        return manager.userIsOwnerOrCoexperimenter(experimentId);
    }

    @Override
    public boolean userCanViewPersonDetails(int personId) {
        return manager.userCanViewPersonDetails(personId);
    }

    @Override
    public boolean userIsOwnerOfScenario(int scenarioId) {
        return manager.userIsOwnerOfScenario(scenarioId);
    }

    @Override
    public boolean userIsOwnerOrCoexpOfCorrespExperiment(int fileId) {
        return manager.userIsOwnerOrCoexpOfCorrespExperiment(fileId);
    }

    @Override
    public boolean userIsExperimenterInGroup(int groupId) {
        return manager.userIsExperimenterInGroup(groupId);
    }

    @Override
    public boolean userIsAdminInGroup(int groupId) {
        return manager.userIsAdminInGroup(groupId);
    }

    @Override
    public boolean userIsMemberInGroup(int groupId) {
        return manager.userIsMemberInGroup(groupId);
    }

    @Override
    public boolean userCanEditPerson(int personId) {
        return manager.userCanEditPerson(personId);
    }

}
