package cz.zcu.kiv.eegdatabase.logic.controller.social;

import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: Jiri Vlasimsky (vlasimsky.jiri@gmail.com)
 * Date: 12.4.11
 * Time: 19:00
 */
public class SocialAuthenticationToken extends AbstractAuthenticationToken {
    private Person person;

    public SocialAuthenticationToken(Collection<GrantedAuthority> authorities, Person person) {
        super(authorities);
        this.person = person;
    }

    @Override
    public Object getCredentials() {
        return person;
    }

    @Override
    public Object getPrincipal() {
        return this.person;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}
