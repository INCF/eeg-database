package cz.zcu.kiv.eegdatabase.logic.controller.list.artifact;

import cz.zcu.kiv.eegdatabase.data.dao.GenericListDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Artifact;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Created with IntelliJ IDEA.
 * User: Honza
 * Date: 24.6.12
 * Time: 18:51
 * To change this template use File | Settings | File Templates.
 */
public class AddArtifactValidator implements Validator {
    private Log log = LogFactory.getLog(getClass());
    @Autowired
    private GenericListDao<Artifact> artifactDao;

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(AddArtifactCommand.class);  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void validate(Object o, Errors errors) {
        AddArtifactCommand data = (AddArtifactCommand) o;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "compensation", "required.field");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "rejectCondition", "required.field");

    }
}
