package cz.zcu.kiv.eegdatabase.wui.components.utils;

import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.resource.ContextRelativeResource;

/**
 * Utilities class for getting string from properties file.
 * 
 * @author Jakub Rinkes
 *
 */
public class ResourceUtils {
    
    /**
     * Get string from properties where his key is propertyExpression.
     * 
     * @param propertyExpression
     * @return
     */
    public static String getString(String propertyExpression) {

        return new ResourceModel(propertyExpression).getObject();
    }
    
    /**
     * Get Model<String>() with string from properties where his key is propertyExpression.
     * 
     * @param propertyExpression
     * @return
     */
    public static IModel<String> getModel(String propertyExpression) {

        return new ResourceModel(propertyExpression);
    }
    
    /**
     * Get string from properties where his key is propertyExpression and model contains 
     * data for formatted string.
     * 
     * @param propertyExpression key for properties
     * @param model data for formatted string if string is prepared for formatting.
     * @return
     */
    public static String getString(String propertyExpression, IModel<?> model) {

        return new StringResourceModel(propertyExpression, model, new Object[] {}).getString();
    }
    
    /**
     * Get Model<String>() with string from properties where his key is propertyExpression 
     * and model contains data for formatted string.
     * 
     * @param propertyExpression key for properties
     * @param model data for formatted string if string is prepared for formatting.
     * @return
     */
    public static IModel<String> getModel(String propertyExpression, IModel<?> model) {

        return new StringResourceModel(propertyExpression, model, new Object[] {});
    }
    
    /**
     * Get image from directory images. Not tested.
     * 
     * @param id
     * @param imageFileName
     * @return
     */
    public static Image getImage(String id, String imageFileName) {

        return new Image(id, new ContextRelativeResource("/images/" + imageFileName));
    }

}
