package cz.zcu.kiv.eegdatabase.data.indexing;

import cz.zcu.kiv.eegdatabase.data.annotation.SolrId;
import org.reflections.Reflections;

import javax.persistence.Entity;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Jan Koren
 * Date: 4.3.13
 * Time: 17:12
 * To change this template use File | Settings | File Templates.
 */
public class IndexingUtils {

    private static final String POJO_BASE="cz.zcu.kiv.eegdatabase.data.pojo";

    /**
     * Picks the classes that use the @SolrId annotation.
     * @return The classes annotated by the @SolrId annotation.
     */
    public static List<Class<?>> getIndexableClasses() {
        // scan the package where all domain classes reside
        Reflections reflections = new Reflections(POJO_BASE);

        // get all domain classes
        // uses (depends on) the JPA @Entity annotation which must be present in the classes
        Set<Class<?>> pojos = reflections.getTypesAnnotatedWith(Entity.class);
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

}
