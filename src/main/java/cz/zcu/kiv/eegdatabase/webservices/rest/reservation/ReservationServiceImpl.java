package cz.zcu.kiv.eegdatabase.webservices.rest.reservation;

import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.dao.ResearchGroupDao;
import cz.zcu.kiv.eegdatabase.data.dao.ReservationDao;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.data.pojo.Reservation;
import cz.zcu.kiv.eegdatabase.webservices.rest.common.exception.RestServiceException;
import cz.zcu.kiv.eegdatabase.webservices.rest.reservation.wrappers.ResearchGroupData;
import cz.zcu.kiv.eegdatabase.webservices.rest.reservation.wrappers.ReservationData;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.jws.WebService;
import javax.ws.rs.core.Response;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Controller for mapping REST requests upon reservation system.
 *
 * @author Petr Miko
 */
@WebService(endpointInterface = "ReservationService")
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
    public List<ReservationData> getToDate(String date) throws RestServiceException {
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
                data.add(new ReservationData(r.getReservationId(),
                                        r.getResearchGroup().getTitle(),
                                        r.getResearchGroup().getResearchGroupId(),
                                        r.getStartTime(),
                                        r.getEndTime(),
                                        r.getPerson().getGivenname() + " " + r.getPerson().getSurname(),
                                        r.getPerson().getEmail()));
            }

            return data;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RestServiceException(e);
        }
    }

    @Override
    public List<ReservationData> getFromToDate(String fromDate, String toDate) throws RestServiceException {
        GregorianCalendar fromCalendar = new GregorianCalendar();
        GregorianCalendar toCalendar = new GregorianCalendar();
        try {
            fromCalendar.setTime(sf.parse(fromDate));
            toCalendar.setTime(sf.parse(toDate));
            List<Reservation> reservations = reservationDao.getReservationsBetween(fromCalendar, toCalendar);
            List<ReservationData> data = new ArrayList<ReservationData>(reservations.size());

            for (Reservation r : reservations) {
                data.add(new ReservationData(r.getReservationId(),
                        r.getResearchGroup().getTitle(),
                        r.getResearchGroup().getResearchGroupId(),
                        r.getStartTime(),
                        r.getEndTime(),
                        r.getPerson().getGivenname() + " " + r.getPerson().getSurname(),
                        r.getPerson().getEmail()));
            }

            return data;
        } catch (Exception e) {
            log.error(String.format("From date: %s | To date: %s | Error: %s", fromDate, toDate, e.getMessage()), e);
            throw new RestServiceException(e);
        }
    }

    @Override
    public List<ResearchGroupData> getMyGroups() throws RestServiceException {

        try {
            Set<ResearchGroup> groups = personDao.getLoggedPerson().getResearchGroups();
            List<ResearchGroupData> data = new ArrayList<ResearchGroupData>(groups.size());

            for(ResearchGroup g : groups){
                ResearchGroupData d = new ResearchGroupData(g.getResearchGroupId(), g.getTitle());
                data.add(d);
            }

            return data;
        } catch (Exception e) {
            throw new RestServiceException(e);
        }
    }

    @Override
    public Response create(ReservationData reservationData) throws RestServiceException {
        try {
            ResearchGroup group = researchGroupDao.read(reservationData.getResearchGroupId());
            Reservation reservation = new Reservation();

            reservation.setResearchGroup(group);
            reservation.setPerson(personDao.getLoggedPerson());
            reservation.setStartTime(new Timestamp(reservationData.getFromTime().getTime()));
            reservation.setEndTime(new Timestamp(reservationData.getToTime().getTime()));
            reservation.setCreationTime(new Timestamp(Calendar.getInstance().getTimeInMillis()));

            int id = reservationDao.createChecked(reservation);
            return Response.ok(reservationData).build();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RestServiceException(e);
        }
    }

    @Override
    public Response delete(ReservationData data) throws RestServiceException {
        try {
            if(personDao.getLoggedPerson().getResearchGroups().contains(researchGroupDao.read(data.getResearchGroupId()))){
                reservationDao.delete(reservationDao.read(data.getReservationId()));
                return Response.ok().build();
            }
            else
                throw new Exception("You are not administrator of the group!");
        } catch (Exception e) {
            log.error(e);
            throw new RestServiceException(e);
        }
    }
}
