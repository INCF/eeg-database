package cz.zcu.kiv.eegdatabase.logic.controller.person;

import cz.zcu.kiv.eegdatabase.data.dao.HearingImpairmentDao;
import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.pojo.HearingImpairment;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * @author JiPER
 */
public class AddHearingDefectToPersonValidator implements Validator {
    @Autowired
    private PersonDao personDao;
    @Autowired
    HearingImpairmentDao hearingImpairmentDao;
    private Log log = LogFactory.getLog(getClass());

    public boolean supports(Class clazz) {
        return clazz.equals(AddDefectToPersonCommand.class);
    }

    public void validate(Object command, Errors errors) {
        AddDefectToPersonCommand data = (AddDefectToPersonCommand) command;

        if (data.getDefectId() < 0) {
            errors.rejectValue("defectId", "required.field");
        }

        Person p = personDao.read(data.getSubjectId());
        HearingImpairment h = hearingImpairmentDao.read(data.getDefectId());
        if (p.getHearingImpairments().contains(h)) {  // field already exists
            errors.rejectValue("defectId", "invalid.paramIdAlreadyInserted");
        }
    }
}
