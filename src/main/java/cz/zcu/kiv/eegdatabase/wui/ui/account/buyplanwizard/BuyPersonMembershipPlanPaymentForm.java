package cz.zcu.kiv.eegdatabase.wui.ui.account.buyplanwizard;

import cz.zcu.kiv.eegdatabase.data.pojo.MembershipPlan;
import cz.zcu.kiv.eegdatabase.data.pojo.PersonMembershipPlan;
import cz.zcu.kiv.eegdatabase.wui.core.membershipplan.MembershipPlanFacade;
import cz.zcu.kiv.eegdatabase.wui.core.security.SecurityFacade;
import org.apache.wicket.extensions.wizard.WizardStep;
import org.apache.wicket.feedback.ComponentFeedbackMessageFilter;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

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
 * BuyGroupMembershipPlanSelectionForm, 2015/04/07 05:13 administrator
 * <p/>
 * ********************************************************************************************************************
 */
public class BuyPersonMembershipPlanPaymentForm extends WizardStep {

    @SpringBean
    private MembershipPlanFacade membershipPlanFacade;
    @SpringBean
    private SecurityFacade securityFacade;

    private IModel<PersonMembershipPlan> model;

    private DropDownChoice<MembershipPlan> membershipPlanDropDownChoice;

    public BuyPersonMembershipPlanPaymentForm(IModel<PersonMembershipPlan> model)
    {

        this.model = model;
        setOutputMarkupId(true);

    }


    private FeedbackPanel createFeedbackForComponent(FormComponent component, String id) {

        ComponentFeedbackMessageFilter filter = new ComponentFeedbackMessageFilter(component);
        final FeedbackPanel feedback = new FeedbackPanel(id, filter);
        feedback.setOutputMarkupId(true);

        return feedback;
    }

}
