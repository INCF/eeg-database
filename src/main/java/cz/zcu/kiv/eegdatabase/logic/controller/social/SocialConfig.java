package cz.zcu.kiv.eegdatabase.logic.controller.social;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.NotConnectedException;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.linkedin.api.LinkedIn;

import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.service.PersonService;

/**
 * Configuration of beans for spring social implementation.
 *
 * @author Michal Patočka
 */
@Configuration
public class SocialConfig {

    private @Inject
    DriverManagerDataSource dataSource;
    private @Inject
    ConnectionFactoryLocator connectionFactoryLocator;
    private @Inject
    TextEncryptor textEncryptor;
    @Autowired
    private PersonService personService;

    @Bean
    @Scope(value = "singleton", proxyMode = ScopedProxyMode.INTERFACES)
    public UsersConnectionRepository usersConnectionRepository() {
        JdbcUsersConnectionRepository repository = new JdbcUsersConnectionRepository(
                dataSource, connectionFactoryLocator, textEncryptor);
        repository.setConnectionSignUp(new SocialConnectionSignUp(personService));
        return repository;
    }

    @Bean
    @Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
    public ConnectionRepository connectionRepository() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new IllegalStateException("Unable to get a ConnectionRepository: no user signed in");
        }
        ConnectionRepository repo;
        try {
            repo = usersConnectionRepository().createConnectionRepository(((Person) authentication.getPrincipal()).getUsername());
        } catch (Exception e) {
            repo = usersConnectionRepository().createConnectionRepository(((User) authentication.getPrincipal()).getUsername());
        }
        return repo;
    }

    /**
     * Proxy for primary linkedin connection.
     *                                                            ;
     * @throws NotConnectedException when no user signed in
     */
    @Bean
    @Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
    public LinkedIn linkedin() {
        return connectionRepository().getPrimaryConnection(LinkedIn.class).getApi();
    }

    /**
     * Proxy for primary facebook connection.
     *
     * @throws NotConnectedException when no user signed in
     */
    @Bean
    @Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
    public Facebook facebook() {
        return connectionRepository().getPrimaryConnection(Facebook.class).getApi();
    }
}