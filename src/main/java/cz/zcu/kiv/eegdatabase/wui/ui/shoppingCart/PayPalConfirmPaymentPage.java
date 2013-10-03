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
 *   PayPalConfirmPaymentPage.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.ui.shoppingCart;

import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.logic.eshop.PayPalTools;
import cz.zcu.kiv.eegdatabase.wui.app.EEGDataBaseApplication;
import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Page for order confirmation after authorizing payment on PayPal server.
 * Part of the two-step confirmation process.
 * User: jfronek
 * Date: 21.3.2013
 */
@AuthorizeInstantiation(value = { "ROLE_READER", "ROLE_USER", "ROLE_EXPERIMENTER", "ROLE_ADMIN" })
public class PayPalConfirmPaymentPage extends MenuPage{
    private static final int ITEMS_PER_PAGE = 20;

    public PayPalConfirmPaymentPage(PageParameters parameters){
        // Token received from PayPal server. It contains token value for current order.
        String token = parameters.get("token").toString();
        if(token == null){
            throw new RestartResponseAtInterceptPageException(EEGDataBaseApplication.get().getHomePage());
        }
        setupComponents(token);
    }

    private void setupComponents(final String token) {
        IModel<String> title = ResourceUtils.getModel("pageTitle.confirmYourPayment");
        add(new Label("title", title));
        setPageTitle(title);

        add(new Label("orderHeader", ResourceUtils.getModel("label.yourOrder")));

        DefaultDataTable<Experiment, String> list = new DefaultDataTable<Experiment, String>("list", createListColumns(),
                new CartDataProvider(), ITEMS_PER_PAGE);
        add(list);

        add(new Label("totalPriceMessage", ResourceUtils.getString("label.totalPrice") + " "));
        add(new Label("totalPriceAmount", new Model(){
            @Override
            public Serializable getObject(){
                String totalPrice =  "" + EEGDataBaseSession.get().getShoppingCart().getTotalPrice();
                return totalPrice;
            }
        }));
        add(new Label("totalPriceCurrency", " " + ResourceUtils.getString("currency.euro")));

        add(new Link<Void>("cancel") {

            private static final long serialVersionUID = 1L;

            @Override
            public void onClick() {
                setResponsePage(ShoppingCartPage.class);
            }
        });

        add(new Link<Void>("confirm") {

            private static final long serialVersionUID = 1L;

            @Override
            public void onClick() {
                boolean success = PayPalTools.doExpressCheckout(token);
                if(success){
                    // Copies current order to match the purchased items. Security measure to ensure the user is given exactly what he paid for.
                    EEGDataBaseSession.get().setAttribute("order", (ArrayList) ((ArrayList) EEGDataBaseSession.get().getShoppingCart().getOrder()).clone());
                    EEGDataBaseSession.get().getShoppingCart().getOrder().clear();
                    setResponsePage(MyDownloadsPage.class);
                }
                else{
                    setResponsePage(PaymentErrorPage.class);
                }
            }
        });

    }

    private List<? extends IColumn<Experiment, String>> createListColumns() {
        List<IColumn<Experiment, String>> columns = new ArrayList<IColumn<Experiment, String>>();

        columns.add(new PropertyColumn<Experiment, String>(ResourceUtils.getModel("dataTable.heading.number"), "experimentId", "experimentId"));
        columns.add(new PropertyColumn<Experiment, String>(ResourceUtils.getModel("dataTable.heading.scenarioTitle"), "scenario.title", "scenario.title"));
        return columns;
    }
}
