package cz.zcu.kiv.eegdatabase.wui.core.educationlevel;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;

public class EducationLevelFacadeImpl implements EducationLevelFacade {
    
    protected Log log = LogFactory.getLog(getClass());
    
    EducationLevelService service;
    
    @Required
    public void setService(EducationLevelService service) {
        this.service = service;
    }
    
    @Override
    public List<EducationLevelDTO> getAllRecords() {
        return service.getAllRecords();
    }

}
