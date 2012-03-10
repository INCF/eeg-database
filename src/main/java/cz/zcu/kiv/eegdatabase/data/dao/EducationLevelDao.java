package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.EducationLevel;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.logic.controller.search.SearchRequest;

import java.util.List;
import java.util.Map;

/**
 * Dao for EducationLevel
 *
 * @author Jiri Novotny
 */
public interface EducationLevelDao extends GenericDao<EducationLevel, Integer> {

    public List<EducationLevel> getEducationLevels(String title);

}
