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
 *   PaymentErrorPage.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.ui.shoppingCart;

import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.ui.home.HomePage;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * Error page for capturing any error that occurs during the payment processing.
 * For security reasons, it doesn't reveal any error description, only suggests the user to continue either
 * to Homepage or My Cart page.
 * User: jfronek
 * Date: 21.3.2013
 */
@AuthorizeInstantiation(value = { "ROLE_READER", "ROLE_USER", "ROLE_EXPERIMENTER", "ROLE_ADMIN" })
public class PaymentErrorPage extends MenuPage{

    public PaymentErrorPage(){
        setupComponents();
    }

    public PaymentErrorPage(PageParameters parameters){
        setupComponents();
    }

    private void setupComponents() {
        IModel<String> title = ResourceUtils.getModel("error.payment.title");
        add(new Label("title", title));
        setPageTitle(title);
        add(new Label("message", ResourceUtils.getModel("error.payment.message")));

        add(new BookmarkablePageLink("goToHomePage", HomePage.class));

        add(new BookmarkablePageLink("goToMyCart", ShoppingCartPage.class));

    }
}
