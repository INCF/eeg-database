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

    public GenericValidator(GenericFacade<T, Integer> service){
        this.service = service;
    }

    @Override
    public void validate(IValidatable<T> validatable) {
        T validatableEntity = validatable.getValue();
        if(validatableEntity == null) {
            error(validatable, ResourceUtils.getString("error.YouAreTryingToAddNonexistingEntity"));
            return;
        }
        List<T> resultEntitites = service.getUnique(validatableEntity);
        if(resultEntitites.size() == 1) {
            // It is valid
        } else if(resultEntitites.size() > 1) {
            error(validatable, ResourceUtils.getString("error.ThereAreMoreEntitiesWithSameName"));
        } else {
            error(validatable, ResourceUtils.getString("error.YouAreTryingToAddNonexistingEntity"));
        }
    }

    private void error(IValidatable<T> validatable, String message) {
        ValidationError error = new ValidationError();
        error.setMessage(message);
        validatable.error(error);
    }
}
