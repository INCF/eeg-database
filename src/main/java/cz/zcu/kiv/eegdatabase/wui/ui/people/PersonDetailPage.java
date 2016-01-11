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
 *   PersonDetailPage.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.ui.people;

import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.PersonOptParamVal;
import cz.zcu.kiv.eegdatabase.wui.app.EEGDataBaseApplication;
import cz.zcu.kiv.eegdatabase.wui.components.menu.button.ButtonPageMenu;
import cz.zcu.kiv.eegdatabase.wui.components.page.BasePage;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.table.TimestampLabel;
import cz.zcu.kiv.eegdatabase.wui.components.utils.PageParametersUtils;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.components.utils.StringUtils;
import cz.zcu.kiv.eegdatabase.wui.core.Gender;
import cz.zcu.kiv.eegdatabase.wui.core.person.PersonFacade;
import cz.zcu.kiv.eegdatabase.wui.core.security.SecurityFacade;
import cz.zcu.kiv.eegdatabase.wui.ui.people.form.PersonAddParamFormPage;
import cz.zcu.kiv.eegdatabase.wui.ui.people.form.PersonFormPage;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.basic.EnumLabel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PropertyListView;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;

import java.util.ArrayList;

/**
 * Page of detail for person.
 * 
 * @author Jakub Rinkes
 *
 */
@AuthorizeInstantiation(value = {"ROLE_ADMIN" })
public class PersonDetailPage extends MenuPage {

    private static final long serialVersionUID = 1L;
    
    @SpringBean
    PersonFacade personFacade;
    
    @SpringBean
    SecurityFacade security;
    
    public PersonDetailPage(PageParameters parameters) {
        
        int personId = parseParameters(parameters);

        setupComponents(personId);
    }
    
    private void setupComponents(int personId) {
        
        setPageTitle(ResourceUtils.getModel("pageTitle.personDetail"));

        add(new ButtonPageMenu("leftMenu", PersonPageLeftMenu.values()));
        
        Person person = personFacade.getPersonForDetail(personId);
        
        add(new EnumLabel<Gender>("gender", Gender.getGenderByShortcut(person.getGender())));
        add(new Label("title", person.getTitle()));
        add(new Label("name", person.getGivenname() + " " + person.getSurname()));
        add(new Label("email", person.getUsername()));
        add(new Label("country", person.getCountry()));
        add(new Label("note", person.getNote()));
        add(new Label("phone", person.getPhoneNumber()));
        add(new TimestampLabel("dateOfBirth", person.getDateOfBirth(), StringUtils.DATE_FORMAT_PATTER));
        
        PropertyListView<PersonOptParamVal> param = new PropertyListView<PersonOptParamVal>("params", new ArrayList<PersonOptParamVal>(person.getPersonOptParamVals())) {
            
            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(ListItem<PersonOptParamVal> item) {
                item.add(new Label("paramName", item.getModelObject().getPersonOptParamDef().getParamName()));
                item.add(new Label("paramValue", item.getModelObject().getParamValue()));
                
            }
        };
        
        
        BookmarkablePageLink<Void> addParameterLink = new BookmarkablePageLink<Void>("addOptParamLink", PersonAddParamFormPage.class, PageParametersUtils.getDefaultPageParameters(personId));
        BookmarkablePageLink<Void> editLink = new BookmarkablePageLink<Void>("editLink", PersonFormPage.class, PageParametersUtils.getDefaultPageParameters(personId));
        editLink.setVisibilityAllowed(security.userCanEditPerson(personId));
        add(param, addParameterLink, editLink);
    }

    private int parseParameters(PageParameters parameters) {

        StringValue value = parameters.get(BasePage.DEFAULT_PARAM_ID);
        if (value.isNull() || value.isEmpty())
            throw new RestartResponseAtInterceptPageException(EEGDataBaseApplication.get().getHomePage());
        return value.toInt();
    }

}
