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
 *   SocialConnectionSignUp.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.logic.controller.social;

import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.wui.core.person.PersonService;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UserProfile;
import org.springframework.social.linkedin.api.LinkedIn;
import org.springframework.social.linkedin.api.LinkedInProfileFull;

import javax.inject.Inject;

/**
 * Class for signing in in via social networks. Invoked when no such
 * user id is found.
 * @author Michal Patočka 
 * 
 */
public final class SocialConnectionSignUp implements ConnectionSignUp {

    private PersonService personService;
    private @Inject LinkedIn linkedin;
   

    public SocialConnectionSignUp(PersonService personService) {
        this.personService = personService;
    }

    @Override
    public String execute(Connection<?> connection) {
        UserProfile profile = connection.fetchUserProfile();
        String email = profile.getEmail();

        if (email == null) {
           LinkedIn ln = (LinkedIn) connection.getApi();
           LinkedInProfileFull full = ln.profileOperations().getUserProfileFull();
           System.out.println("xxxx " + full.getId());
           linkedin.profileOperations().getUserProfile().getId();

        }


        SocialUser user = new SocialUser(profile.getEmail(),
                profile.getFirstName(), profile.getLastName());
      
        Person person = personService.createPerson(user, null);

        return person.getUsername();

    }
}
