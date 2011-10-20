package cz.zcu.kiv.eegdatabase.mock;

import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.data.pojo.Reservation;
import cz.zcu.kiv.eegdatabase.data.DaoCollection;

import java.sql.Timestamp;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created: 5.5.11
 *
 * @author Jenda Kolena, jendakolena@gmail.com
 * @version 0.1
 */
public abstract class MockFactory
{
    private static DaoCollection daoCollection;
    private static Person person;
    private static Reservation reservation;
    private static ResearchGroup researchGroup;

    public static Reservation createReservation(GregorianCalendar start, GregorianCalendar end)
    {
        if (reservation == null)
        {
            if (daoCollection == null) throw new RuntimeException("You must set the DaoCollection first!");
            reservation = new Reservation();
            reservation.setPerson(createPerson());
            reservation.setResearchGroup(createResearchGroup());
            reservation.setCreationTime(new Timestamp(new Date().getTime()));
            reservation.setStartTime(new Timestamp(start.getTimeInMillis()));
            reservation.setEndTime(new Timestamp(end.getTimeInMillis()));
            daoCollection.getReservationDao().create(reservation);
        }
        return reservation;
    }

    public static void freeReservation()
    {
        freeReservation(true);
    }

    public static void freeReservation(boolean deleteRelated)
    {
        if (reservation != null) daoCollection.getReservationDao().delete(reservation);
        reservation = null;
        if (deleteRelated) freeResearchGroup();
    }

    public static Person createPerson()
    {
        if (person == null)
        {
            if (daoCollection == null) throw new RuntimeException("You must set the DaoCollection first!");
            person = daoCollection.getPersonDao().getPerson("jenda");
        }

        return person;
    }

    public static void freePerson()
    {
        person = null;
    }

    public static ResearchGroup createResearchGroup()
    {
        if (researchGroup == null)
        {
            if (daoCollection == null) throw new RuntimeException("You must set the DaoCollection first!");
            researchGroup = new ResearchGroup(12345678, createPerson(), "test group", "test description");
            daoCollection.getResearchGroupDao().create(researchGroup);
        }
        return researchGroup;
    }

    public static void freeResearchGroup()
    {
        if (researchGroup != null) daoCollection.getResearchGroupDao().delete(researchGroup);
        researchGroup = null;
    }


    public static void freeAll()
    {
        freeReservation();
        freeResearchGroup();
        freePerson();
    }


    public static void setDaoCollection(DaoCollection daoCollection)
    {
        MockFactory.daoCollection = daoCollection;
    }
}
