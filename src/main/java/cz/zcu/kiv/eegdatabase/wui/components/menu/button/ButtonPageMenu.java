package cz.zcu.kiv.eegdatabase.wui.components.menu.button;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;

import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;

public class ButtonPageMenu extends Panel {

    private static final long serialVersionUID = 6708846586316988188L;

    public ButtonPageMenu(String id, IButtonPageMenu[] listOfPages) {
        super(id);

        List<IButtonPageMenu> list = filterUnauthorizedPages(listOfPages);

        ListView<IButtonPageMenu> menu = new ListView<IButtonPageMenu>("menu", list) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(ListItem<IButtonPageMenu> item) {
                item.add(new BookmarkablePageLink("link", item.getModelObject().getPageClass(), item.getModelObject().getPageParameters())
                        .add(new Label("label", ResourceUtils.getModel(item.getModelObject().getPageTitleKey()))));
            }
        };

        add(menu);
    }

    private List<IButtonPageMenu> filterUnauthorizedPages(IButtonPageMenu[] listOfPages) {

        List<IButtonPageMenu> list = new ArrayList<IButtonPageMenu>();

        for (IButtonPageMenu item : listOfPages) {

            AuthorizeInstantiation annotation = ((AuthorizeInstantiation) item.getPageClass().getAnnotation(AuthorizeInstantiation.class));

            if (annotation != null) {
                Roles roles = new Roles(annotation.value());

                if (EEGDataBaseSession.get().hasAnyRole(roles)) {
                    list.add(item);
                }
            }
        }
        return list;

    }
}
