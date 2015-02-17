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

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;
import java.util.List;

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

import cz.zcu.kiv.eegdatabase.data.pojo.License;
import cz.zcu.kiv.eegdatabase.data.pojo.Order;
import cz.zcu.kiv.eegdatabase.data.pojo.OrderItem;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.logic.eshop.ShoppingCart;
import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;
import cz.zcu.kiv.eegdatabase.wui.components.menu.button.ButtonPageMenu;
import cz.zcu.kiv.eegdatabase.wui.components.model.MoneyFormatConverter;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.utils.PageParametersUtils;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.license.LicenseFacade;
import cz.zcu.kiv.eegdatabase.wui.core.order.OrderFacade;
import cz.zcu.kiv.eegdatabase.wui.ui.order.OrderDetailPage;
import cz.zcu.kiv.eegdatabase.wui.ui.order.OrderItemPanel;

@AuthorizeInstantiation(value = { "ROLE_READER", "ROLE_USER", "ROLE_EXPERIMENTER", "ROLE_ADMIN" })
public class ShoppingCartPage extends MenuPage {

    private static final long serialVersionUID = 1L;

    @SpringBean
    private OrderFacade orderFacade;

    @SpringBean
    private LicenseFacade licenseFacade;

    private Label totalPriceLabel;

    public ShoppingCartPage() {
        setupComponents();
    }

    private void setupComponents() {

        IModel<String> title = ResourceUtils.getModel("pageTitle.myCart");
        final Person loggedUser = EEGDataBaseSession.get().getLoggedUser();
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

        PropertyListView<OrderItem> items = new PropertyListView<OrderItem>("items", shoppingCart.getOrder().getItems()) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(final ListItem<OrderItem> item) {

                item.add(new OrderItemPanel("item", item.getModel()));

                // XXX price hidden for now.
                /*
                 * item.add(new Label("price", item.getModel().getObject().getPrice()) {
                 * 
                 * @Override public <C> IConverter<C> getConverter(Class<C> type) { return new MoneyFormatConverter(Currency.getInstance("EUR"), 2); } });
                 */
                item.add(new RemoveLinkPanel("removeItemLink", item.getModel()));

                List<License> licenses = new ArrayList<License>();
                boolean isExpPackageLicenseChoiceShow = item.getModelObject().getExperiment() == null;
                if (isExpPackageLicenseChoiceShow) {
                    int experimentPackageId = item.getModelObject().getExperimentPackage().getExperimentPackageId();
                    licenses.addAll(licenseFacade.getLicenseForPackageAndOwnedByPerson(loggedUser.getPersonId(), experimentPackageId));
                    licenses.add(0, licenseFacade.getPublicLicense());
                }

                ChoiceRenderer<License> renderer = new ChoiceRenderer<License>("licenseInfo", "licenseId");
                DropDownChoice<License> licenseChoice = new DropDownChoice<License>("license", licenses, renderer);
                
                if (isExpPackageLicenseChoiceShow) {
                    // preselect first license for dropdown choice component
                    item.getModelObject().setLicense(licenseChoice.getChoices().get(0));
                }
                
                licenseChoice.add(new AjaxFormComponentUpdatingBehavior("onChange") {

                    private static final long serialVersionUID = 1L;

                    @Override
                    protected void onUpdate(AjaxRequestTarget target) {

                        OrderItem orderItem = item.getModelObject();
                        if (orderItem.getLicense() != null) {
                            orderItem.setPrice(orderItem.getLicense().getPrice());
                        } else {
                            orderItem.setPriceFromItem();
                        }
                        
                        target.add(container);
                        target.add(totalPriceLabel);
                    }
                });
                licenseChoice.setVisibilityAllowed(isExpPackageLicenseChoiceShow && !licenses.isEmpty());
                item.add(new Label("NoLicenseLabel", ResourceUtils.getModel("label.license.public")).setVisibilityAllowed(!(isExpPackageLicenseChoiceShow && !licenses.isEmpty())));
                item.add(licenseChoice);

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
                
                License publicLicense = licenseFacade.getPublicLicense();
                
                for(OrderItem item : order.getItems()) {
                    
                    if(item.getLicense() == null) {
                        item.setLicense(publicLicense);
                    }
                    
                    if(item.getPrice() == null) {
                        item.setPrice(BigDecimal.ZERO);
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

}
