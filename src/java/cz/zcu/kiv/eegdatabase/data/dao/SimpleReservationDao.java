package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.Reservation;
import cz.zcu.kiv.eegdatabase.logic.util.ControllerUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Implementation of DAO for accessing Reservation entities. This class will use
 * only one specific couple of classes, so it doesn't need to be generic.
 *
 * @author Jan Kolena
 */
public class SimpleReservationDao
        extends SimpleGenericDao<Reservation, Integer>
        implements ReservationDao {

    public SimpleReservationDao() {
        super(Reservation.class);
    }

    public List<Reservation> getReservationsBetween(GregorianCalendar start, GregorianCalendar end, String startstr, String endstr) {
        /*String hqlQuery = "from ResearchGroup researchGroup "
            + "left join fetch researchGroup.researchGroupMemberships as membership "
            + "where membership.person.personId = :personId "
            + "order by researchGroup.title";*/

        /*String hqlQuery = "from Reservation reservation where reservation.start_Time > TIMESTAMP ':start' AND reservation.end_Time < TIMESTAMP ':end'";
        //where reservation.start_Time >= TIMESTAMP '2011-01-13 23:08:51' AND reservation.end_Time <= TIMESTAMP '2011-01-17 23:08:51'
        log.info("HQL="+hqlQuery);*/

        String hqlQuery = "from Reservation reservation where reservation.startTime > TIMESTAMP "+
                ":starttime AND reservation.endTime < TIMESTAMP :endtime";


        Session session = getSession();
        List result = session.createQuery(hqlQuery)
              .setString("starttime",startstr).setString("endtime",endstr).list();

        return result;

       /* Session ses = getSession();
      Query q = ses.createQuery(hqlQuery);



        q.setDate(":start",start.getTime());
        q.setDate(":end",end.getTime());
                                           */




      //return q.list();




   /*
getHibernateTemplate().findByNamedQuery(hqlQuery, new Object[] {'starttime': start, 'endtime': end});

        return getHibernateTemplate().findByNamedParam(hqlQuery,new String[]{"start","end"}, new Object[]{start, end});*/
    }

}
