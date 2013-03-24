package cz.zcu.kiv.eegdatabase.wui.ui.shoppingCart;

import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.wui.app.EEGDataBaseApplication;
import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;
import cz.zcu.kiv.eegdatabase.wui.components.page.BasePage;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.table.TimestampPropertyColumn;
import cz.zcu.kiv.eegdatabase.wui.components.table.ViewLinkPanel;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.ExperimentsDetailPage;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jfronek
 * Date: 21.3.13
 * Time: 22:38
 * To change this template use File | Settings | File Templates.
 */
@AuthorizeInstantiation("ROLE_USER")
public class PayPalConfirmPaymentPage extends MenuPage{
    private static final int ITEMS_PER_PAGE = 20;

    public PayPalConfirmPaymentPage(PageParameters parameters){
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
