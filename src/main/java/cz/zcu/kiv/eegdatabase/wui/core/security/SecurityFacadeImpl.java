package cz.zcu.kiv.eegdatabase.wui.core.security;

import org.springframework.beans.factory.annotation.Required;

import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;

public class SecurityFacadeImpl implements SecurityFacade {

    SecurityService service;

    @Required
    public void setService(SecurityService service) {
        this.service = service;
    }

    @Override
    public boolean authorization(String userName, String password) {
        return EEGDataBaseSession.get().signIn(userName, password);
    }

    @Override
    public void logout() {
        EEGDataBaseSession.get().invalidate();
    }

    @Override
    public boolean personAbleToWriteIntoGroup(int researchGroupId) {
        return service.personAbleToWriteIntoGroup(researchGroupId);
    }

    @Override
    public boolean userIsExperimenter() {
        return service.userIsExperimenter();
    }

    @Override
    public boolean isAdmin() {
        return service.isAdmin();
    }

    @Override
    public boolean userIsGroupAdmin() {
        return service.userIsGroupAdmin();
    }

    @Override
    public boolean userIsOwnerOrCoexperimenter(int experimentId) {
        return service.userIsOwnerOrCoexperimenter(experimentId);
    }

    @Override
    public boolean userCanViewPersonDetails(int personId) {
        return service.userCanViewPersonDetails(personId);
    }

    @Override
    public boolean userIsOwnerOfScenario(int scenarioId) {
        return service.userIsOwnerOfScenario(scenarioId);
    }

    @Override
    public boolean userIsOwnerOrCoexpOfCorrespExperiment(int fileId) {
        return service.userIsOwnerOrCoexpOfCorrespExperiment(fileId);
    }

    @Override
    public boolean userIsExperimenterInGroup(int groupId) {
        return service.userIsExperimenterInGroup(groupId);
    }

    @Override
    public boolean userIsAdminInGroup(int groupId) {
        return service.userIsAdminInGroup(groupId);
    }

    @Override
    public boolean userIsMemberInGroup(int groupId) {
        return service.userIsMemberInGroup(groupId);
    }

    @Override
    public boolean userCanEditPerson(int personId) {
        return service.userCanEditPerson(personId);
    }

    @Override
    public boolean isAuthorizedToRequestGroupRole() {
        return service.isAuthorizedToRequestGroupRole();
    }

}
