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
 * Time: 18:07
 * To change this template use File | Settings | File Templates.
 */
public class RemoveLinkPanel extends Panel {

    public RemoveLinkPanel(String id, IModel<Experiment> model) {
        super(id);
        final Experiment experiment = model.getObject();
        add(new Link<Void>("link") {

            private static final long serialVersionUID = 1L;

            @Override
            public void onClick() {
                EEGDataBaseSession.get().getShoppingCart().getOrder().remove(experiment);
            }
        }.add(new Label("label", ResourceUtils.getModel("link.removeFromCart"))));
    }
}
