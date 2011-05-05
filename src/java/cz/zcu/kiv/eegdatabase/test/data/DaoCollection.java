package cz.zcu.kiv.eegdatabase.test.data;

import cz.zcu.kiv.eegdatabase.data.dao.ArticleDao;
import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.dao.ResearchGroupDao;
import cz.zcu.kiv.eegdatabase.data.dao.ReservationDao;

/**
 * Created: 5.5.11
 *
 * @author Jenda Kolena, jendakolena@gmail.com
 * @version 0.1
 */
public class DaoCollection
{
    private PersonDao personDao;
    private ResearchGroupDao researchGroupDao;
    private ReservationDao reservationDao;
    private ArticleDao articleDao;

    public DaoCollection(PersonDao personDao, ResearchGroupDao researchGroupDao, ReservationDao reservationDao, ArticleDao articleDao)
    {
        this.personDao = personDao;
        this.researchGroupDao = researchGroupDao;
        this.reservationDao = reservationDao;
        this.articleDao = articleDao;
    }

    public PersonDao getPersonDao()
    {
        return personDao;
    }

    public ResearchGroupDao getResearchGroupDao()
    {
        return researchGroupDao;
    }

    public ReservationDao getReservationDao()
    {
        return reservationDao;
    }

    public ArticleDao getArticleDao()
    {
        return articleDao;
    }
}
