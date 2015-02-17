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
 *   OrderDataProvider.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.ui.order;

import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;

import cz.zcu.kiv.eegdatabase.data.pojo.Order;
import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;
import cz.zcu.kiv.eegdatabase.wui.components.repeater.BasicDataProvider;
import cz.zcu.kiv.eegdatabase.wui.core.order.OrderFacade;

public class OrderDataProvider extends BasicDataProvider<Order> {

    private static final long serialVersionUID = 7294045302100220472L;

    private OrderFacade facade;

    public OrderDataProvider(OrderFacade facade) {
        super("date", SortOrder.ASCENDING);
        this.facade = facade;

        int personId = EEGDataBaseSession.get().getLoggedUser().getPersonId();

        super.listModel.setObject(this.facade.getAllOrdersForPerson(personId));
    }

    public OrderDataProvider(OrderFacade facade, int personId) {
        super("date", SortOrder.ASCENDING);
        this.facade = facade;

        super.listModel.setObject(this.facade.getAllOrdersForPerson(personId));
    }

}
