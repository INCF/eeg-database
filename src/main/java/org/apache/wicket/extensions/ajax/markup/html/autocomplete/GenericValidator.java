package org.apache.wicket.extensions.ajax.markup.html.autocomplete;

import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.GenericFacade;
import org.apache.bcel.generic.RETURN;
import org.apache.wicket.PageReference;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.validation.INullAcceptingValidator;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.ValidationError;

import java.io.Serializable;
import java.lang.reflect.Constructor;
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
                errorNonExisting(validatable);
                return;
            }
            if(validatableEntity == null)
                validatableEntity = object;
        }
        else if((list.size() < 2) && validatableEntity == null){
            errorNonExisting(validatable);
            return;
        }

        if(validatableEntity != null){
            List<T> resultEntitites = service.getUnique(validatableEntity);
            if(resultEntitites.size() == 1) {
                    // It is valid
            } else if(resultEntitites.size() > 1) {
                error(validatable, ResourceUtils.getString("error.ThereAreMoreEntitiesWithSameName"));
            }
            else if ((list == null || list.isEmpty())) {
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
