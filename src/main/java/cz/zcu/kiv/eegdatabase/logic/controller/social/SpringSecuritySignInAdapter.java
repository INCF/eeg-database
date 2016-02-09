/*******************************************************************************
 * This file is part of the EEG-database project
 *
 * ==========================================
 *
 * Copyright (C) 2013 by University of West Bohemia (http://www.zcu.cz/en/)
 *
 * ***********************************************************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * ***********************************************************************************************************************
 *
 * SpringSecuritySignInAdapter.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.logic.controller.social;

import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.web.context.request.NativeWebRequest;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;

/**
 * Signs the user in by setting spring security authentication.
 * Uses SocialAuthenticationToken to sign in.
 *
 * @author Michal Patočka
 * @see SocialAuthenticationToken
 */
public class SpringSecuritySignInAdapter implements SignInAdapter {

    //private final RequestCache requestCache;
    private @Inject
    PersonDao personDao;

    public SpringSecuritySignInAdapter() {

    }

    @Override
    public String signIn(String localUserId, Connection<?> connection, NativeWebRequest request) {

        Person person = personDao.getPerson(localUserId);

        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(person.getAuthority());
        List<GrantedAuthority> grantedAuthorities = Collections.singletonList(grantedAuthority);
        Authentication a = new SocialAuthenticationToken(grantedAuthorities, person);
        a.setAuthenticated(true);
        SecurityContextHolder.getContext().setAuthentication(a);

        //returns null - providerSignInController will redirect to its postSigninUrl
        return null;
    }


}