package cz.zcu.kiv.eegdatabase.webservices.rest.groups;

import cz.zcu.kiv.eegdatabase.webservices.rest.groups.wrappers.ResearchGroupData;

import java.util.List;

/**
 * Research group service interface.
 *
 * @author Petr Miko
 */
public interface ResearchGroupService {

    /**
     * Getter of all research groups.
     *
     * @return list of research groups
     */
    public List<ResearchGroupData> getAllGroups();

    /**
     * Getter of user's research groups.
     *
     * @return list of research groups
     */
    public List<ResearchGroupData> getMyGroups();
}
