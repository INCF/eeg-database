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
 *   EEGDataBaseSession.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.app.session;

import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.Session;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.request.Request;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.logic.eshop.ShoppingCart;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.experimentpackage.ExperimentPackageFacade;
import cz.zcu.kiv.eegdatabase.wui.core.experiments.ExperimentsFacade;
import cz.zcu.kiv.eegdatabase.wui.core.order.OrderFacade;
import cz.zcu.kiv.eegdatabase.wui.core.person.PersonFacade;

/**
 * Implementation custom session object. Prepared authorization and keep information about roles.
 * 
 * @author Jakub Rinkes
 * 
 */
public class EEGDataBaseSession extends AuthenticatedWebSession {

    private static final long serialVersionUID = 4193935519312775047L;

    protected Log log = LogFactory.getLog(getClass());

    @SpringBean(name = "authenticationManager")
    private AuthenticationManager authenticationManager;

    @SpringBean
    PersonFacade facade;

    @SpringBean
    OrderFacade orderFacade;

    @SpringBean
    ExperimentPackageFacade epFacade;

    @SpringBean
    ExperimentsFacade eFacade;

    private Person loggedUser;
    private final String SOCIAL_PASSWD = "#SOCIAL#";

    private ShoppingCart shoppingCart;

    private StringValue searchString;

    /*
     * software cache with purchased experiments and packages. Cache is flushed after create new order.
     */
    private Set<Integer> purchasedExperiments;
    private Set<Integer> purchasedExperimentPackages;

    public static EEGDataBaseSession get()
    {
        return (EEGDataBaseSession) Session.get();
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

        if (password.equalsIgnoreCase(SOCIAL_PASSWD)) {
            this.setLoggedUser(facade.getPerson(username));
            this.createShoppingCart();
            reloadPurchasedItemCache();
            return true;
        }

        boolean authenticated = false;
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            authenticated = authentication.isAuthenticated();
            this.setLoggedUser(facade.getPerson(username));
            reloadPurchasedItemCache();
            this.createShoppingCart();

        } catch (AuthenticationException e) {
            error((String.format("User '%s' failed to login. Reason: %s", username, e.getMessage())));
            authenticated = false;
        }

        if (getLoggedUser() != null && getLoggedUser().isLock()) {
            this.setLoggedUser(null);
            SecurityContextHolder.clearContext();
            this.shoppingCart = null;
            error(ResourceUtils.getString("text.user.lock.login", username));
            return false;
        }

        return authenticated;
    }

    @Override
    public Roles getRoles() {
        Roles roles = new Roles();
        if (isSignedIn()) {

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            for (GrantedAuthority auth : authentication.getAuthorities()) {
                roles.add(auth.getAuthority());
            }
        }

        return roles;
    }

    public boolean hasRole(String role) {

        return getRoles().hasRole(role);
    }

    public boolean hasAnyRole(Roles roles) {

        return getRoles().hasAnyRole(roles);
    }

    public boolean authenticatedSocial() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        for (GrantedAuthority auth : authentication.getAuthorities()) {
            if (auth.getAuthority().equals("ROLE_ANONYMOUS"))
                return false;
        }

        if (authentication.isAuthenticated()) {

            String username = "";

            if (authentication.getPrincipal() instanceof User)
                username = ((User) authentication.getPrincipal()).getUsername();
            else if (authentication.getPrincipal() instanceof Person)
                username = ((Person) authentication.getPrincipal()).getUsername();

            return signIn(username, SOCIAL_PASSWD);
        }

        return false;
    }

    public Person getLoggedUser() {
        return loggedUser;
    }

    private void setLoggedUser(Person loggedUser) {
        this.loggedUser = loggedUser;
    }

    private void createShoppingCart() {
        // There can be only one Shopping cart per Session.
        if (this.shoppingCart == null) {
            this.shoppingCart = new ShoppingCart();
        }
    }

    public ShoppingCart getShoppingCart() {
        return shoppingCart;
    }

    public StringValue getSearchString() {
        if (searchString == null || searchString.isNull()) {
            return StringValue.valueOf("");
        }
        return searchString;
    }

    public void setSearchString(StringValue searchString) {
        this.searchString = searchString;
    }

    /**
     * Flush cache and reload new state.
     */
    public void reloadPurchasedItemCache() {

        purchasedExperiments = orderFacade.getPurchasedExperimentId(getLoggedUser().getPersonId());
        purchasedExperimentPackages = orderFacade.getPurchasedExperimentPackageId(getLoggedUser().getPersonId());
    }

    public boolean isExperimentPurchased(int experimentId) {

        if (purchasedExperiments == null || purchasedExperiments.isEmpty())
            return false;

        return purchasedExperiments.contains(experimentId);
    }

    public boolean isExperimentPackagePurchased(int packageId) {

        if (purchasedExperimentPackages == null || purchasedExperimentPackages.isEmpty())
            return false;

        return purchasedExperimentPackages.contains(packageId);
    }
}
