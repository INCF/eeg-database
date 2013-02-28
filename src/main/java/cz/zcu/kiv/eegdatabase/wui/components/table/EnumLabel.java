package cz.zcu.kiv.eegdatabase.wui.components.table;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;

public class EnumLabel<T extends Enum<T>> extends Label {

    private static final long serialVersionUID = 2303811444228963248L;

    public EnumLabel(String id, T object, String propertyExpression) {
        super(id, new StringResourceModel(propertyExpression, new Model<T>(object), new Object[] {}).toString());
    }
}
