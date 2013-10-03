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
 *   ForgottenPasswordPage.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.ui.security;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.EmailTextField;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.person.PersonFacade;

/**
 * Page for generate new password and send it on email adress if the email adress is in system.
 * 
 * @author Jakub Rinkes
 *
 */
public class ForgottenPasswordPage extends MenuPage {

    private static final long serialVersionUID = 8777241488694645558L;

    @SpringBean
    PersonFacade personFacade;

    public ForgottenPasswordPage() {
        setPageTitle(ResourceUtils.getModel("pageTitle.forgottenPassword"));
        add(new ForgottenForm("form", getFeedback()));
    }

    class ForgottenForm extends Form<Person> {

        private static final long serialVersionUID = 1L;

        public ForgottenForm(String id, final FeedbackPanel feedback) {
            super(id, new CompoundPropertyModel<Person>(new Person()));

            final FormComponent<String> username = new EmailTextField("username");
            username.setRequired(true);

            final AjaxButton submit = new AjaxButton("submit", ResourceUtils.getModel("button.send"), this) {

                private static final long serialVersionUID = 1L;

                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {

                    Person personObj = ForgottenForm.this.getModelObject();
                    Person person = personFacade.getPerson(personObj.getUsername());
                    if (person != null && person.isConfirmed()) {
                        personFacade.forgottenPassword(person);

                        info(ResourceUtils.getString("pageTitle.forgottenPasswordSuccess"));
                        username.setVisibilityAllowed(false);
                        this.setVisibilityAllowed(false);
                    } else {

                        error(ResourceUtils.getString("pageTitle.forgottenPasswordFailed"));
                    }

                    target.add(this);
                    target.add(username);
                    target.add(feedback);
                }

                @Override
                protected void onError(AjaxRequestTarget target, Form<?> form) {
                    target.add(feedback);
                }

            };
            username.setOutputMarkupId(true);
            submit.setOutputMarkupId(true);
            add(username, submit);
        }
    }
}
