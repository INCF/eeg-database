package cz.zcu.kiv.eegdatabase.wui.components.table;

import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.model.IModel;

public class StyleClassPropertyColumn<T, S> extends PropertyColumn<T, S> {

    private static final long serialVersionUID = 7884643917380980331L;

    private String styleClass;

    public StyleClassPropertyColumn(IModel<String> displayModel, String propertyExpression, String CSSClass) {
        super(displayModel, propertyExpression);
        this.styleClass = CSSClass;
    }

    public StyleClassPropertyColumn(IModel<String> displayModel, S sortProperty, String propertyExpression, String CSSClass) {
        super(displayModel, sortProperty, propertyExpression);
        this.styleClass = CSSClass;
    }

    @Override
    public String getCssClass() {
        return styleClass;
    }
}
