package cz.zcu.kiv.eegdatabase.logic.controller.social;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.web.ConnectInterceptor;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.request.WebRequest;

import java.util.List;


public class FacebookUniqueConnectInterceptor implements ConnectInterceptor<Facebook> {

   @Autowired
   private UsersConnectionRepository usersConnectionRepository;

   @Autowired
   private ConnectionRepository connectionRepository;

    public FacebookUniqueConnectInterceptor() {
    }

    @Override
    public void preConnect(ConnectionFactory<Facebook> connectionFactory, MultiValueMap<String, String> parameters, WebRequest request) {
     //Nothing to do
    }

    @Override
    public void postConnect(Connection<Facebook> connection, WebRequest request) {
        ConnectionRepository repo;
        List<String> users = usersConnectionRepository
               .findUserIdsWithConnection(connection);

       if (users.size() > 1) {
           for (String user: users) {
               repo = usersConnectionRepository.createConnectionRepository(user);
               repo.removeConnections("facebook");
                 }
         connectionRepository.addConnection(connection);
       }
    }
}
