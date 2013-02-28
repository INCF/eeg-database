package cz.zcu.kiv.eegdatabase.webservices.rest.groups;

import cz.zcu.kiv.eegdatabase.webservices.rest.groups.wrappers.ResearchGroupData;

import java.util.List;

/**
 * @author Petr Miko
 *         Date: 24.2.13
 */
public interface ResearchGroupService {

    public List<ResearchGroupData> getAllGroups();

    public List<ResearchGroupData> getMyGroups();
}
