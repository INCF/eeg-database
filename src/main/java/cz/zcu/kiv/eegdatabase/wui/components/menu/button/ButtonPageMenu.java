package cz.zcu.kiv.eegdatabase.wui.components.menu.button;

import java.util.Arrays;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;

import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;

public class ButtonPageMenu extends Panel {

    private static final long serialVersionUID = 6708846586316988188L;

    public ButtonPageMenu(String id, IButtonPageMenu[] listOfPages) {
        super(id);

        ListView<IButtonPageMenu> menu = new ListView<IButtonPageMenu>("menu", Arrays.asList(listOfPages)) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(ListItem<IButtonPageMenu> item) {
                item.add(new BookmarkablePageLink("link", item.getModelObject().getPageClass())
                        .add(new Label("label", ResourceUtils.getModel(item.getModelObject().getPageTitleKey()))));
            }
        };

        add(menu);
    }
}
