package cz.zcu.kiv.eegdatabase.logic.controller.validation;

import cz.zcu.kiv.eegdatabase.data.dao.GenericDao;
import cz.zcu.kiv.eegdatabase.logic.controller.experiment.AddMetadataCommand;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import cz.zcu.kiv.eegdatabase.data.pojo.FileMetadataParamVal;
import cz.zcu.kiv.eegdatabase.data.pojo.FileMetadataParamValId;

/**
 *
 * @author JiPER
 */
public class AddFileMetadataValidator implements Validator {

    private Log log = LogFactory.getLog(getClass());
    private GenericDao<FileMetadataParamVal, FileMetadataParamValId> fileMetadataDao;

    public boolean supports(Class clazz) {
        return clazz.equals(AddMetadataCommand.class);
    }

    public void validate(Object command, Errors errors) {
        AddMetadataCommand addMetadataCommand = (AddMetadataCommand) command;
        log.debug("Validating form for adding file metadata.");

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "paramValue", "required.paramValue");

        if (addMetadataCommand.getParamId() <= 0) {
            errors.rejectValue("paramId", "required.paramId");
        }

        FileMetadataParamVal f = fileMetadataDao.read(new FileMetadataParamValId(addMetadataCommand.getParamId(), addMetadataCommand.getDataId()));
        if (f != null) {
            errors.rejectValue("paramId", "invalid.paramIdAlreadyInserted");
        }
    }

    public GenericDao<FileMetadataParamVal, FileMetadataParamValId> getFileMetadataDao() {
        return fileMetadataDao;
    }

    public void setFileMetadataDao(GenericDao<FileMetadataParamVal, FileMetadataParamValId> fileMetadataDao) {
        this.fileMetadataDao = fileMetadataDao;
    }
}
