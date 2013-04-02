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
 * Created with IntelliJ IDEA.
 * User: jfronek
 * Date: 21.3.13
 * Time: 22:38
 * To change this template use File | Settings | File Templates.
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
