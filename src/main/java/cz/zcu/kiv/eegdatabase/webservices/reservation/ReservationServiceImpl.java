package cz.zcu.kiv.eegdatabase.webservices.reservation;

import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.dao.ResearchGroupDao;
import cz.zcu.kiv.eegdatabase.data.dao.ReservationDao;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.data.pojo.Reservation;
import cz.zcu.kiv.eegdatabase.webservices.reservation.wrappers.ReservationData;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.jws.WebService;
import javax.ws.rs.core.Response;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Controller for mapping REST requests upon reservation system.
 *
 * @author Petr Miko
 */
@WebService(endpointInterface = "cz.zcu.kiv.eegdatabase.webservices.reservation.ReservationService")
@SuppressWarnings("unchecked")
public class ReservationServiceImpl implements ReservationService {

    private final static Log log = LogFactory.getLog(ReservationServiceImpl.class);

    @Autowired
    @Qualifier("reservationDao")
    private ReservationDao reservationDao;

    @Autowired
    @Qualifier("researchGroupDao")
    private ResearchGroupDao researchGroupDao;

    @Autowired
    @Qualifier("personDao")
    private PersonDao personDao;

    private final SimpleDateFormat sf = new SimpleDateFormat("dd-MM-yyyy");

    @Override
    public List<ReservationData> getToDate(String date) throws ReservationException {
        GregorianCalendar calendarFrom = new GregorianCalendar();
        GregorianCalendar calendarTo = new GregorianCalendar();
        try {
            log.debug(sf.parse(date));
            calendarFrom.setTime(sf.parse(date));
            calendarTo.setTime(sf.parse(date));
            calendarTo.add(Calendar.DAY_OF_MONTH, 1);
            List<Reservation> reservations = reservationDao.getReservationsBetween(calendarFrom, calendarTo);
            List<ReservationData> data = new ArrayList<ReservationData>(reservations.size());

            for (Reservation r : reservations) {
                data.add(new ReservationData(r.getReservationId(), r.getResearchGroup().getTitle(), r.getResearchGroup().getResearchGroupId(), r.getStartTime(), r.getEndTime()));
            }

            return data;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ReservationException(e);
        }
    }

    @Override
    public List<ReservationData> getFromToDate(String fromDate, String toDate) throws ReservationException {
        GregorianCalendar fromCalendar = new GregorianCalendar();
        GregorianCalendar toCalendar = new GregorianCalendar();
        try {
            fromCalendar.setTime(sf.parse(fromDate));
            toCalendar.setTime(sf.parse(toDate));
            List<Reservation> reservations = reservationDao.getReservationsBetween(fromCalendar, toCalendar);
            List<ReservationData> data = new ArrayList<ReservationData>(reservations.size());

            for (Reservation r : reservations) {
                data.add(new ReservationData(r.getReservationId(), r.getResearchGroup().getTitle(), r.getResearchGroup().getResearchGroupId(), r.getStartTime(), r.getEndTime()));
            }

            return data;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ReservationException(e);
        }
    }

    @Override
    public Response create(int groupId, String date, String fromHour, String toHour) throws ReservationException {
        try {
            ResearchGroup group = researchGroupDao.read(groupId);
            Reservation reservation = new Reservation();

            reservation.setResearchGroup(group);
            reservation.setPerson(personDao.getLoggedPerson());
            reservation.setStartTime(new Timestamp(sf.parse(fromHour).getTime()));
            reservation.setEndTime(new Timestamp(sf.parse(toHour).getTime()));
            reservation.setCreationTime(new Timestamp(Calendar.getInstance().getTimeInMillis()));

            reservationDao.createChecked(reservation);

            return Response.ok().build();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ReservationException(e);
        }
    }

    @Override
    public Response delete(int reservationId) throws ReservationException {
        try {
            reservationDao.delete(reservationDao.read(reservationId));
            return Response.ok(reservationId).build();
        } catch (Exception e) {
            log.error(e);
            throw new ReservationException(e);
        }
    }
}
