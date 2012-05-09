package cz.zcu.kiv.eegdatabase.webservices.client;

import cz.zcu.kiv.eegdatabase.webservices.dataDownload.wrappers.PersonInfo;

import javax.jws.WebService;
import java.util.List;

/**
 * @author František Liška
 */
@WebService
public interface ClientService {
    boolean isServiceAvailable();
    public List<PersonInfo> getAllPeople();
}
