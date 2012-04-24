package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.EducationLevel;

import java.util.List;

/**
 * Dao for EducationLevel
 *
 * @author Jiri Novotny
 */
public interface EducationLevelDao extends GenericListDaoWithDefault<EducationLevel> {

    public List<EducationLevel> getEducationLevels(String title);

}
