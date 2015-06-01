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
 *   MenuPage.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.components.page;


import cz.zcu.kiv.eegdatabase.wui.app.EEGDataBaseApplication;
import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;
import cz.zcu.kiv.eegdatabase.wui.components.feedback.BaseFeedbackMessagePanel;
import cz.zcu.kiv.eegdatabase.wui.components.menu.ddm.MainMenu;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.ui.account.AccountOverViewPage;
import cz.zcu.kiv.eegdatabase.wui.ui.search.MenuSearchPanel;
import cz.zcu.kiv.eegdatabase.wui.ui.security.RegistrationPage;
import cz.zcu.kiv.eegdatabase.wui.ui.shoppingCart.ShoppingCartPage;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.string.StringValue;

import java.io.Serializable;
import java.util.Calendar;

/**
 * MenuPage for EEGDatabase portal, added header section with information about logged user,
 * added base feedback panel and footer link.
 * 
 * @author Jakub Rinkes
 *
 */
public class MenuPage extends BasePage {

    private static final long serialVersionUID = -9208758802775141621L;

    private BaseFeedbackMessagePanel feedback;

    public MenuPage() {

        feedback = new BaseFeedbackMessagePanel("base_feedback");
        add(feedback);

        boolean signedIn = EEGDataBaseSession.get().isSignedIn();

        String labelMessage;
        Class<?> pageClass;
        String labelLink;

        if (signedIn) {
            labelMessage = ResourceUtils.getString("general.header.logged");
            labelMessage += EEGDataBaseSession.get().getLoggedUser().getUsername();
            labelLink = ResourceUtils.getString("general.page.myaccount.link");
            pageClass = AccountOverViewPage.class;
        } else {
            labelMessage = ResourceUtils.getString("general.header.notlogged");
            labelLink = ResourceUtils.getString("action.register");
            pageClass = RegistrationPage.class;
        }

        BookmarkablePageLink headerLink = new BookmarkablePageLink("userHeaderLink", pageClass);
        headerLink.add(new Label("linkLabel", labelLink));
        add(headerLink);

        BookmarkablePageLink cart = new BookmarkablePageLink("cart", ShoppingCartPage.class);
        String cartLabel = ResourceUtils.getString("general.page.myCart.link") + " ";
        if(signedIn){
            //
            cart.add(new Label("cartSizeLabel", new Model(){
                @Override
                public Serializable getObject(){
                    String cartSize =  "" + EEGDataBaseSession.get().getShoppingCart().size();
                    return cartSize;
                }
            }));

        }
        cart.add(new Label("cartLabel", cartLabel));
        cart.setVisibilityAllowed(signedIn);
        add(cart);

        Link<Void> link = new Link<Void>("logout") {

            private static final long serialVersionUID = 1L;

            @Override
            public void onClick() {
                EEGDataBaseSession.get().invalidate();
                setResponsePage(EEGDataBaseApplication.get().getHomePage());
            }
        };
        link.setVisibilityAllowed(signedIn);
        add(link);

        StringValue searchString = EEGDataBaseSession.get().getSearchString();
        MenuSearchPanel panel = new MenuSearchPanel("menuSearchPanel", searchString);
        add(panel);
        if (!signedIn) {
            panel.setVisible(false);
        }

        add(new Label("userLogStatusLabel", labelMessage));

        add(new MainMenu("mainMenu"));

        add(new ExternalLink("footerLink", ResourceUtils.getString("general.footer.link"), ResourceUtils.getString("general.footer.link.title")));

        int year = Calendar.getInstance().get(Calendar.YEAR);

        add(new Label("year", ResourceUtils.getString("general.footer.year") + year));
    }

    public BaseFeedbackMessagePanel getFeedback() {
        return feedback;
    }
    
    @Override
    public void renderHead(IHeaderResponse response) {
        response.render(CssHeaderItem.forUrl("/files/wizard-style.css"));
        super.renderHead(response);
    }
}
