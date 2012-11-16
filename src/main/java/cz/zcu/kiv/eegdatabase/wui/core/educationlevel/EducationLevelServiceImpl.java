package cz.zcu.kiv.eegdatabase.wui.core.educationlevel;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import cz.zcu.kiv.eegdatabase.data.dao.EducationLevelDao;
import cz.zcu.kiv.eegdatabase.data.pojo.EducationLevel;

public class EducationLevelServiceImpl implements EducationLevelService {

    EducationLevelDao educationLevelDAO;

    @Required
    public void setEducationLevelDAO(EducationLevelDao educationLevelDAO) {
        this.educationLevelDAO = educationLevelDAO;
    }

    @Override
    @Transactional(readOnly = true)
    public List<EducationLevelDTO> getAllRecords() {
        return convertEntities(educationLevelDAO.getAllRecords());
    }

    private List<EducationLevelDTO> convertEntities(List<EducationLevel> entities) {
        List<EducationLevelDTO> list = new ArrayList<EducationLevelDTO>(entities.size());
        
        for(EducationLevel entity : entities){
            EducationLevelDTO dto = new EducationLevelDTO();
            dto.setId(entity.getEducationLevelId());
            dto.setTitle(entity.getTitle());
            dto.setDefaultNumber(entity.getDefaultNumber());
            list.add(dto);
        }
        
        return list;
    }

}
