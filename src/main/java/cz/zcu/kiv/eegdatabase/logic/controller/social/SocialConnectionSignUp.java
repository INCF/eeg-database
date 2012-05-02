package cz.zcu.kiv.eegdatabase.logic.controller.social;

import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.service.HibernatePersonService;
import cz.zcu.kiv.eegdatabase.data.service.PersonService;
import cz.zcu.kiv.eegdatabase.logic.controller.root.RegistrationCommand;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import javax.inject.Inject;
import org.eclipse.jetty.server.Authentication.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UserProfile;
import org.springframework.social.linkedin.api.LinkedIn;

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

        SocialUser user = new SocialUser(profile.getEmail(),
                profile.getFirstName(), profile.getLastName());
        
      
        Person person = personService.createPerson(user, null);
        
      

       

        return person.getUsername();

    }
}