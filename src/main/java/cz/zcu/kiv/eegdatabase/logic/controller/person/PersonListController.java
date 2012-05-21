package cz.zcu.kiv.eegdatabase.logic.controller.person;

import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.logic.util.Paginator;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author JiPER
 */
public class PersonListController extends AbstractController {

    private PersonDao personDao;

    private static final int ITEMS_PER_PAGE = 20;

    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView mav = new ModelAndView("people/list");

        Paginator paginator = new Paginator(personDao.getCountForList(), ITEMS_PER_PAGE);

        int page = 1;
        try {
            page = Integer.parseInt(request.getParameter("page"));
        } catch (Exception e) {
        }
        paginator.setActualPage(page);
        List<Person> list = personDao.getDataForList(paginator.getFirstItemIndex(), ITEMS_PER_PAGE);

        mav.addObject("personList", list);
        mav.addObject("paginator", paginator.getLinks());
        return mav;
    }

    public PersonDao getPersonDao() {
        return personDao;
    }

    public void setPersonDao(PersonDao personDao) {
        this.personDao = personDao;
    }
}
