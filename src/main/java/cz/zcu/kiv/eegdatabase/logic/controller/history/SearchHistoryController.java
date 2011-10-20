/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.logic.controller.history;

import cz.zcu.kiv.eegdatabase.data.dao.AuthorizationManager;
import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.dao.SimpleHistoryDao;
import cz.zcu.kiv.eegdatabase.data.pojo.History;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroupMembership;
import cz.zcu.kiv.eegdatabase.logic.controller.search.AbstractSearchController;
import cz.zcu.kiv.eegdatabase.logic.util.ControllerUtils;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Prepared search results
 * using history searcher command for saving searching scenario(by scenario title), date interval (from date of download, to date of download)
 *
 * @author pbruha
 */
public class SearchHistoryController extends AbstractSearchController {

    private AuthorizationManager auth;
    private PersonDao personDao;
    private SimpleHistoryDao<History, Integer> historyDao;

    public SearchHistoryController() {
        setCommandClass(HistorySearcherCommand.class);
        setCommandName("historySearcherCommand");
    }

    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        HistorySearcherCommand search = (HistorySearcherCommand) super.formBackingObject(request);
        return search;
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        log.debug("Processing advanced search download history");
        ModelAndView mav = super.onSubmit(request, response, command);
        Person user = null;
        String authority = null;
        String roleAdmin = "ROLE_ADMIN";
        boolean isGroupAdmin;

        user = personDao.getPerson(ControllerUtils.getLoggedUserName());
        authority = user.getAuthority();
        isGroupAdmin = auth.userIsGroupAdmin();
        if (authority.equals(roleAdmin) || isGroupAdmin) {
            if (authority.equals(roleAdmin)) {
                isGroupAdmin = false;
            }
            Set<ResearchGroupMembership> rgm = user.getResearchGroupMemberships();
            List<Integer> groupsId = new ArrayList<Integer>();

            for (ResearchGroupMembership member : rgm) {
                if (member.getAuthority().equals("GROUP_ADMIN")) {
                    groupsId.add(member.getResearchGroup().getResearchGroupId());
                }
            }


            try {
                List<History> historyResults = historyDao.getHistorySearchResults(requests, isGroupAdmin, groupsId);
                mav.addObject("historyResults", historyResults);
                mav.addObject("resultsEmpty", historyResults.isEmpty());
            } catch (NumberFormatException e) {
                mav.addObject("mistake", "Number error");
                mav.addObject("error", true);
            } catch (RuntimeException e) {
                mav.addObject("mistake", e.getMessage());
                mav.addObject("error", true);
            }
            return mav;
        }
        mav.setViewName("system/accessDeniedNotAdmin");
        return mav;
    }

    public SimpleHistoryDao<History, Integer> getHistoryDao() {
        return historyDao;
    }

    public void setHistoryDao(SimpleHistoryDao<History, Integer> historyDao) {
        this.historyDao = historyDao;
    }

    public AuthorizationManager getAuth() {
        return auth;
    }

    public void setAuth(AuthorizationManager auth) {
        this.auth = auth;
    }

    public PersonDao getPersonDao() {
        return personDao;
    }

    public void setPersonDao(PersonDao personDao) {
        this.personDao = personDao;
    }
}
