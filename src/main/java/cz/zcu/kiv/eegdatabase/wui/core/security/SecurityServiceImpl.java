package cz.zcu.kiv.eegdatabase.wui.core.security;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import cz.zcu.kiv.eegdatabase.data.dao.AuthorizationManager;

public class SecurityServiceImpl implements SecurityService {
    
    protected Log log = LogFactory.getLog(getClass());

    AuthorizationManager manager;

    @Required
    public void setManager(AuthorizationManager manager) {
        this.manager = manager;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean personAbleToWriteIntoGroup(int researchGroupId) {
        return manager.personAbleToWriteIntoGroup(researchGroupId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean userIsExperimenter() {
        return manager.userIsExperimenter();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isAdmin() {
        return manager.isAdmin();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean userIsGroupAdmin() {
        return manager.userIsGroupAdmin();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean userIsOwnerOrCoexperimenter(int experimentId) {
        return manager.userIsOwnerOrCoexperimenter(experimentId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean userCanViewPersonDetails(int personId) {
        return manager.userCanViewPersonDetails(personId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean userIsOwnerOfScenario(int scenarioId) {
        return manager.userIsOwnerOfScenario(scenarioId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean userIsOwnerOrCoexpOfCorrespExperiment(int fileId) {
        return manager.userIsOwnerOrCoexpOfCorrespExperiment(fileId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean userIsExperimenterInGroup(int groupId) {
        return manager.userIsExperimenterInGroup(groupId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean userIsAdminInGroup(int groupId) {
        return manager.userIsAdminInGroup(groupId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean userIsMemberInGroup(int groupId) {
        return manager.userIsMemberInGroup(groupId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean userCanEditPerson(int personId) {
        return manager.userCanEditPerson(personId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isAuthorizedToRequestGroupRole() {
        return manager.isAuthorizedToRequestGroupRole();
    }

}
