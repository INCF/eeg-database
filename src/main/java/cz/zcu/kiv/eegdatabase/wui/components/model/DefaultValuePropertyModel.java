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
 *   DefaultValuePropertyModel.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.components.model;

import java.io.Serializable;

import org.apache.wicket.model.PropertyModel;

/**
 * Implementation of wicket model with default value. If model is null then return default value.
 * 
 * @author Jakub Rinkes
 * 
 * @param <T>
 *            type of model object.
 */
public class DefaultValuePropertyModel<T extends Serializable> extends PropertyModel<T> {

    private static final long serialVersionUID = 1L;
    private T defaultValue;

    public DefaultValuePropertyModel(Object object, String propertyKey, T defaultValue) {
        
        super(object, propertyKey);
        this.defaultValue = defaultValue;
    }

    @Override
    public T getObject() {
        
        T object = super.getObject();
        return object != null ? object : defaultValue;
    }

}
