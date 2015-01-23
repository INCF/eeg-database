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
 *   RemoveLinkPanel.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.ui.shoppingCart;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import cz.zcu.kiv.eegdatabase.data.pojo.OrderItem;
import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;

public class RemoveLinkPanel extends Panel {

    private static final long serialVersionUID = -2589037934890834710L;

    private OrderItem orderItem;

    public RemoveLinkPanel(String id, IModel<OrderItem> model) {
        super(id);
        orderItem = model.getObject();

        add(new Link<Void>("link") {

            private static final long serialVersionUID = 1L;

            @Override
            public void onClick() {

                EEGDataBaseSession.get().getShoppingCart().removeFromCart(orderItem);
                setResponsePage(ShoppingCartPage.class);
            }

        }.add(new Label("label", ResourceUtils.getModel("link.removeFromCart"))));
    }
}
