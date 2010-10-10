package cz.zcu.kiv.eegdatabase.logic.controller;

import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;

/**
 *
 * @author JiPER
 */
public class PersonListController extends AbstractController {

    private PersonDao personDao;

    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView mav = new ModelAndView("people/list");

        List<Person> list = personDao.getAllRecords();

//        Session session = HibernateUtil.getSessionFactory().openSession();
//        Transaction transaction = session.beginTransaction();
//        try {
//            List<Person> list = session.createQuery("FROM pojo.Person AS person ORDER BY person.surname, person.givenname").list();
//
//            mav.addObject("personList", list);
//
//            session.flush();
//            transaction.commit();
//        } catch (Exception e) {
//            transaction.rollback();
//        } finally {
//            session.close();
//        }

        mav.addObject("personList", list);
        return mav;
    }

    public PersonDao getPersonDao() {
        return personDao;
    }

    public void setPersonDao(PersonDao personDao) {
        this.personDao = personDao;
    }
}
