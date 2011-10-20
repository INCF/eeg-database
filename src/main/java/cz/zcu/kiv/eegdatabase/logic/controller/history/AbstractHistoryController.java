/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.logic.controller.history;

import cz.zcu.kiv.eegdatabase.data.dao.AuthorizationManager;
import cz.zcu.kiv.eegdatabase.data.dao.HistoryDao;
import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.dao.ResearchGroupDao;
import cz.zcu.kiv.eegdatabase.data.pojo.History;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.logic.controller.myaccount.ChangeDefaultGroupCommand;
import cz.zcu.kiv.eegdatabase.logic.util.ControllerUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import java.util.List;
import java.util.Map;

/**
 * Sets reference data for form view and prepared ModelAndView for action "on submit"(in section history)
 *
 * @author pbruha
 */
public abstract class AbstractHistoryController extends SimpleFormController {

    protected Log log = LogFactory.getLog(getClass());
    protected AuthorizationManager auth;
    protected PersonDao personDao;
    protected HistoryDao historyDao;
    protected ResearchGroupDao researchGroupDao;

    /**
     * Load reference data needed for displaying the form
     *
     * @param map         - map from section History
     * @param historyType - history type (DAILY, WEEKLY, MONTHLY)
     * @return map - reference data for view
     */
    protected Map setReferenceData(Map map, ChoiceHistory historyType) {
        int userId;
        Person user = null;
        String authority = null;
        String roleAdmin = "ROLE_ADMIN";
        boolean isGroupAdmin;
        userId = personDao.getLoggedPerson().getPersonId();
        user = personDao.getPerson(ControllerUtils.getLoggedUserName());
        authority = user.getAuthority();
        isGroupAdmin = auth.userIsGroupAdmin();
        map.put("isAdmin", authority.equals(roleAdmin));


        String countOfDownloadedFiles;
        List<History> historyList = null;
        List<History> lastDownloadedFilesHistoryList = null;
        List<DownloadStatistic> topDownloadedFilesList = null;
        List<ResearchGroup> researchGroupList =
                researchGroupDao.getResearchGroupsWhereUserIsGroupAdmin(personDao.getLoggedPerson());
        ResearchGroup defaultGroup = personDao.getLoggedPerson().getDefaultGroup();
        int defaultGroupId = (defaultGroup != null) ? defaultGroup.getResearchGroupId() : 0;

        if (authority.equals(roleAdmin) || isGroupAdmin) {
            if (authority.equals(roleAdmin)) {
                defaultGroupId = 0;
                isGroupAdmin = false;
            }
            historyList = historyDao.getHistory(historyType, isGroupAdmin, defaultGroupId);
            lastDownloadedFilesHistoryList = historyDao.getLastDownloadHistory(historyType, isGroupAdmin, defaultGroupId);
            topDownloadedFilesList = historyDao.getTopDownloadHistory(historyType, isGroupAdmin, defaultGroupId);

            countOfDownloadedFiles = "" + historyList.size();
            map.put("countOfDownloadedFiles", countOfDownloadedFiles);
            map.put("historyList", historyList);
            map.put("topDownloadedFilesList", topDownloadedFilesList);
            map.put("lastDownloadedFilesHistoryList", lastDownloadedFilesHistoryList);
            map.put("defaultGroupId", defaultGroupId);
            map.put("researchGroupList", researchGroupList);
            map.put("access", true);
            return map;
        }
        map.put("access", false);
        return map;
    }

    /**
     * Prepared ModelAndView for action "on submit" needed for displaying the form
     *
     * @param historyType               -  history type (DAILY, WEEKLY, MONTHLY)
     * @param changeDefaultGroupCommand - object for form's atributes
     * @param mav                       - ModelAndView instance from section History
     * @return mav - ModelAndView
     */
    protected ModelAndView onSubmit(ChoiceHistory historyType, ChangeDefaultGroupCommand changeDefaultGroupCommand, ModelAndView mav) {

        List<ResearchGroup> researchGroupList =
                researchGroupDao.getResearchGroupsWhereUserIsGroupAdmin(personDao.getLoggedPerson());

        int defaultGroupId = changeDefaultGroupCommand.getDefaultGroup();
        mav.addObject("defaultGroupId", defaultGroupId);
        mav.addObject("researchGroupList", researchGroupList);
        log.debug("Show selected history");

        String countOfDownloadedFiles;
        int userId;
        List<History> historyList = null;
        Person user = null;
        String authority = null;
        String roleAdmin = "ROLE_ADMIN";
        boolean isGroupAdmin;
        List<History> lastDownloadedFilesHistoryList = null;
        List<DownloadStatistic> topDownloadedFilesList = null;
        userId = personDao.getLoggedPerson().getPersonId();
        user = personDao.getPerson(ControllerUtils.getLoggedUserName());
        authority = user.getAuthority();
        isGroupAdmin = auth.userIsGroupAdmin();
        mav.addObject("changeDefaultGroup", changeDefaultGroupCommand);

        if ((authority.equals(roleAdmin) || isGroupAdmin)) {
            historyList = historyDao.getHistory(historyType, isGroupAdmin, changeDefaultGroupCommand.getDefaultGroup());
            lastDownloadedFilesHistoryList = historyDao.getLastDownloadHistory(historyType, isGroupAdmin, changeDefaultGroupCommand.getDefaultGroup());
            topDownloadedFilesList = historyDao.getTopDownloadHistory(historyType, isGroupAdmin, changeDefaultGroupCommand.getDefaultGroup());

            countOfDownloadedFiles = "" + historyList.size();
            mav.addObject("countOfDownloadedFiles", countOfDownloadedFiles);
            mav.addObject("historyList", historyList);
            mav.addObject("topDownloadedFilesList", topDownloadedFilesList);
            mav.addObject("lastDownloadedFilesHistoryList", lastDownloadedFilesHistoryList);
            mav.addObject("isAdmin", authority.equals(roleAdmin));
            mav.addObject("access", true);
            return mav;
        }
        mav.addObject("access", false);
        return mav;
    }

    public HistoryDao getHistoryDao() {
        return historyDao;
    }

    public void setHistoryDao(HistoryDao historyDao) {
        this.historyDao = historyDao;
    }

    public PersonDao getPersonDao() {
        return personDao;
    }

    public void setPersonDao(PersonDao personDao) {
        this.personDao = personDao;
    }

    public AuthorizationManager getAuth() {
        return auth;
    }

    public void setAuth(AuthorizationManager auth) {
        this.auth = auth;
    }

    public ResearchGroupDao getResearchGroupDao() {
        return researchGroupDao;
    }

    public void setResearchGroupDao(ResearchGroupDao researchGroupDao) {
        this.researchGroupDao = researchGroupDao;
    }
}
