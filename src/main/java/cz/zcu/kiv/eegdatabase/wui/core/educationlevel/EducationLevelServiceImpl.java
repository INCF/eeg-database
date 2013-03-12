package cz.zcu.kiv.eegdatabase.wui.core.educationlevel;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import cz.zcu.kiv.eegdatabase.data.dao.EducationLevelDao;
import cz.zcu.kiv.eegdatabase.data.pojo.EducationLevel;

public class EducationLevelServiceImpl implements EducationLevelService {
    
    protected Log log = LogFactory.getLog(getClass());

    EducationLevelDao educationLevelDAO;

    @Required
    public void setEducationLevelDAO(EducationLevelDao educationLevelDAO) {
        this.educationLevelDAO = educationLevelDAO;
    }

    @Override
    @Transactional(readOnly = true)
    public List<EducationLevel> getAllRecords() {
        return educationLevelDAO.getAllRecords();
    }

}
