package cz.zcu.kiv.eegdatabase.logic.controller.social;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.web.context.request.NativeWebRequest;

import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;

/**
 * Signs the user in by setting spring security authentication.
 * Uses SocialAuthenticationToken to sign in.
 *
 * @author Michal Patočka
 * @see SocialAuthenticationToken
 */
public class SpringSecuritySignInAdapter implements SignInAdapter {
    
    private final RequestCache requestCache;
    private @Inject
    PersonDao personDao;
    
    @Inject
    public SpringSecuritySignInAdapter(RequestCache requestCache) {
        this.requestCache = requestCache;
    }
    
    @Override
    public String signIn(String localUserId, Connection<?> connection, NativeWebRequest request) {
        
        Person person = personDao.getPerson(localUserId);
        
        GrantedAuthority grantedAuthority = new GrantedAuthorityImpl(person.getAuthority());
        List<GrantedAuthority> grantedAuthorities = Collections.singletonList(grantedAuthority);
        Authentication a = new SocialAuthenticationToken(grantedAuthorities, person);
        a.setAuthenticated(true);
        SecurityContextHolder.getContext().setAuthentication(a);

        //returns null - providerSignInController will redirect to its postSigninUrl
        return null;
    }
    
  
}
