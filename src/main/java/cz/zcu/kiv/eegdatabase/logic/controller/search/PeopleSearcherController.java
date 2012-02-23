/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.zcu.kiv.eegdatabase.logic.controller.search;

import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author pbruha
 */
public class PeopleSearcherController extends AbstractSearchController {

    private PersonDao personDao;

    public PeopleSearcherController() {
        setCommandClass(PeopleSearcherCommand.class);
        setCommandName("peopleSearcherCommand");
    }

    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        PeopleSearcherCommand search = (PeopleSearcherCommand) super.formBackingObject(request);
        return search;
    }


    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        logger.debug("Search people controller");
        ModelAndView mav = super.onSubmit(request, response, command);

        try {
            List<Person> personResults = personDao.getPersonSearchResults(requests);
            mav.addObject("personResults", personResults);
            mav.addObject("resultsEmpty", personResults.isEmpty());
        } catch (NumberFormatException e) {
            mav.addObject("mistake", "Number error");
            mav.addObject("error", true);
        } catch (RuntimeException e) {
            mav.addObject("mistake", e.getMessage());
            mav.addObject("error", true);
        }
        return mav;
    }



    public PersonDao getPersonDao() {
        return personDao;
    }

    public void setPersonDao(PersonDao personDao) {
        this.personDao = personDao;
    }




}
