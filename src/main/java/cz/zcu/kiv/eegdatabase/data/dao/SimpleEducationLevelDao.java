package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.EducationLevel;
import cz.zcu.kiv.eegdatabase.logic.controller.search.SearchRequest;
import cz.zcu.kiv.eegdatabase.logic.util.ControllerUtils;
import org.hibernate.tool.ant.QueryExporterTask;
import org.springframework.dao.support.DataAccessUtils;

import java.util.*;

/**
 * This class extends powers class SimpleGenericDao.
 * Class is determined only for EducationLevel.
 *
 * @author Jiri Novotny
 */
public class SimpleEducationLevelDao
        extends SimpleGenericDao<EducationLevel, Integer> implements EducationLevelDao {

    public SimpleEducationLevelDao() {
        super(EducationLevel.class);
    }

    /**
     * Finds all education levels with the specified title
     * @param title - title property value
     * @return List of EducationLevel entities with searched title
     */
    public List<EducationLevel> getEducationLevels(String title) {
       String HQLselect = "from EducationLevel level " + "where level.title = :title";
       List<EducationLevel> results = getHibernateTemplate().findByNamedParam(HQLselect, "title", title);
       return results;
    }
}
