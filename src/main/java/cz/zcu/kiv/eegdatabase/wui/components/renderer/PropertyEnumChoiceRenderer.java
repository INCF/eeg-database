package cz.zcu.kiv.eegdatabase.wui.components.renderer;

import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;

public class PropertyEnumChoiceRenderer<T extends Enum<T>> extends ChoiceRenderer<T> {
    
    private static final long serialVersionUID = -3266244453054196496L;
    
    private String propertyExpression;

    public PropertyEnumChoiceRenderer(String propertyExpression) {
        super();
        this.propertyExpression = propertyExpression;
    }
    
    public PropertyEnumChoiceRenderer(String propertyExpression, String idExpression)
    {
        super(null, idExpression);
    }
    
    @Override
    public Object getDisplayValue(T object) {
        return new StringResourceModel(propertyExpression, new Model<T>(object), new Object[]{}).toString();
    }

}
