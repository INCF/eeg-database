package cz.zcu.kiv.eegdatabase.webservices.client;

import javax.jws.WebService;

/**
 * @author František Liška
 */
@WebService(endpointInterface = "cz.zcu.kiv.eegdatabase.webservices.client.ClientService")
public class ClientServiceImpl implements ClientService{

    public String sayHi(String text) {
        System.out.println("sayHi called");
        return "Hello " + text;
    }
}
