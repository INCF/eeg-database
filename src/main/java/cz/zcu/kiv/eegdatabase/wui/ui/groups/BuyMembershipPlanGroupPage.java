package cz.zcu.kiv.eegdatabase.wui.ui.groups;

import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroupMembershipPlan;
import cz.zcu.kiv.eegdatabase.wui.app.EEGDataBaseApplication;
import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;
import cz.zcu.kiv.eegdatabase.wui.components.form.AjaxWizardButtonBar;
import cz.zcu.kiv.eegdatabase.wui.components.menu.button.ButtonPageMenu;
import cz.zcu.kiv.eegdatabase.wui.components.page.BasePage;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.utils.PageParametersUtils;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.group.ResearchGroupFacade;
import cz.zcu.kiv.eegdatabase.wui.core.membershipplan.ResearchGroupMembershipPlanFacade;
import cz.zcu.kiv.eegdatabase.wui.core.security.SecurityFacade;
import cz.zcu.kiv.eegdatabase.wui.ui.groups.form.buyplanwizard.BuyGroupMembershipPlanPaymentForm;
import cz.zcu.kiv.eegdatabase.wui.ui.groups.form.buyplanwizard.BuyGroupMembershipPlanSelectionForm;
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
public class BuyMembershipPlanGroupPage extends MenuPage {


    @SpringBean
    SecurityFacade securityFacade;

    @SpringBean
    ResearchGroupMembershipPlanFacade planFacade;

    @SpringBean
    ResearchGroupFacade groupFacade;

    public BuyMembershipPlanGroupPage(PageParameters parameters) {
        StringValue value = parseParameters(parameters);
        final int groupId = value.toInt();
        final ResearchGroup group = groupFacade.getResearchGroupById(groupId);

        setPageTitle(ResourceUtils.getModel("pageTitle.experimentDetail"));
        add(new ButtonPageMenu("leftMenu", prepareLeftMenu()));

        final Model<ResearchGroupMembershipPlan> model = new Model<ResearchGroupMembershipPlan>(new ResearchGroupMembershipPlan());

        WizardModel wizardModel = new WizardModel();
        wizardModel.add(new BuyGroupMembershipPlanSelectionForm(model));
        wizardModel.add(new BuyGroupMembershipPlanPaymentForm(model));

        Wizard wizard = new Wizard("wizard", wizardModel, false) {

            private static final long serialVersionUID = 1L;

            @Override
            public void onFinish() {

                ResearchGroupMembershipPlan plan = model.getObject();
                plan.setFrom(new Timestamp(System.currentTimeMillis()));
                plan.setTo(new Timestamp(System.currentTimeMillis() + (plan.getMembershipPlan().getLength() * 1000)));
                //plan.setTo(new Timestamp(System.currentTimeMillis()*plan.getMembershipPlan().getLength()));
                plan.setResearchGroup(group);

                Person logged = EEGDataBaseSession.get().getLoggedUser();

                planFacade.create(plan);

                setResponsePage(ListOfMembershipPlansGroupPage.class, PageParametersUtils.getDefaultPageParameters(groupId));
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
                throw new RestartResponseAtInterceptPageException(ListOfMembershipPlansGroupPage.class, getPageParameters());
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
