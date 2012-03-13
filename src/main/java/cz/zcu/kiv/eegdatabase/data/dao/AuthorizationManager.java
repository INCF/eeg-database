package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.Person;

/**
 * DAO for checking permissions of users.
 *
 * @author Jindra
 */
public interface AuthorizationManager {

    /**
     * Checks whether the logged user has permission to add experiment into
     * the research group defined by parameter.
     *
     * @param researchGroupId ID of the research group
     * @return <code>true</code> if user has permission, else <code>false</code>
     */
    public boolean personAbleToWriteIntoGroup(int researchGroupId);

    /**
     * Checks whether the logged user is member with GROUP_EXPERIMENTER or
     * GROUP_ADMIN role at least in one group.
     *
     * @return <code>true</code> when able to write data in some group, else
     *         <code>false</code>
     */
    public boolean userIsExperimenter();

    public boolean isAdmin();

    /**
     * Checks whether the logged user is member with
     * GROUP_ADMIN role at least in one group.
     *
     * @return <code>true</code> when able to write data in some group, else
     *         <code>false</code>
     */
    public boolean userIsGroupAdmin();

    /**
     * Checks whether the logged user is owner of or co-experimenter on experiment
     * specified by the experiment id.
     *
     * @param experimentId ID of experiment
     * @return
     */
    public boolean userIsOwnerOrCoexperimenter(int experimentId);

    /**
     * Checks whether the logged person is in the same group as the person specified
     * by person ID and whether the logged user is at least experimenter in that group.
     *
     * @param personId
     * @return
     */
    public boolean userCanViewPersonDetails(int personId);

    /**
     * Checks whether the logged person is owner of the scenario specified by scenarioId.
     *
     * @param scenarioId
     * @return
     */
    boolean userIsOwnerOfScenario(int scenarioId);

    public boolean userIsOwnerOrCoexpOfCorrespExperiment(int fileId);

    public boolean userIsExperimenterInGroup(int groupId);

    public boolean userIsAdminInGroup(int groupId);

    boolean userIsMemberInGroup(int groupId);

    boolean userCanEditPerson(int personId);
}
