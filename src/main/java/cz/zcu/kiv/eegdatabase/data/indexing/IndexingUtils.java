package cz.zcu.kiv.eegdatabase.data.indexing;

import cz.zcu.kiv.eegdatabase.data.annotation.Indexed;
import cz.zcu.kiv.eegdatabase.data.annotation.SolrId;
import cz.zcu.kiv.eegdatabase.data.dao.*;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import org.reflections.Reflections;

import javax.persistence.Entity;
import java.lang.reflect.Field;
import java.util.*;

/**
 * Utility class containing a set of useful indexing-related methods.
 * User: Jan Koren
 * Date: 4.3.13
 */
public class IndexingUtils {

    private static final String POJO_BASE="cz.zcu.kiv.eegdatabase.data.pojo";

    public static final String SOURCE_DATABASE = "database";
    public static final String SOURCE_LINKEDIN = "linkedin";

    /**
     * Picks the classes that use the @SolrId annotation.
     * @return The classes annotated by the @SolrId annotation.
     */
    public static List<Class<?>> getIndexableClasses() {
        // scan the package where all domain classes reside
        Reflections reflections = new Reflections(POJO_BASE);

        // get all domain classes to be indexed
        Set<Class<?>> pojos = reflections.getTypesAnnotatedWith(Indexed.class);
        List<Class<?>> solrAnotated = new ArrayList<Class<?>>();
        int pojoCount =  pojos.size();
        Iterator<Class<?>> subtypesIterator = pojos.iterator();

        // pick the classes containing the @SolrId field annotation
        while (subtypesIterator.hasNext()) {
            Class<?> clazz = subtypesIterator.next();
            Field[] fields= clazz.getDeclaredFields();
            for(Field field: fields) {
                if(field.isAnnotationPresent(SolrId.class)) {
                    solrAnotated.add(clazz);
                    break;
                }
            }
        }

        return solrAnotated;
    }

    /**
     * Returns a defined set of DAO classes that are needed to perform indexing of desired POJOs.
     * @return The set of DAO classes.
     */
    public static final Set<Class <? extends GenericDao>> getDaosForIndexing() {
        Set<Class<? extends GenericDao>> classes = new HashSet<Class<? extends GenericDao>>();
        classes.add(ArticleDao.class);
        classes.add(ExperimentDao.class);
        classes.add(PersonDao.class);
        classes.add(ResearchGroupDao.class);
        classes.add(ScenarioDao.class);

        return classes;
    }

}
