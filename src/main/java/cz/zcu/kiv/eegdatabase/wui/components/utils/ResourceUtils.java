package cz.zcu.kiv.eegdatabase.wui.components.utils;

import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.resource.ContextRelativeResource;

public class ResourceUtils {

    public static String getString(String propertyExpression) {

        return new ResourceModel(propertyExpression).getObject();
    }

    public static IModel<String> getModel(String propertyExpression) {

        return new ResourceModel(propertyExpression);
    }
    
    public static String getString(String propertyExpression, IModel<?> model) {
        
        return new StringResourceModel(propertyExpression, model, new Object[]{}).getString();
    }
    
    public static IModel<String> getModel(String propertyExpression, IModel<?> model) {
        
        return new StringResourceModel(propertyExpression, model, new Object[]{});
    }
    
    public static Image getImage(String id, String imageFileName) {
        
        return new Image(id, new ContextRelativeResource("/images/"+imageFileName));
    }
    
}
