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
 *   OrderDetailPage.java, 2014/14/09 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.ui.order;

import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PropertyListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;

import cz.zcu.kiv.eegdatabase.data.pojo.License;
import cz.zcu.kiv.eegdatabase.data.pojo.Order;
import cz.zcu.kiv.eegdatabase.data.pojo.OrderItem;
import cz.zcu.kiv.eegdatabase.wui.components.menu.button.ButtonPageMenu;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.table.TimestampLabel;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.components.utils.StringUtils;
import cz.zcu.kiv.eegdatabase.wui.core.order.OrderFacade;
import cz.zcu.kiv.eegdatabase.wui.ui.home.HomePage;
import cz.zcu.kiv.eegdatabase.wui.ui.shoppingCart.ShoppingCartPageLeftMenu;

@AuthorizeInstantiation(value = { "ROLE_READER", "ROLE_USER", "ROLE_EXPERIMENTER", "ROLE_ADMIN" })
public class OrderDetailPage extends MenuPage {

    private static final long serialVersionUID = -483958624352734792L;

    @SpringBean
    private OrderFacade orderFacade;
    
    public OrderDetailPage(PageParameters parameters) {

        StringValue orderIdParam = parameters.get(DEFAULT_PARAM_ID);

        if (orderIdParam.isEmpty() || orderIdParam.isNull()) {

            throw new RestartResponseAtInterceptPageException(HomePage.class);

        } else {

            int orderId = orderIdParam.toInt();
            setupComponents(orderId);
        }
    }

    private void setupComponents(int orderId) {

        final Order order = orderFacade.getOrderForDetail(orderId);

        IModel<String> title = ResourceUtils.getModel("pageTitle.order.detail", order.getId());
        add(new Label("title", title));
        setPageTitle(title);
        add(new ButtonPageMenu("leftMenu", ShoppingCartPageLeftMenu.values()));

        Label idLabel = new Label("id", order.getId());
        TimestampLabel date = new TimestampLabel("date", order.getDate(), StringUtils.DATE_TIME_FORMAT_PATTER);
        Label priceLabel = new Label("orderPrice", order.getOrderPrice());
        Label person = new Label("person", order.getPerson().getFullName());

        PropertyListView<OrderItem> items = new PropertyListView<OrderItem>("items", order.getItems()) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(ListItem<OrderItem> item) {

                item.add(new OrderItemPanel("item", item.getModel()));
                item.add(new Label("price", item.getModel().getObject().getPrice()));
                item.add(new Label("license", item.getModelObject().getLicense().getLicenseInfo()));
            }
        };

        add(idLabel, date, priceLabel, person, items);

    }

}
