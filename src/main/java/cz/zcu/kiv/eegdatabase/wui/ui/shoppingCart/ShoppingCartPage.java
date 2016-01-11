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
 *   ShoppingCartPage.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.ui.shoppingCart;

import cz.zcu.kiv.eegdatabase.data.pojo.*;
import cz.zcu.kiv.eegdatabase.logic.eshop.ShoppingCart;
import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;
import cz.zcu.kiv.eegdatabase.wui.components.menu.button.ButtonPageMenu;
import cz.zcu.kiv.eegdatabase.wui.components.model.MoneyFormatConverter;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.utils.PageParametersUtils;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.MembershipPlanType;
import cz.zcu.kiv.eegdatabase.wui.core.experimentLicense.ExperimentLicenseFacade;
import cz.zcu.kiv.eegdatabase.wui.core.experimentpackage.ExperimentPackageLicenseFacade;
import cz.zcu.kiv.eegdatabase.wui.core.license.LicenseFacade;
import cz.zcu.kiv.eegdatabase.wui.core.membershipplan.PersonMembershipPlanFacade;
import cz.zcu.kiv.eegdatabase.wui.core.membershipplan.ResearchGroupMembershipPlanFacade;
import cz.zcu.kiv.eegdatabase.wui.core.order.OrderFacade;
import cz.zcu.kiv.eegdatabase.wui.ui.order.OrderDetailPage;
import cz.zcu.kiv.eegdatabase.wui.ui.order.OrderItemPanel;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PropertyListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.convert.IConverter;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;
import java.util.List;

@AuthorizeInstantiation(value = { "ROLE_READER", "ROLE_USER", "ROLE_EXPERIMENTER", "ROLE_ADMIN" })
public class ShoppingCartPage extends MenuPage {

    private static final long serialVersionUID = 1L;

    @SpringBean
    private OrderFacade orderFacade;

    @SpringBean
    PersonMembershipPlanFacade personPlanFacade;

    @SpringBean
    ResearchGroupMembershipPlanFacade groupPlanFacade;

    @SpringBean
    private LicenseFacade licenseFacade;
    
    @SpringBean
    private ExperimentLicenseFacade experimentLicenseFacade;
    
    @SpringBean
    private ExperimentPackageLicenseFacade experimentPackageLicenseFacade;

    private Label totalPriceLabel;

    
    public ShoppingCartPage() {
        setupComponents();
    }

    
    private void setupComponents() {

        IModel<String> title = ResourceUtils.getModel("pageTitle.myCart");
        add(new Label("title", title));
        setPageTitle(title);
        add(new ButtonPageMenu("leftMenu", ShoppingCartPageLeftMenu.values()));

        final ShoppingCart shoppingCart = EEGDataBaseSession.get().getShoppingCart();

        // empty cart
        add(new Label("emptyCart", ResourceUtils.getModel("text.emptyCart")) {

            private static final long serialVersionUID = 1L;

            @Override
            public boolean isVisible() {
                return shoppingCart.isEmpty();
            }
        });

        final WebMarkupContainer container = new WebMarkupContainer("itemsContainer") {

            private static final long serialVersionUID = 1L;

            @Override
            public boolean isVisible() {
                return !shoppingCart.isEmpty();
            }
        };
        container.setOutputMarkupId(true);

        @SuppressWarnings("serial")
        PropertyListView<OrderItem> items = new PropertyListView<OrderItem>("items", shoppingCart.getOrder().getItems()) {

            @Override
            protected void populateItem(final ListItem<OrderItem> item) {

                item.add(new OrderItemPanel("item", item.getModel(), true));

                // XXX price hidden for now.
                /*
                 * item.add(new Label("price", item.getModel().getObject().getPrice()) {
                 * 
                 * @Override public <C> IConverter<C> getConverter(Class<C> type) { return new MoneyFormatConverter(Currency.getInstance("EUR"), 2); } });
                 */
                item.add(new RemoveLinkPanel("removeItemLink", item.getModel()));
                
                Label licenseTitle = new Label("licenseTitle", ResourceUtils.getModel("dataTable.heading.licenseTitle"));
                item.add(licenseTitle);

                List<License> licenses = new ArrayList<License>();
                ChoiceRenderer<License> renderer = new ChoiceRenderer<License>("title", "licenseId");
                final DropDownChoice<License> licenseChoice = new DropDownChoice<License>("license", licenses, renderer);
                licenseChoice.setNullValid(false);
                
                if (item.getModelObject().getExperiment() != null) {
                    
                    final List<ExperimentLicence> expLicenses = experimentLicenseFacade.getExperimentLicensesForExperiment(item.getModelObject().getExperiment());
                    for (ExperimentLicence expLicense : expLicenses)
                        licenses.add(expLicense.getLicense());

                    licenseChoice.add(new AjaxFormComponentUpdatingBehavior("onChange") {

                        @Override
                        protected void onUpdate(AjaxRequestTarget target) {
                            ExperimentLicence experimentLicense = findExperimentLicense(expLicenses, licenseChoice.getModelObject());
                            item.getModelObject().setPrice(experimentLicense.getPrice());
                            target.add(container);
                            target.add(totalPriceLabel);
                        }
                    });
                    
                } else if (item.getModelObject().getExperimentPackage() != null) {
                    
                    final List<ExperimentPackageLicense> expPacLicenses = experimentPackageLicenseFacade.getExperimentPackageLicensesForPackage(item.getModelObject().getExperimentPackage());
                    for (ExperimentPackageLicense epl : expPacLicenses)
                        licenses.add(epl.getLicense());
                    
                    licenseChoice.add(new AjaxFormComponentUpdatingBehavior("onChange") {

                        @Override
                        protected void onUpdate(AjaxRequestTarget target) {
                            ExperimentPackageLicense expPacLicense = findExperimentPackageLicense(expPacLicenses, licenseChoice.getModelObject());
                            item.getModelObject().setPrice(expPacLicense.getPrice());
                            target.add(container);
                            target.add(totalPriceLabel);
                        }
                    });

                } else if (item.getModelObject().getMembershipPlan() != null) {
                    licenseChoice.setVisible(false);
                    licenseTitle.setVisibilityAllowed(false);
                }
                
                item.add(licenseChoice);
                
                Label price = new Label("price", new PropertyModel<String>(item.getModelObject(), "price"));
                item.add(price);
            }
        };
        
        items.setReuseItems(true);
        container.add(items);
        add(container);
        add(new Label("totalPriceMessage", ResourceUtils.getString("label.totalPrice") + " "));

        totalPriceLabel = new Label("totalPriceAmount", new PropertyModel<BigDecimal>(shoppingCart, "totalPrice")) {

            private static final long serialVersionUID = 1L;

            @Override
            public <C> IConverter<C> getConverter(Class<C> type) {
                return new MoneyFormatConverter(Currency.getInstance("EUR"), 2);
            }
        };
        totalPriceLabel.setOutputMarkupId(true);
        add(totalPriceLabel);

        // XXX PAYPAL payment disabled - not necessary now.
        /*
         * add(new Link<Void>("PayPalExpressCheckoutLink") {
         * 
         * private static final long serialVersionUID = 1L;
         * 
         * @Override public void onClick() {
         * 
         * if (!shoppingCart.isEmpty()) { setResponsePage(new RedirectPage(PayPalTools.setExpressCheckout())); } else { // Partially fixes trouble with browser caching and back
         * button setResponsePage(ShoppingCartPage.class); }
         * 
         * } }.setVisibilityAllowed(!shoppingCart.isEmpty()));
         */
        // For now just create order and show it.

        add(new Link<Void>("createOrder") {

            private static final long serialVersionUID = 1L;

            @Override
            public void onClick() {
                
                Order order = shoppingCart.getOrder();
                order.setDate(new Timestamp(new Date().getTime()));
                order.setPerson(EEGDataBaseSession.get().getLoggedUser());
                order.setOrderPrice(shoppingCart.getTotalPrice());
                
                for(OrderItem item : order.getItems()) {
                    
                    if (item.getPrice() == null) {
                        item.setPrice(BigDecimal.ZERO);
                    }

                    if (item.getMembershipPlan() != null) {
                        MembershipPlan plan = item.getMembershipPlan();
                        if (MembershipPlanType.GROUP.getType()==plan.getType()) {
                            ResearchGroup group = item.getResearchGroup();
                            ResearchGroupMembershipPlan groupPlan = new ResearchGroupMembershipPlan();
                            groupPlan.setResearchGroup(group);
                            groupPlan.setMembershipPlan(plan);
                            groupPlan.setFrom(new Timestamp(System.currentTimeMillis()));
                            groupPlan.setTo(new Timestamp(System.currentTimeMillis() + (groupPlan.getMembershipPlan().getLength() * 86400000)));
                            groupPlanFacade.create(groupPlan);
                        } else {
                            Person logged = EEGDataBaseSession.get().getLoggedUser();
                            PersonMembershipPlan personPlan = new PersonMembershipPlan();
                            personPlan.setPerson(logged);
                            personPlan.setMembershipPlan(plan);
                            personPlan.setFrom(new Timestamp(System.currentTimeMillis()));
                            personPlan.setTo(new Timestamp(System.currentTimeMillis() + (personPlan.getMembershipPlan().getLength() * 86400000)));
                            personPlanFacade.create(personPlan);
                        }
                    }
                }

                Integer orderId = orderFacade.create(order);

                EEGDataBaseSession.get().getShoppingCart().clear();
                // flush cache with purchased items from session
                EEGDataBaseSession.get().reloadPurchasedItemCache();
                setResponsePage(OrderDetailPage.class, PageParametersUtils.getDefaultPageParameters(orderId));
            }

        }.setVisibilityAllowed(!shoppingCart.isEmpty()));

    }
    
    
    private ExperimentLicence findExperimentLicense(List<ExperimentLicence> list, License license) {
        for (ExperimentLicence item : list) {
            if (item.getLicense().equals(license))
                return item;
        }
        return null;
    }
    
    
    private ExperimentPackageLicense findExperimentPackageLicense(List<ExperimentPackageLicense> list, License license) {
        for (ExperimentPackageLicense item : list) {
            if (item.getLicense().equals(license))
                return item;
        }
        return null;
    }

}
