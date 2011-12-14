package cz.zcu.kiv.eegdatabase.data.service;

import cz.zcu.kiv.eegdatabase.data.dao.GenericDao;
import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.dao.ResearchGroupDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroupMembership;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroupMembershipId;
import cz.zcu.kiv.eegdatabase.logic.Util;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * Implementace DataService pouzivajici pro ukladani dat do databaze Hibernate.
 *
 * @author Jindra
 */
public class HibernateDatabaseService implements DataService {

  private Log log = LogFactory.getLog(getClass());
  @Autowired
  private PersonDao personDao;
  @Autowired
  @Qualifier("researchGroupMembershipDao")
  private GenericDao<ResearchGroupMembership, ResearchGroupMembershipId> reseachGroupMembershipDao;
  @Autowired
  private ResearchGroupDao researchGroupDao;
  @Autowired
  @Qualifier("researchGroupMembershipDao")
  private GenericDao<ResearchGroupMembership, ResearchGroupMembershipId> researchGroupMembershipDao;

  /**
   * Vytvori novou vyzkumnou skupinu.
   *
   * @param title Nazev skupiny
   * @param description Popis skupiny
   * @param ownerUsername Uzivatel, ktery bude jejim vlastnikem a zaroven spravcem
   * @return Nove vytvorena skupina
   */
  public ResearchGroup createResearchGroup(String title, String description, String ownerUsername) {
    log.debug("Creating new POJO object ResearchGroup.");
    ResearchGroup group = new ResearchGroup();

    log.debug("Setting group title = " + title);
    group.setTitle(title);

    log.debug("Setting group description = " + description);
    group.setDescription(description);

    log.debug("Setting group owner = " + ownerUsername);
    Person owner = personDao.getPerson(ownerUsername);
    group.setPerson(owner);

    log.debug("Saving new ResearchGroup object.");
    researchGroupDao.create(group);

    log.debug("Assigning user to the group as member with level GROUP_ADMIN.");
    ResearchGroupMembership membership = new ResearchGroupMembership();
    membership.setAuthority(Util.GROUP_ADMIN);
    membership.setId(new ResearchGroupMembershipId(owner.getPersonId(), group.getResearchGroupId()));
    log.debug("Saving membership relation.");
    reseachGroupMembershipDao.create(membership);

    log.debug("Creating of research group done.");

    return group;
  }

  public String getResearchGroupTitle(int id) {
    return researchGroupDao.read(id).getTitle();
  }

  public ResearchGroupMembership addMemberToResearchGroup(String userName, int groupId, String role) {
    log.debug("Getting user id.");
    int personId = personDao.getPerson(userName).getPersonId();

    log.debug("Creating new ResearchGroupMembership object.");
    ResearchGroupMembership membership = new ResearchGroupMembership();

    log.debug("Setting id to ResearchGroupMembership.");
    membership.setId(new ResearchGroupMembershipId(personId, groupId));

    log.debug("Setting authority to ResearchGroupMembership.");
    membership.setAuthority(role);

    log.debug("Saving new membership.");
    researchGroupMembershipDao.create(membership);

    log.debug("Returning new ResearchGroupMembership object.");
    return membership;
  }



  public boolean userNameInGroup(String userName, int groupId) {
    return personDao.userNameInGroup(userName, groupId);
  }

  public boolean usernameExists(String userName) {
    return personDao.usernameExists(userName);
  }
}
