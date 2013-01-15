package cz.zcu.kiv.eegdatabase.logic.controller.social;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.web.ConnectInterceptor;
import org.springframework.social.linkedin.api.LinkedIn;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.request.WebRequest;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: stebjan
 * Date: 15.1.13
 * Time: 14:06
 * To change this template use File | Settings | File Templates.
 */
public class LinkedInUniqueConnectInterceptor implements ConnectInterceptor<LinkedIn> {

    @Autowired
    private UsersConnectionRepository usersConnectionRepository;

    @Autowired
    private ConnectionRepository connectionRepository;

    @Override
    public void preConnect(ConnectionFactory<LinkedIn> linkedInConnectionFactory, MultiValueMap<String, String> parameters, WebRequest request) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void postConnect(Connection<LinkedIn> connection, WebRequest request) {
        ConnectionRepository repo;
                List<String> users = usersConnectionRepository
                       .findUserIdsWithConnection(connection);

               if (users.size() > 1) {
                   for (String user: users) {
                       repo = usersConnectionRepository.createConnectionRepository(user);
                       repo.removeConnections("linkedIn");
                         }
                 connectionRepository.addConnection(connection);
               }
    }
}
