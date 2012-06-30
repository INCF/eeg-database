package cz.zcu.kiv.eegdatabase.webservices.reservation;

import cz.zcu.kiv.eegdatabase.data.dao.ReservationDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Reservation;
import cz.zcu.kiv.eegdatabase.webservices.reservation.wrappers.ReservationData;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.jws.WebService;
import java.util.ArrayList;
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

    @Override
    public List<ReservationData> getAll() {
        List<ReservationData> data = new ArrayList<ReservationData>();

        for (Reservation r : reservationDao.getAllRecords()) {
            ReservationData rd = new ReservationData(r.getResearchGroup().getTitle(), r.getStartTime(), r.getEndTime());
            data.add(rd);
        }
        return data;
    }
}
