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
 *   BuyLinkPanel.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.ui.shoppingCart;

import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

/**
 * A Panel with a link for adding an experiment into user's shopping cart.
 * User: jfronek
 * Date: 4.3.2013
 */
public class BuyLinkPanel  extends Panel {

    private static final long serialVersionUID = 5458856518415845451L;

    /**
     * Constructor of BuyLinkPanel
     * @param id - component id
     * @param model - datamodel of related Experiment
     */
    public BuyLinkPanel(String id, IModel<Experiment> model) {
        super(id);
        final Experiment experiment = model.getObject();
        add(new Link<Void>("link") {

            private static final long serialVersionUID = 1L;

            @Override
            public void onClick() {
                EEGDataBaseSession.get().getShoppingCart().addToCart(experiment);
            }
        }.add(new Label("label", ResourceUtils.getModel("link.addToCart"))).setVisible(false)); //TODO set me visible
        // .setVisibilityAllowed(!EEGDataBaseSession.get().getShoppingCart().isInCart(experiment)));
        // "Add to Cart" links are rendered only for experiments that haven't been places in the cart yet.
    }
}
