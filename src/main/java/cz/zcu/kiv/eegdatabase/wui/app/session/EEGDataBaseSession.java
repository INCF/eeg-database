package cz.zcu.kiv.eegdatabase.wui.app.session;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.Session;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.request.Request;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

public class EEGDataBaseSession extends AuthenticatedWebSession {

    private static final long serialVersionUID = 4193935519312775047L;
    
    protected Log log = LogFactory.getLog(getClass());
    
    @SpringBean(name="authenticationManager")
    private AuthenticationManager authenticationManager;
    
    private String userName;
    
    public static EEGDataBaseSession get()
    {
        return (EEGDataBaseSession)Session.get();
    }
    
    public EEGDataBaseSession(Request request) {
        super(request);
        injectDependencies();
        ensureDependenciesNotNull();
    }

    private void ensureDependenciesNotNull() {
        if (authenticationManager == null) {
            throw new IllegalStateException("AdminSession requires an authenticationManager.");
        }
    }

    private void injectDependencies() {
        Injector.get().inject(this);
    }

    @Override
    public boolean authenticate(String username, String password) {
        boolean authenticated = false;
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            authenticated = authentication.isAuthenticated();
            this.userName = username;
            
        } catch (AuthenticationException e) {
            error((String.format("User '%s' failed to login. Reason: %s", username, e.getMessage())));
            authenticated = false;
        }
        return authenticated;
    }

    @Override
    public Roles getRoles() {
        Roles roles = new Roles();
        if(isSignedIn()) {
            
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            
            for(GrantedAuthority auth : authentication.getAuthorities())
                roles.add(auth.getAuthority());
        }
        
        return roles;
    }
    
    public boolean hasRole(String role){
        
        return getRoles().hasRole(role);
    }
    
    public boolean hasAnyRole(Roles roles) {
        
        return getRoles().hasAnyRole(roles);
    }
    
    public String getUserName() {
        return userName;
    }
}
