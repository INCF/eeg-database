package cz.zcu.kiv.eegdatabase.wui.ui.shoppingCart;

import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

/**
 * Created with IntelliJ IDEA.
 * User: jfronek
 * Date: 4.3.13
 * Time: 14:13
 * To change this template use File | Settings | File Templates.
 */
public class BuyLinkPanel  extends Panel {

    private static final long serialVersionUID = 5458856518415845451L;

    public BuyLinkPanel(String id, IModel<Experiment> model) {
        super(id);
        final Experiment experiment = model.getObject();
        add(new Link<Void>("link") {

            private static final long serialVersionUID = 1L;

            @Override
            public void onClick() {
                EEGDataBaseSession.get().getShoppingCart().addToCart(experiment);
            }
        }.add(new Label("label", ResourceUtils.getModel("link.addToCart")))
         .setVisibilityAllowed(!EEGDataBaseSession.get().getShoppingCart().isInCart(experiment)));
    }
}
