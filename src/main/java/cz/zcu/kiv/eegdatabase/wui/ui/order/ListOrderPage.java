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
 *   ListOrderPage.java, 2014/14/09 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.ui.order;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;

import cz.zcu.kiv.eegdatabase.data.pojo.Order;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;
import cz.zcu.kiv.eegdatabase.wui.components.menu.button.ButtonPageMenu;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.table.ViewLinkPanel;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.order.OrderFacade;
import cz.zcu.kiv.eegdatabase.wui.core.person.PersonFacade;
import cz.zcu.kiv.eegdatabase.wui.ui.home.HomePage;
import cz.zcu.kiv.eegdatabase.wui.ui.shoppingCart.ShoppingCartPageLeftMenu;

@AuthorizeInstantiation(value = { "ROLE_READER", "ROLE_USER", "ROLE_EXPERIMENTER", "ROLE_ADMIN" })
public class ListOrderPage extends MenuPage {

    private static final long serialVersionUID = -4812429463451519264L;
    private static final int ITEMS_PER_PAGE = 20;

    @SpringBean
    private PersonFacade personFacade;

    @SpringBean
    private OrderFacade orderFacade;

    public ListOrderPage() {

        int personId = EEGDataBaseSession.get().getLoggedUser().getPersonId();
        setupComponents(personId, true);
    }

    public ListOrderPage(PageParameters parameters) {

        StringValue personIdParam = parameters.get(DEFAULT_PARAM_ID);

        if (personIdParam.isEmpty() || personIdParam.isNull()) {

            throw new RestartResponseAtInterceptPageException(HomePage.class);

        } else {

            int personId = personIdParam.toInt();
            setupComponents(personId, false);
        }

    }

    private void setupComponents(int personId, boolean myOrders) {

        IModel<String> title;
        if (myOrders) {
            title = ResourceUtils.getModel("pageTitle.order.myOrders");
        } else {
            Person person = personFacade.read(personId);
            if (person == null)
                throw new RestartResponseAtInterceptPageException(HomePage.class);

            title = ResourceUtils.getModel("pageTitle.order.userOrders", person.getUsername());
        }

        add(new Label("title", title));
        setPageTitle(title);
        add(new ButtonPageMenu("leftMenu", ShoppingCartPageLeftMenu.values()));

        DefaultDataTable<Order, String> orders = new DefaultDataTable<Order, String>("orders", createListColumns(),
                new OrderDataProvider(orderFacade, personId), ITEMS_PER_PAGE);
        add(orders);

    }

    private List<? extends IColumn<Order, String>> createListColumns() {

        List<IColumn<Order, String>> columns = new ArrayList<IColumn<Order, String>>();

        columns.add(new PropertyColumn<Order, String>(ResourceUtils.getModel("dataTable.heading.id"), "id", "id"));
        columns.add(new PropertyColumn<Order, String>(ResourceUtils.getModel("dataTable.heading.date"), "date", "date"));
        columns.add(new PropertyColumn<Order, String>(ResourceUtils.getModel("dataTable.heading.order.price"), "orderPrice", "orderPrice"));

        columns.add(new PropertyColumn<Order, String>(null, null, null) {

            private static final long serialVersionUID = 1L;

            @Override
            public void populateItem(Item<ICellPopulator<Order>> item, String componentId, IModel<Order> rowModel) {
                item.add(new ViewLinkPanel(componentId, OrderDetailPage.class, "id", rowModel, ResourceUtils.getModel("link.detail")));
            }
        });
        return columns;

    }
}
