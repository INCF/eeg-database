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
 *   HomeLoginForm.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.ui.home;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.StringValidator;

import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.security.LoginUserDTO;
import cz.zcu.kiv.eegdatabase.wui.core.security.SecurityFacade;
import cz.zcu.kiv.eegdatabase.wui.ui.welcome.WelcomePage;

/**
 * Form for login form on page.
 * 
 * @author Jakub Rinkes
 *
 */
public class HomeLoginForm extends Form<LoginUserDTO> {

    private static final long serialVersionUID = -5196364867691352802L;

    @SpringBean
    SecurityFacade secFacade;

    public HomeLoginForm(String id) {
        super(id, new CompoundPropertyModel<LoginUserDTO>(new LoginUserDTO()));

        TextField<String> userName = new TextField<String>("userName");
        userName.setRequired(true);

        add(userName);

        PasswordTextField password = new PasswordTextField("password");
        password.setRequired(true);

        add(password);

        Button submit = new Button("submit", ResourceUtils.getModel("action.login")) {

            private static final long serialVersionUID = 1L;

            @Override
            public void onSubmit() {
                LoginUserDTO object = HomeLoginForm.this.getModelObject();
                if (EEGDataBaseSession.get().signIn(object.getUserName().toLowerCase(), object.getPassword())) {
                    continueToOriginalDestination();
                    setResponsePage(WelcomePage.class);

                } else {
                    error("User cannot be log in.");
                }
            }
        };
        add(submit);
    }

}
