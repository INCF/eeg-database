package cz.zcu.kiv.eegdatabase.wui.components.table;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.utils.PageParametersUtils;

public class ViewLinkPanel extends Panel {

    private static final long serialVersionUID = 5458856518415845451L;

    public ViewLinkPanel(String id, Class<? extends MenuPage> page, String propertyExpression, IModel model) {
        super(id);
        PropertyModel viewModel = new PropertyModel(model, propertyExpression);
        add(new BookmarkablePageLink("link", page, PageParametersUtils.getDefaultPageParameters(viewModel.getObject()))
                .add(new Label("label", viewModel)));
    }
    
    public ViewLinkPanel(String id, Class<? extends MenuPage> page, String propertyExpression, IModel model, String displayProperty) {
        super(id);
        PropertyModel paramModel = new PropertyModel(model, propertyExpression);
        PropertyModel viewModel = new PropertyModel(model, displayProperty);
        add(new BookmarkablePageLink("link", page, PageParametersUtils.getDefaultPageParameters(paramModel.getObject()))
                .add(new Label("label", viewModel)));
    }
    
    public ViewLinkPanel(String id, Class<? extends MenuPage> page, String propertyExpression, IModel model, IModel<String> displayModel) {
        super(id);
        PropertyModel param = new PropertyModel(model, propertyExpression);
        add(new BookmarkablePageLink("link", page, PageParametersUtils.getDefaultPageParameters(param.getObject()))
                .add(new Label("label", displayModel)));
    }
}
