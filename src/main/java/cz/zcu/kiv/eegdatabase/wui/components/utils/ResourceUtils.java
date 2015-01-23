/*******************************************************************************
 * This file is part of the EEG-database project
 * 
 *   ==========================================
 *  
 *   Copyright (C) 2013 by University of West Bohemia (http://www.zcu.cz/en/)
 *  
 *  ***********************************************************************************************************************
 *  
 *   Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 *   the License. You may obtain a copy of the License at
 *  
 *       http://www.apache.org/licenses/LICENSE-2.0
 *  
 *   Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 *   an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 *   specific language governing permissions and limitations under the License.
 *  
 *  ***********************************************************************************************************************
 *  
 *   ResourceUtils.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.components.utils;

import java.io.File;
import java.io.IOException;

import org.apache.velocity.runtime.resource.loader.ResourceLoaderFactory;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.resource.ContextRelativeResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.ResourceLoader;

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
     * Get string with string from properties where his key is propertyExpression
     * and params contains data for formatted string.
     * 
     * @param propertyExpression
     * @param params
     * @return
     */
    public static String getString(String propertyExpression, Object... params) {

        return new StringResourceModel(propertyExpression, null, params).getString();
    }
    
    /**
     * Get IModel<String> with string from properties where his key is propertyExpression
     * and params contains data for formatted string.
     * 
     * @param propertyExpression
     * @param params
     * @return
     */
    public static IModel<String> getModel(String propertyExpression, Object... params) {

        return new StringResourceModel(propertyExpression, null, params);
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
    
    public static File getFile(String filePath) throws IOException {

        return new ClassPathResource(filePath).getFile();
    }

}
