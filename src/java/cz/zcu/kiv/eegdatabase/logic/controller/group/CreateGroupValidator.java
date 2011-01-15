package cz.zcu.kiv.eegdatabase.logic.controller.group;

import cz.zcu.kiv.eegdatabase.data.dao.ResearchGroupDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class CreateGroupValidator implements Validator {
    @Autowired
    private ResearchGroupDao researchGroupDao;

    public boolean supports(Class aClass) {
        return aClass.equals(CreateGroupCommand.class);
    }

    public void validate(Object command, Errors errors) {
        CreateGroupCommand data = (CreateGroupCommand) command;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "researchGroupTitle", "required.field");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "researchGroupDescription", "required.field");

        if (!researchGroupDao.canSaveTitle(data.getResearchGroupTitle(), data.getId())) {
            errors.rejectValue("researchGroupTitle", "error.valueAlreadyInDatabase");
        }
    }
}
