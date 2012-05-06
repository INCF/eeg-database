package cz.zcu.kiv.eegdatabase.webservices.client;

import javax.jws.WebService;
/**
 * @author František Liška
 */
@WebService
public interface ClientService {
    String sayHi(String text);
}
