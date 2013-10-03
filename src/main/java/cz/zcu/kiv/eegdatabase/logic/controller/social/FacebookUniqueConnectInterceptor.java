/*******************************************************************************
 * This file is part of the EEG-database project
 * 
 *   ==========================================
 *  
 *   Copyright (C) 2013 by University of West Bohemia (http://www.zcu.cz/en/)
 *  
 *  ***********************************************************************************************************************
 *  
 *   Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 *   the License. You may obtain a copy of the License at
 *  
 *       http://www.apache.org/licenses/LICENSE-2.0
 *  
 *   Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 *   an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 *   specific language governing permissions and limitations under the License.
 *  
 *  ***********************************************************************************************************************
 *  
 *   FacebookUniqueConnectInterceptor.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
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
