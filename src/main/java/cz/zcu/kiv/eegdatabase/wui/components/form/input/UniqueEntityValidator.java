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
 *   UniqueEntityValidator.java, 2013/28/11 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.components.form.input;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;

import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.GenericFacade;

public class UniqueEntityValidator<T> implements IValidator<T> {

    private static final long serialVersionUID = 1L;
    
    private GenericFacade<T, Integer> facade;

    public UniqueEntityValidator(GenericFacade<T, Integer> facade) {
        this.facade = facade;
    }

    @Override
    public void validate(IValidatable<T> validatable) {
        T validatableEntity = validatable.getValue();

        List<T> resultEntitites = facade.getUnique(validatableEntity);

        if (resultEntitites.size() == 1) {
            // It is valid
        } else if (resultEntitites.size() > 1) {
            error(validatable, ResourceUtils.getString("error.ThereAreMoreEntitiesWithSameName"));
        } else if (resultEntitites.size() == 0) {
            // The entity does not exists and input object is null (not empty)
            error(validatable, ResourceUtils.getString("error.nonexistingEntity"));
        }
    }
    
    private void error(IValidatable<T> validatable, String message) {
        ValidationError error = new ValidationError();
        error.setMessage(message);
        validatable.error(error);
    }

}
