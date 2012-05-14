package cz.zcu.kiv.eegdatabase.webservices.client;

import cz.zcu.kiv.eegdatabase.webservices.client.wrappers.HardwareInfo;
import cz.zcu.kiv.eegdatabase.webservices.client.wrappers.PersonInfo;
import cz.zcu.kiv.eegdatabase.webservices.client.wrappers.ResearchGroupInfo;

import javax.jws.WebService;
import java.util.List;

/**
 * @author František Liška
 */
@WebService
public interface ClientService {
    boolean isServiceAvailable();
    public List<PersonInfo> getAllPeople();
    public List<ResearchGroupInfo> getMyResearchGroups();
}
