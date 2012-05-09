package cz.zcu.kiv.eegdatabase.webservices.client;

import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import javax.jws.WebService;

/**
 * @author František Liška
 */
@WebService(endpointInterface = "cz.zcu.kiv.eegdatabase.webservices.client.ClientService")
public class ClientServiceImpl implements ClientService{
    private PersonDao personDao;
    private static final Log log = LogFactory.getLog(ClientServiceImpl.class);

    public String sayHi(String text) {
        System.out.println("sayHi called");
        return "Hello " + text;
    }

    public boolean isServiceAvailable() {
            log.debug("User " + personDao.getLoggedPerson().getEmail() +
                        " verified connection with client web service.");
        return true;
    }

    public PersonDao getPersonDao() {
        return personDao;
    }

    public void setPersonDao(PersonDao personDao) {
        this.personDao = personDao;
    }
}
