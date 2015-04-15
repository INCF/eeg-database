package cz.zcu.kiv.eegdatabase.wui.ui.account;

import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.PersonMembershipPlan;
import cz.zcu.kiv.eegdatabase.wui.app.EEGDataBaseApplication;
import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;
import cz.zcu.kiv.eegdatabase.wui.components.form.AjaxWizardButtonBar;
import cz.zcu.kiv.eegdatabase.wui.components.menu.button.ButtonPageMenu;
import cz.zcu.kiv.eegdatabase.wui.components.page.BasePage;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.membershipplan.PersonMembershipPlanFacade;
import cz.zcu.kiv.eegdatabase.wui.core.person.PersonFacade;
import cz.zcu.kiv.eegdatabase.wui.core.security.SecurityFacade;
import cz.zcu.kiv.eegdatabase.wui.ui.account.buyplanwizard.BuyPersonMembershipPlanPaymentForm;
import cz.zcu.kiv.eegdatabase.wui.ui.account.buyplanwizard.BuyPersonMembershipPlanSelectionForm;
import cz.zcu.kiv.eegdatabase.wui.ui.groups.GroupPageLeftMenu;
import org.apache.wicket.Component;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.extensions.wizard.Wizard;
import org.apache.wicket.extensions.wizard.WizardModel;
import org.apache.wicket.feedback.ComponentFeedbackMessageFilter;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * ********************************************************************************************************************
 * <p/>
 * This file is part of the eegdatabase project
 * <p/>
 * ==========================================
 * <p/>
 * Copyright (C) 2015 by University of West Bohemia (http://www.zcu.cz/en/)
 * <p/>
 * **********************************************************************************************************************
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 * <p/>
 * **********************************************************************************************************************
 * <p/>
 * BuyMembershipPlanPage, 2015/04/07 05:54 administrator
 * <p/>
 * ********************************************************************************************************************
 */
public class BuyMembershipPlanPersonPage extends MenuPage {


    @SpringBean
    SecurityFacade securityFacade;

    @SpringBean
    PersonMembershipPlanFacade planFacade;

    @SpringBean
    PersonFacade personFacade;

    public BuyMembershipPlanPersonPage(PageParameters parameters) {
        final Person person = personFacade.getLoggedPerson();

        setPageTitle(ResourceUtils.getModel("pageTitle.buyPlan"));

        add(new ButtonPageMenu("leftMenu", MyAccountPageLeftMenu.values()));

        final Model<PersonMembershipPlan> model = new Model<PersonMembershipPlan>(new PersonMembershipPlan());

        WizardModel wizardModel = new WizardModel();
        wizardModel.add(new BuyPersonMembershipPlanSelectionForm(model));
        wizardModel.add(new BuyPersonMembershipPlanPaymentForm(model));

        Wizard wizard = new Wizard("wizard", wizardModel, false) {

            private static final long serialVersionUID = 1L;

            @Override
            public void onFinish() {

                PersonMembershipPlan plan = model.getObject();
                plan.setFrom(new Timestamp(System.currentTimeMillis()));
                plan.setTo(new Timestamp(System.currentTimeMillis() + (plan.getMembershipPlan().getLength() * 1000)));
                //plan.setTo(new Timestamp(System.currentTimeMillis()*plan.getMembershipPlan().getLength()));
                plan.setPerson(person);

                Person logged = EEGDataBaseSession.get().getLoggedUser();

                planFacade.create(plan);

                setResponsePage(ListOfMembershipPlansPersonPage.class);
            }

            @Override
            protected Component newFeedbackPanel(String id) {

                ComponentFeedbackMessageFilter filter = new ComponentFeedbackMessageFilter(this);
                return new FeedbackPanel(id, filter);
            }

            @Override
            protected Component newButtonBar(String id) {
                return new AjaxWizardButtonBar(id, this);
            }

            @Override
            public void onCancel() {
                throw new RestartResponseAtInterceptPageException(ListOfMembershipPlansPersonPage.class, getPageParameters());
            }

        };

        add(wizard);
    }


    private GroupPageLeftMenu[] prepareLeftMenu() {

        List<GroupPageLeftMenu> list = new ArrayList<GroupPageLeftMenu>();
        boolean authorizedToRequestGroupRole = securityFacade.isAuthorizedToRequestGroupRole();

        for (GroupPageLeftMenu tmp : GroupPageLeftMenu.values())
            list.add(tmp);

        if (!authorizedToRequestGroupRole)
            list.remove(GroupPageLeftMenu.REQUEST_FOR_GROUP_ROLE);

        GroupPageLeftMenu[] array = new GroupPageLeftMenu[list.size()];
        return list.toArray(array);
    }

    private StringValue parseParameters(PageParameters parameters) {

        StringValue value = parameters.get(BasePage.DEFAULT_PARAM_ID);
        if (value.isNull() || value.isEmpty())
            throw new RestartResponseAtInterceptPageException(EEGDataBaseApplication.get().getHomePage());
        return value;
    }
}
