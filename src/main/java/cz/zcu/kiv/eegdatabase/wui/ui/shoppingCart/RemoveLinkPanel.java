package cz.zcu.kiv.eegdatabase.wui.ui.shoppingCart;

import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

/**
 * A Panel with a link to remove Experiment for shopping cart.
 * User: jfronek
 * Date: 4.3.2013
 */
public class RemoveLinkPanel extends Panel {
    /**
     * Constructor
     * @param id component id
     * @param model datamodel of related experiment
     */
    public RemoveLinkPanel(String id, IModel<Experiment> model) {
        super(id);
        final Experiment experiment = model.getObject();
        add(new Link<Void>("link") {

            private static final long serialVersionUID = 1L;

            @Override
            public void onClick() {
                EEGDataBaseSession.get().getShoppingCart().removeFromCart(experiment);
                setResponsePage(ShoppingCartPage.class);
            }
        }.add(new Label("label", ResourceUtils.getModel("link.removeFromCart"))));
    }
}
