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


public class EnsureUniqueConnectInterceptor implements ConnectInterceptor<Facebook> {

   @Autowired
   private UsersConnectionRepository usersConnectionRepository;

   @Autowired
   private ConnectionRepository connectionRepository;

    public EnsureUniqueConnectInterceptor() {
    }

    @Override
    public void preConnect(ConnectionFactory<Facebook> connectionFactory, MultiValueMap<String, String> parameters, WebRequest request) {

    }

    @Override
    public void postConnect(Connection<Facebook> connection, WebRequest request) {
        boolean connectionAlreadyAssociatedWithAnotherUser = usersConnectionRepository
       .findUserIdsWithConnection(connection).size() > 1;
        List<String> users = usersConnectionRepository
               .findUserIdsWithConnection(connection);

       if (connectionAlreadyAssociatedWithAnotherUser) {
         //connectionRepository.removeConnections("facebook");
           for (String user: users) {
                     System.out.println(user);
               connectionRepository.removeConnections("facebook");
                 }
          // connectionRepository.addConnection(connection);
//       RuntimeException nonUniqueConnectionException = new RuntimeException(
//       "The connection is already associated with a different account");
//       request.setAttribute("lastSessionException",
//       nonUniqueConnectionException, WebRequest.SCOPE_SESSION);
//       throw nonUniqueConnectionException;
       }
        System.out.println("postconnect");
    }
}
