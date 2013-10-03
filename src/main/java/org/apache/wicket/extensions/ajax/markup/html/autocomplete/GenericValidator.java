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
 *   GenericValidator.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package org.apache.wicket.extensions.ajax.markup.html.autocomplete;

import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.GenericFacade;
import org.apache.wicket.validation.INullAcceptingValidator;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.ValidationError;

import java.io.Serializable;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Jakub Balhar
 * Date: 11.5.13
 * Time: 14:23
 */
public class GenericValidator<T> implements INullAcceptingValidator<T>, Serializable {
    protected GenericFacade<T, Integer> service;

    List list = null;
    T object = null;

    boolean required = false;

    public GenericValidator(GenericFacade<T, Integer> service){
        this.service = service;
    }

    @Override
    public void validate(IValidatable<T> validatable) {

        T validatableEntity = validatable.getValue();

        if(list == null){
            if(object == null && validatableEntity == null){
                if(required)
                    error(validatable, ResourceUtils.getString("error.nonexistingEntity"));
                return;
            }
            if(validatableEntity == null) {
                validatableEntity = object;
            }
        }
        else if((list.size() < 2) && validatableEntity == null){
            if(required)
                errorNonExisting(validatable);
            return;
        }

        if(validatableEntity != null){
            List<T> resultEntitites = service.getUnique(validatableEntity);
            if(resultEntitites.size() == 1) {
                    // It is valid
            } else if(resultEntitites.size() > 1) {
                error(validatable, ResourceUtils.getString("error.ThereAreMoreEntitiesWithSameName"));
            } else if(resultEntitites.size() == 0) {
                // The entity does not exists and input object is null (not empty)
                errorNonExisting(validatable);
            } else if ((list == null || list.isEmpty())) {
                errorNonExisting(validatable);
            }
        }
    }

    private void errorNonExisting(IValidatable<T> validatable){
        if (required)
            error(validatable, ResourceUtils.getString("error.ExistingEntityRequired"));
        else
            error(validatable, ResourceUtils.getString("error.nonexistingEntity"));
    }


    public void setList(List list){
        this.list = list;
    }

    public void setAutocompleteObject(T object){
        this.object = object;
    }

    public void setRequired(boolean isRequired){
        this.required = isRequired;
    }

    private void error(IValidatable<T> validatable, String message) {
        ValidationError error = new ValidationError();
        error.setMessage(message);
        validatable.error(error);
    }
}
