/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.logic.controller.experiment;

import cz.zcu.kiv.eegdatabase.data.dao.AuthorizationManager;
import cz.zcu.kiv.eegdatabase.data.dao.GenericDao;
import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Jan
 */
public class ChooseMetadataController extends AbstractController {

    private GenericDao<Experiment, Integer> experimentDao;
    private PersonDao personDao;
    private AuthorizationManager auth;

    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView mav = new ModelAndView("experiments/chooseMetadata");
        int id = Integer.parseInt(request.getParameter("id"));
        Experiment m = experimentDao.read(id);
        Person user =  personDao.getLoggedPerson();
        boolean auth = false;
        if (user.getAuthority().equals("ROLE_ADMIN")) {
            auth = true;
        }
        if (m.getPersonByOwnerId().getPersonId() == user.getPersonId()) {
            auth = true;
        }
        if (personDao.userNameInGroup(user.getUsername(), m.getResearchGroup().getResearchGroupId())) {
            auth = true;
        }
        // For the menu item
        mav.addObject("userCanSeePersonDetail", auth);

        mav.addObject("measurationDetail", m);
        return mav;
    }

    public GenericDao<Experiment, Integer> getExperimentDao() {
        return experimentDao;
    }

    public void setExperimentDao(GenericDao<Experiment, Integer> experimentDao) {
        this.experimentDao = experimentDao;
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
