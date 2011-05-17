package cz.zcu.kiv.eegdatabase.data.service;

import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroupMembership;

/**
 * Rozhrani pro pristup k perzistentnim datum
 * 
 * @author Jindra
 */
public interface DataService {

  public ResearchGroup createResearchGroup(String title, String description, String ownerUsername);

  public String getResearchGroupTitle(int id);

  public ResearchGroupMembership addMemberToResearchGroup(String userName, int groupId, String role);

  public boolean usernameExists(String userName);

  public boolean userNameInGroup(String userName, int groupId);
}
