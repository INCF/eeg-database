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
 *   LinkedInUniqueConnectInterceptor.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
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
