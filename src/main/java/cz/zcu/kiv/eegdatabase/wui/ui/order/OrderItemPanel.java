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
 *   OrderItemPanel.java, 2014/14/09 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.ui.order;

import com.ibm.icu.text.SimpleDateFormat;
import cz.zcu.kiv.eegdatabase.data.pojo.*;
import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;
import cz.zcu.kiv.eegdatabase.wui.components.table.TimestampLabel;
import cz.zcu.kiv.eegdatabase.wui.components.utils.PageParametersUtils;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.components.utils.StringUtils;
import cz.zcu.kiv.eegdatabase.wui.core.experiments.ExperimentsFacade;
import cz.zcu.kiv.eegdatabase.wui.core.promocode.PromoCodeFacade;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.ExperimentsDetailPage;
import cz.zcu.kiv.eegdatabase.wui.ui.memberships.MembershipPlansDetailPage;
import cz.zcu.kiv.eegdatabase.wui.ui.order.components.PromoCodePopupForm;
import cz.zcu.kiv.eegdatabase.wui.ui.order.components.StringWrapper;
import cz.zcu.kiv.eegdatabase.wui.ui.shoppingCart.ShoppingCartPage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PropertyListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.math.BigDecimal;
import java.util.Date;

public class OrderItemPanel extends Panel {

    private static final long serialVersionUID = 1L;
    protected Log log = LogFactory.getLog(getClass());

    @SpringBean
    private ExperimentsFacade facade;

    @SpringBean
    private PromoCodeFacade promoFacade;

    private IModel<String> showActionModel;
    private IModel<String> hideActionModel;

    private boolean malleable;

    public OrderItemPanel(String id, final IModel<OrderItem> model, boolean malleable) {
        super(id, new CompoundPropertyModel<OrderItem>(model));

        this.malleable = malleable;

        showActionModel = ResourceUtils.getModel("action.show");
        hideActionModel = ResourceUtils.getModel("action.hide");

        final OrderItem modelItem = model.getObject();
        final Experiment experiment = model.getObject().getExperiment();
        final ExperimentPackage experimentPackage = model.getObject().getExperimentPackage();
        final MembershipPlan membershipPlan = model.getObject().getMembershipPlan();
        final ResearchGroup researchGroup = model.getObject().getResearchGroup();

        // prepare containers
        WebMarkupContainer experimentContainer = new WebMarkupContainer("experiment") {

            private static final long serialVersionUID = 1L;

            @Override
            public boolean isVisible() {
                return experiment != null;
            }
        };

        final WebMarkupContainer packageContainer = new WebMarkupContainer("package") {

            private static final long serialVersionUID = 1L;

            @Override
            public boolean isVisible() {
                return experimentPackage != null;
            }
        };


        WebMarkupContainer membershipPlanContainer = new WebMarkupContainer("membershipPlan") {

            private static final long serialVersionUID = 1L;

            @Override
            public boolean isVisible() {
                return membershipPlan != null;
            }
        };


        // prepare texts for experiment container
        int experimentId;
        String scenarioTitle;
        String date;
        if (experiment != null) {
            experimentId = experiment.getExperimentId();
            scenarioTitle = experiment.getScenario().getTitle();
            date = new SimpleDateFormat(StringUtils.DATE_TIME_FORMAT_PATTER, EEGDataBaseSession.get().getLocale()).format((Date) experiment.getStartTime());
        } else {
            experimentId = 0;
            scenarioTitle = "";
            date = "";
        }

        int membershipPlanID;
        String membershipPlanName= "";
        String researchGroupName = "";

        if (membershipPlan!= null)
        {
            membershipPlanName = membershipPlan.getName();
            membershipPlanID = membershipPlan.getMembershipId();
        }
        else
        {
            membershipPlanID = -1;
        }
        if(researchGroup != null)
        {
            researchGroupName = " for "+researchGroup.getDescription();
        }
        else
        {
            researchGroupName = "";
        }

        // add components for experiment container
        experimentContainer.add(new Label("experimentText1", ResourceUtils.getModel("text.order.item.experiment1", Integer.toString(experimentId), scenarioTitle)));
        experimentContainer.add(new Label("experimentText2", ResourceUtils.getModel("text.order.item.experiment2", date)));
        experimentContainer.add(new BookmarkablePageLink<Void>("detail", ExperimentsDetailPage.class, PageParametersUtils.getDefaultPageParameters(experimentId)));

        membershipPlanContainer.add(new Label("membershipPlanText1", membershipPlanName + researchGroupName));
        membershipPlanContainer.add(new BookmarkablePageLink<Void>("detail", MembershipPlansDetailPage.class, PageParametersUtils.getDefaultPageParameters(membershipPlanID)));
        final ModalWindow promoCodePopup = this.addPromoCodePopup(modelItem, membershipPlanContainer);


        // prepare texts for package container
        int packageId;
        String name;
        String group;
        if (experimentPackage != null) {
            packageId = experimentPackage.getExperimentPackageId();
            name = experimentPackage.getName();
            group = experimentPackage.getResearchGroup() != null ? experimentPackage.getResearchGroup().getTitle() : "";
        } else {
            packageId = 0;
            name = "";
            group = "";
        }

        // add components for package container
        packageContainer.add(new Label("packageText1", ResourceUtils.getModel("text.order.item.package1", name)));
        //packageContainer.add(new Label("packageText2", ResourceUtils.getModel("text.order.item.package2", group)));

        // add component for list of experiments in package
        final PropertyListView<Experiment> packageExperimentList = new PropertyListView<Experiment>("experiments", facade.getExperimentsByPackage(packageId)) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(ListItem<Experiment> item) {
                item.add(new Label("experimentId"));
                item.add(new Label("scenario.title"));
                item.add(new TimestampLabel("date", item.getModelObject().getStartTime(), StringUtils.DATE_TIME_FORMAT_PATTER));
                item.add(new BookmarkablePageLink<Void>("detail", ExperimentsDetailPage.class, PageParametersUtils.getDefaultPageParameters(item.getModelObject().getExperimentId())));
            }
        };

        // add show hide link for list of experiments in package
        final Label showHideLinkLabel = new Label("showHideLinkLabel", showActionModel);
        AjaxLink<Void> showHideLink = new AjaxLink<Void>("detail") {

            private static final long serialVersionUID = 1L;

            @Override
            public void onClick(AjaxRequestTarget target) {

                packageExperimentList.setVisible(!packageExperimentList.isVisible());
                if (packageExperimentList.isVisible())
                    showHideLinkLabel.setDefaultModel(hideActionModel);
                else
                    showHideLinkLabel.setDefaultModel(showActionModel);

                target.add(packageContainer);
            }
        };

        showHideLink.add(showHideLinkLabel);
        packageContainer.add(showHideLink);
        packageContainer.add(packageExperimentList);
        packageExperimentList.setVisible(false);

        experimentContainer.setOutputMarkupId(true);
        packageContainer.setOutputMarkupId(true);
        add(experimentContainer, packageContainer, membershipPlanContainer);
        //BookmarkablePageLink<Void> applyPromoCodeLink = new BookmarkablePageLink<Void>("applyPromoCode", BuyMembershipPlanPersonPage.class);
        //add(applyPromoCodeLink);

    }

    private ModalWindow addPromoCodePopup(final OrderItem parent, WebMarkupContainer membershipPlanContainer) {
        final ModalWindow popup = new ModalWindow("promoCodePopup");
        popup.setAutoSize(true);
        popup.setResizable(false);
        popup.setMinimalWidth(500);
        popup.setWidthUnit("px");
        popup.showUnloadConfirmation(false);

       PromoCodePopupForm popupForm = new PromoCodePopupForm(popup.getContentId(), new Model<StringWrapper>(new StringWrapper())) {

            @Override
            protected void onSubmitAction(IModel<StringWrapper> strWrapper, AjaxRequestTarget target, Form<?> form)
            {
                String code = strWrapper.getObject().getValue();
                if(parent.getMembershipPlan()!=null)
                {
                    if(parent.getResearchGroup()==null)
                    {
                        if(promoFacade.isValidPersonalPlanCode(code))
                        {
                            PromoCode promoCode = promoFacade.getPromoCodeByKeyword(code);
                            parent.setPromoCode(promoCode);
                            double price = parent.getMembershipPlan().getPrice().doubleValue()*(1d-((double)promoCode.getDiscount()/100d));
                            parent.setPrice(new BigDecimal(price));
                        }
                    }
                    else
                    {
                        if(promoFacade.isValidGroupPlanCode(code))
                        {
                            PromoCode promoCode = promoFacade.getPromoCodeByKeyword(code);
                            parent.setPromoCode(promoCode);
                            double price = parent.getMembershipPlan().getPrice().doubleValue()*(1d-((double)promoCode.getDiscount()/100d));
                            parent.setPrice(new BigDecimal(price));
                        }
                    }
                }
                ModalWindow.closeCurrent(target);
                setResponsePage(ShoppingCartPage.class);
            }

            @Override
            protected void onCancelAction(IModel<StringWrapper> strWrapper, AjaxRequestTarget target, Form<?> form) {
                ModalWindow.closeCurrent(target);
            }

        };
        popup.setContent(popupForm);
        membershipPlanContainer.add(popup);
        AjaxLink popupLink = new AjaxLink<Object>("applyPromoCode") {

            private static final long serialVersionUID = 1L;

            @Override
            public void onClick(AjaxRequestTarget target) {
                //License l = this.getModelObject();
                //if (l!=null) System.out.println(l.getTitle());
                popup.show(target);
            }

        };
        popup.setOutputMarkupPlaceholderTag(true);
        popup.setVisibilityAllowed(true);
        String promoCode = "";
        if(parent.getPromoCode()!=null)
        {
            promoCode = "Applied code: "+parent.getPromoCode().getKeyword()+" ("+parent.getPromoCode().getDiscount()+"% off)";
        }
        membershipPlanContainer.add(new Label("promoCodeText", promoCode));
        membershipPlanContainer.add(popupLink);
        popupLink.setVisible(malleable);
        return popup;
    }


}
