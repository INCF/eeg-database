package cz.zcu.kiv.eegdatabase.wui.ui.shoppingCart;

import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;
import cz.zcu.kiv.eegdatabase.wui.components.menu.button.ButtonPageMenu;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.table.TimestampPropertyColumn;
import cz.zcu.kiv.eegdatabase.wui.components.table.ViewLinkPanel;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.ExperimentsDetailPage;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.pages.RedirectPage;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jfronek
 * Date: 4.3.13
 * Time: 15:12
 * To change this template use File | Settings | File Templates.
 */
@AuthorizeInstantiation(value = { "ROLE_READER", "ROLE_USER", "ROLE_EXPERIMENTER", "ROLE_ADMIN" })
public class ShoppingCartPage extends MenuPage {

    private static final int ITEMS_PER_PAGE = 20;

    public ShoppingCartPage(){
        setupComponents();
    }

    private void setupComponents(){
        IModel<String> title = ResourceUtils.getModel("pageTitle.myCart");
        add(new Label("title", title));
        setPageTitle(title);

        add(new ButtonPageMenu("leftMenu", ShoppingCartPageLeftMenu.values()));

        add(new Label("emptyCart", ResourceUtils.getModel("text.emptyCart")){
            @Override
            public boolean isVisible(){
                return EEGDataBaseSession.get().getShoppingCart().isEmpty();
            }
        });

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

        add(new Link<Void>("PayPalExpressCheckoutLink") {

            private static final long serialVersionUID = 1L;

            @Override
            public void onClick() {
                if(!EEGDataBaseSession.get().getShoppingCart().isEmpty()){
                    setResponsePage(new RedirectPage(PayPalTools.setExpressCheckout()));
                }
                //Partially fixes trouble with browser caching and back button
                else {
                    setResponsePage(ShoppingCartPage.class);
                }

            }
        }.setVisibilityAllowed(!EEGDataBaseSession.get().getShoppingCart().isEmpty()));

    }

    private List<? extends IColumn<Experiment, String>> createListColumns() {
        List<IColumn<Experiment, String>> columns = new ArrayList<IColumn<Experiment, String>>();

        columns.add(new PropertyColumn<Experiment, String>(ResourceUtils.getModel("dataTable.heading.number"), "experimentId", "experimentId"));
        columns.add(new PropertyColumn<Experiment, String>(ResourceUtils.getModel("dataTable.heading.scenarioTitle"), "scenario.title", "scenario.title"));
        columns.add(new PropertyColumn<Experiment, String>(ResourceUtils.getModel("dataTable.heading.date"), "startTime", "startTime"));
        columns.add(new PropertyColumn<Experiment, String>(ResourceUtils.getModel("dataTable.heading.gender"), "personBySubjectPersonId.gender", "personBySubjectPersonId.gender"));
        columns.add(new TimestampPropertyColumn<Experiment, String>(ResourceUtils.getModel("dataTable.heading.yearOfBirth"), "personBySubjectPersonId.dateOfBirth",
                "personBySubjectPersonId.dateOfBirth", "yyyy"));

        columns.add(new PropertyColumn<Experiment, String>(ResourceUtils.getModel("dataTable.heading.detail"), null, null) {
            @Override
            public void populateItem(Item<ICellPopulator<Experiment>> item, String componentId, IModel<Experiment> rowModel) {
                item.add(new ViewLinkPanel(componentId, ExperimentsDetailPage.class, "experimentId", rowModel, ResourceUtils.getModel("link.detail")));
            }
        });

        //Add to cart
        columns.add(new PropertyColumn<Experiment, String>(ResourceUtils.getModel("dataTable.heading.remove"), null, null) {

            @Override
            public void populateItem(Item<ICellPopulator<Experiment>> item, String componentId, IModel<Experiment> rowModel) {
                item.add(new RemoveLinkPanel(componentId, rowModel));
            }
        });
        return columns;
    }

}

